package pos.userservice.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import javax.crypto.SecretKey;
import java.util.Objects;
import java.util.Optional;

@Service
public class TokenManger {

    // to do - take the secret key from .env file
    private  final String SECRET_KEY = "pLLHd6tWZC8AVEmUxOq9VXzqtnY7tkZ/8OK9b5J6QEQ=";

    public Optional<String> extractEmailFromRefreshToken(ServerWebExchange exchange) {
        try {
            return getRefreshTokenFromCookies(exchange.getRequest())
                    .map(token -> {
                        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
                        Claims claims = Jwts.parser()
                                .verifyWith(key)
                                .build()
                                .parseSignedClaims(token)
                                .getPayload();
                        return claims.getSubject();
                    });
        } catch (ExpiredJwtException e) {
            System.err.println("Token expired: " + e.getMessage());
        } catch (io.jsonwebtoken.security.SignatureException e) {
            System.err.println("Invalid token signature: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error parsing token: " + e.getMessage());
        }

        return Optional.empty();
    }

     private Optional<String> getRefreshTokenFromCookies(ServerHttpRequest request) {
        return getTokenFromCookies(request, "refreshToken");
     }

    private Optional<String> getTokenFromCookies(ServerHttpRequest request, String tokenName) {
        if (request.getCookies().containsKey(tokenName)) {
            return Optional.of(Objects.requireNonNull(request.getCookies().getFirst(tokenName)).getValue());
        }
        return Optional.empty();
    }
}
