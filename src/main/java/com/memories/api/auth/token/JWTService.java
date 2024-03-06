package com.memories.api.auth.token;

import com.memories.api.auth.dto.Credentials;
import com.memories.api.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
@ConditionalOnProperty(name = "memories.token-type", havingValue = "jwt")
public class JWTService implements TokenService{
    SecretKey key = Keys.hmacShaKeyFor("secret-must-be-at-least-32-chars".getBytes());
    @Override
    public Token createToken(User user, Credentials creds) {

        String token = Jwts.builder().subject(Long.toString(user.getId())).signWith(key).compact();
        return new Token("Bearer", token);
    }

    @Override
    public User verifyToken(String authorizationHeader) {
        if (authorizationHeader == null) {
            return null;
        }
        String token = authorizationHeader.split("Bearer ")[1];
        JwtParser parser = Jwts.parser().verifyWith(key).build();

        try {
            Jws<Claims> claimsJws = parser.parseSignedClaims(token);
            Claims payload = claimsJws.getPayload();
            Long userID = Long.valueOf(payload.getSubject());
            User user = new User();
            user.setId(userID);
            return user;
        } catch (JwtException e) {
            e.printStackTrace();
        }
        return null;
    }
}
