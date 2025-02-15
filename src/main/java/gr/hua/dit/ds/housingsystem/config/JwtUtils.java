package gr.hua.dit.ds.housingsystem.config;

import gr.hua.dit.ds.housingsystem.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Base64;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${housing.app.jwtSecret}")
    private String jwtSecret;

    @Value("${housing.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private byte[] decodedSecret;

    @PostConstruct
    public void init() {
        //JASON CHANGES
        try {
            this.decodedSecret = Base64.getUrlDecoder().decode(jwtSecret);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Base64 key for JWT secret", e);
        }
//        this.decodedSecret = jwtSecret.getBytes();
        //JASON CHANGES

    }

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(String.valueOf(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, decodedSecret)
                .compact();
    }


    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(decodedSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(decodedSecret).parseClaimsJws(authToken);
            return true;
        } catch (JwtException e) {
            logger.error("Invalid JWT: {}", e.getMessage());
        }
        return false;
    }

    public Long getUserIdFromJwtToken(String token) {
        try {
            return Long.parseLong(Jwts.parser()
                    .setSigningKey(decodedSecret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject());
        } catch (NumberFormatException e) {
            return null;
        }
    }


}