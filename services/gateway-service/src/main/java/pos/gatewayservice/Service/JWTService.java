package pos.gatewayservice.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.stereotype.Service;
import pos.gatewayservice.Exeptions.UnauthorizedException;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Service
public class JWTService {

    private final SecretKey accessKey;
    private final SecretKey refreshKey;

    public JWTService() {
        // to do -- Add them in .env file
        String secretKey = "pLLHd6tWZC8AVEmUxOq9VXzqtnY7tkZ/8OK9b5J6QEQ=";
        String refreshSecretKey = "pLLHd6tWZC8AVEmUxOq9VXzqtnY7tkZ/8OK9b5J6QEQ=";

        // Decode and cache keys
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecretKey));
    }

    public String generateAccessToken(String email, String role) {
        return generateToken(email, role, accessKey, "access", 15 * 60 * 1000); // 15 minutes
    }

    public String generateRefreshToken(String email, String role) {
        return generateToken(email, role, refreshKey, "refresh", 30L * 24 * 60 * 60 * 1000); // 30 days
    }

    private String generateToken(String email, String role, SecretKey key, String tokenType, long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("token_type", tokenType);
        claims.put("unique_id", UUID.randomUUID().toString());
        claims.put("role", role);

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }


    public boolean validateToken(String token, String email, SecretKey key) {
        Claims claims = getClaimsFromToken(token, key);
        final String userName = claims.getSubject();
        return (userName.equals(email) && !claims.getExpiration().before(new Date()));
    }


    public String extractUserEmail(String token, SecretKey key) {
        try {
            return getClaimsFromToken(token, key).getSubject();
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (JwtException e) {
            throw new UnauthorizedException("Invalid token: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new UnauthorizedException("Token unidentifiable", e);
        }
    }


    private Claims getClaimsFromToken(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenExpired(String token, SecretKey key) {
        Date expirationDate = getClaimsFromToken(token, key).getExpiration();
        return expirationDate.before(new Date());
    }

    public String extractUserRole(String token, SecretKey key) {
        return getClaimsFromToken(token, key).get("role", String.class);
    }

}
