package com.example.demo4.SecurityApp.services;

import com.example.demo4.SecurityApp.entities.User;
import com.example.demo4.SecurityApp.entities.enums.SubscriptionPlan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class SubscriptionService {

    public boolean hasAccessBasedOnPlan(User user, String requiredPlan) {
        SubscriptionPlan userPlan = user.getSubscriptionPlan();
        return userPlan.name().equals(requiredPlan) || userPlan.ordinal() >= SubscriptionPlan.valueOf(requiredPlan).ordinal();
    }
}

