package pos.gatewayservice.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import pos.gatewayservice.Model.UserPrincipal;


import java.util.Objects;
import java.util.Optional;

@Component
public class CookiesManager {

    private final JWTService jwtService;

    @Autowired
    public CookiesManager(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    public void setCookies(UserPrincipal userPrincipal, ServerHttpResponse response) {

        String role = userPrincipal.getUser().getRole().name();

        String accessToken = jwtService.generateAccessToken(userPrincipal.getEmail(), role);
        String refreshToken = jwtService.generateRefreshToken(userPrincipal.getEmail(), role);

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(false)
                .maxAge(900)// 15 minutes
                .path("/")
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // Set to true in production
                .maxAge(24 * 60 * 60) // 1 day (24 hours)
                .path("/")
                .build();

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

    public Optional<String> getAccessTokenFromCookies(org.springframework.http.server.reactive.ServerHttpRequest request) {
        return getTokenFromCookies(request, "accessToken");
    }

    public Optional<String> getRefreshTokenFromCookies(org.springframework.http.server.reactive.ServerHttpRequest request) {
        return getTokenFromCookies(request, "refreshToken");
    }

    private Optional<String> getTokenFromCookies(ServerHttpRequest request, String tokenName) {
        // Extract cookies from the request and look for the token name
        if (request.getCookies().containsKey(tokenName)) {
            return Optional.of(Objects.requireNonNull(request.getCookies().getFirst(tokenName)).getValue());
        }
        return Optional.empty();
    }

    public void removeCookies(ServerHttpResponse response) {
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", null)
                .httpOnly(true)
                .secure(false)
                .maxAge(0)
                .path("/")
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", null)
                .httpOnly(true)
                .secure(false)
                .maxAge(0)
                .path("/")
                .build();

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

    public void updateAccessTokenCookie(String newAccessToken, ServerHttpResponse response) {
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", newAccessToken)
                .httpOnly(true)
                .secure(false) // Set to true in production
                .maxAge(15 * 60) // 15 minutes
                .path("/")
                .build();
        response.addCookie(accessTokenCookie);
    }

    public void updateRefreshTokenCookie(String newRefreshToken, ServerHttpResponse response) {
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(false)
                .maxAge(24 * 60 * 60)
                .path("/")
                .build();
        response.addCookie(refreshTokenCookie);
    }

}
