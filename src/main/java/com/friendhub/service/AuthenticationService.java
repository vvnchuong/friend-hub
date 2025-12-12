package com.friendhub.service;

import com.friendhub.dto.request.AuthenticationRequest;
import com.friendhub.dto.request.IntrospectRequest;
import com.friendhub.dto.response.AuthenticationResponse;
import com.friendhub.dto.response.IntrospectResponse;
import com.friendhub.entity.User;
import com.friendhub.enums.ErrorCode;
import com.friendhub.exception.AppException;
import com.friendhub.repository.InvalidatedTokenRepository;
import com.friendhub.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthenticationService {

    private final InvalidatedTokenRepository invalidatedTokenRepository;

    private final UserRepository userRepository;

    @Value("${jwt.signerKey}")
    private String SIGNED_KEY;

    @Value("${jwt.validDuration}")
    private long VALID_DURATION;

    public AuthenticationService(InvalidatedTokenRepository invalidatedTokenRepository,
                                 UserRepository userRepository) {
        this.invalidatedTokenRepository = invalidatedTokenRepository;
        this.userRepository = userRepository;
    }

    public IntrospectResponse introspect(IntrospectRequest request) {
        String token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token);
        } catch (JOSEException | ParseException e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .validated(isValid)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_FOUND));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(token)
                .build();
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNED_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date())))
            throw new RuntimeException();

        if (invalidatedTokenRepository
                .existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new RuntimeException();

        return signedJWT;
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getFirstName() + " " + user.getLastName())
                .issuer("friendhub.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now()
                        .plus(VALID_DURATION, ChronoUnit.SECONDS)
                        .toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("user_id", user.getId())
                .claim("email", user.getEmail())
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .claim("scope", user.getRole().getName())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNED_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

}
