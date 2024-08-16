package com.example.demo4.SecurityApp.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {

    private Long id;
    private String email;
    private String name;
    private String subscriptionPlan;
}
