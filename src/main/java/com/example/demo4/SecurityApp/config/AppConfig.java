package com.example.demo4.SecurityApp.config;

import com.example.demo4.SecurityApp.dto.SignUpDto;
import com.example.demo4.SecurityApp.dto.UserDto;
import com.example.demo4.SecurityApp.entities.User;
import com.example.demo4.SecurityApp.entities.enums.SubscriptionPlan;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    @Bean
    ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.typeMap(SignUpDto.class , User.class).setConverter(context->{
//            SignUpDto signUpDto = context.getSource();
//            SubscriptionPlan subscriptionPlan = SubscriptionPlan.valueOf(signUpDto.getSubscriptionPlan());
//            User user = context.getDestination();
//            user.setSubscriptionPlan(subscriptionPlan);
//            return user;
//        });
//        modelMapper.typeMap(User.class , UserDto.class).setConverter(context->{
//            UserDto userDto = context.getDestination();
//            User user = context.getSource();
//            String subscriptionPlan = user.getSubscriptionPlan().name();
//            userDto.setSubscriptionPlan(subscriptionPlan);
//            return userDto;
//        });
        return modelMapper;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}