package com.example.demo4.SecurityApp.dto;

import com.example.demo4.SecurityApp.entities.enums.Permission;
import com.example.demo4.SecurityApp.entities.enums.Role;
import lombok.*;

import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SignUpDto {
    private String email;
    private String password;
    private String name;
    private Set<Role> roles;
    private Set<Permission> permissions;
    private String subscriptionPlan;
}
