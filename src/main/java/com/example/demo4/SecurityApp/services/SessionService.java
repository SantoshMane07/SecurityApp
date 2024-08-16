package com.example.demo4.SecurityApp.services;

import com.example.demo4.SecurityApp.entities.Session;
import com.example.demo4.SecurityApp.entities.User;
import com.example.demo4.SecurityApp.entities.enums.SubscriptionPlan;
import com.example.demo4.SecurityApp.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    public void generateNewSession(User user, String refreshToken) {

        SubscriptionPlan subscriptionPlan = user.getSubscriptionPlan();
        int sessionLimit = 0;
        if(subscriptionPlan.equals(SubscriptionPlan.FREE)){
            sessionLimit = 1;
        } else if (subscriptionPlan.equals(SubscriptionPlan.BASIC)) {
            sessionLimit = 2;
        }else {
            sessionLimit = 3;
        }
        List<Session> userSessions = sessionRepository.findByUser(user);
        if (userSessions.size() == sessionLimit) {
            userSessions.sort(Comparator.comparing(Session::getLastUsedAt));

            Session leastRecentlyUsedSession = userSessions.getFirst();
            sessionRepository.delete(leastRecentlyUsedSession);
        }

        Session newSession = Session.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();
        sessionRepository.save(newSession);
    }

    public void validateSession(String refreshToken) {
        Session session = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new SessionAuthenticationException("Session not found for refreshToken: "+refreshToken));
        session.setLastUsedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }

    public void removeSession(String refreshToken) {
        Session session = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new SessionAuthenticationException("Session not found for refreshToken: "+refreshToken));
        sessionRepository.deleteById(session.getId());
    }
}
