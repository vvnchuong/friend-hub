package com.friendhub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.friendhub.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Enumerated(EnumType.STRING)
    UserRole name;

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    List<User> users = new ArrayList<>();

}
