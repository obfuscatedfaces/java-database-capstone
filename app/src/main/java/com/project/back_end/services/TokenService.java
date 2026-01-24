package com.project.back_end.services;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {

    /**
     * Internal class to hold session details.
     */
    private static class Session {
        String email;
        String role;

        Session(String email, String role) {
            this.email = email;
            this.role = role;
        }
    }

    private final Map<String, Session> tokenStorage = new ConcurrentHashMap<>();

    public String generateToken(String email, String role) {
        String token = UUID.randomUUID().toString();
        tokenStorage.put(token, new Session(email, role));
        return token;
    }

    public Map<String, String> validateToken(String token, String requiredRole) {
        Map<String, String> errors = new HashMap<>();

        if (token == null || !tokenStorage.containsKey(token)) {
            errors.put("auth", "Invalid or expired session token.");
            return errors;
        }

        Session session = tokenStorage.get(token);
        if (!session.role.equalsIgnoreCase(requiredRole)) {
            errors.put("role", "Unauthorized access: Role mismatch.");
            return errors;
        }

        return errors;
    }


    public String getEmailFromToken(String token) {
        if (token != null && tokenStorage.containsKey(token)) {
            return tokenStorage.get(token).email;
        }
        return null;
    }

    public void invalidateToken(String token) {
        tokenStorage.remove(token);
    }
}