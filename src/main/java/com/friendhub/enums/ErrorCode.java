package com.friendhub.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

//  10000 – 19999  Auth
//  20000 – 29999  User
//  30000 – 39999  Post
//  40000 – 49999  Comment
//  50000 – 59999  Friend / Social graph
//  60000 – 69999  Chat / Messaging
//  70000 – 79999  Notification
//  80000 – 89999  File / Media / Upload
//  90000 – 99999  System / Misc

    UNAUTHENTICATED(1001, "Unauthenticated.", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1002, "You do not have permission.", HttpStatus.FORBIDDEN),
    EMAIL_NOT_FOUND(1003, "Email not found.", HttpStatus.UNAUTHORIZED),

    USER_NOT_FOUND(2001, "User not found.", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTED(2002, "User already existed.", HttpStatus.CONFLICT),
    ROLE_NOT_FOUND(2003, "Role not found.", HttpStatus.NOT_FOUND),
    INVALID_OLD_PASSWORD(2004, "Invalid old password", HttpStatus.BAD_REQUEST),
    PASSWORD_CONFIRM_MISMATCH(2005, "Password confirm mismatch.", HttpStatus.BAD_REQUEST),
    USER_BANNED(2007, "Your account has been disabled due to a violation of our community standards.", HttpStatus.FORBIDDEN),
    USER_DEACTIVATED(2008, "User account has been deactivated.", HttpStatus.FORBIDDEN),

    POST_NOT_FOUND(3001, "Post not found.", HttpStatus.NOT_FOUND),

    ALREADY_FRIENDS(5001, "You are already friends.", HttpStatus.BAD_REQUEST),
    FRIEND_REQUEST_ALREADY(5002, "Friend request is already pending.", HttpStatus.BAD_REQUEST),
    FRIEND_REQUEST_NOT_FOUND(5003, "Friend request not found.", HttpStatus.NOT_FOUND),
    GROUP_NOT_FOUND(5004, "Group not found.", HttpStatus.NOT_FOUND),
    GROUP_MEMBER_NOT_FOUND(5005, "Group member not found.", HttpStatus.NOT_FOUND),
    CANNOT_REMOVE_GROUP_ADMIN(5006, "Cannot remove admin from group.", HttpStatus.FORBIDDEN),
    ALREADY_MEMBER(5007, "User is already a member.", HttpStatus.BAD_REQUEST),
    NOT_A_GROUP_MEMBER(5008, "You are not a member of this group.", HttpStatus.FORBIDDEN),
    CANNOT_LEAVE_GROUP_AS_ONLY_ADMIN(5009, "Cannot leave group as the only admin.", HttpStatus.FORBIDDEN),
    JOIN_REQUEST_ALREADY_PENDING(5010, "You already have a pending request.", HttpStatus.BAD_REQUEST),
    JOIN_REQUEST_NOT_FOUND(5011, "Join request not found.", HttpStatus.NOT_FOUND),
    JOIN_REQUEST_ALREADY_HANDLED(5012, "This request has already been handled.", HttpStatus.BAD_REQUEST),
    POST_NOT_IN_GROUP(5013, "Post does not belong to this group.", HttpStatus.BAD_REQUEST),
    GROUP_IS_PRIVATE(5014, "Group is private.", HttpStatus.BAD_REQUEST),
    GROUP_IS_PUBLIC(5015, "Group is public.", HttpStatus.BAD_REQUEST),
    INVALID_JOIN_REQUEST_STATE(5016, "Invalid join request state", HttpStatus.BAD_REQUEST),
    INVALID_JOIN_REQUEST(5017, "Invalid join request", HttpStatus.BAD_REQUEST),
    COMMENT_DISABLED_BY_ADMIN(5018, "Comment disabled by admin.", HttpStatus.FORBIDDEN),
    GROUP_NAME_INVALID(5019, "Invalid group name", HttpStatus.BAD_REQUEST),
    REPORT_NOT_FOUND(5020, "Report not found.", HttpStatus.NOT_FOUND),
    ALREADY_REPORTED(5021, "You are already reported.", HttpStatus.BAD_REQUEST),
    INVALID_REPORT_STATE(5022, "Invalid report state", HttpStatus.BAD_REQUEST),
    CANNOT_REPORT_OWN_CONTENT(5023, "You can't report your post.", HttpStatus.BAD_REQUEST),

    FILE_EMPTY(8001, "File is empty. Please upload a file.", HttpStatus.BAD_REQUEST),
    INVALID_FILE(8002, "Invalid file.", HttpStatus.UNSUPPORTED_MEDIA_TYPE),

    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error.", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

}
