package pos.gatewayservice.Config;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import pos.gatewayservice.Model.UserPrincipal;
import pos.gatewayservice.Service.CookiesManager;
import pos.gatewayservice.Service.JWTService;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public class JWTFilter implements WebFilter {

    private final ReactiveUserDetailsService userDetailsService;
    private final JWTService jwtService;
    private final CookiesManager cookiesManager;
    private static final PathPatternParser patternParser = new PathPatternParser();


    private final List<String> publicURI;

    public JWTFilter(ReactiveUserDetailsService userDetailsService,
                     JWTService jwtService,
                     CookiesManager cookiesManager) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.cookiesManager = cookiesManager;
        this.publicURI = SecurityConfig.PUBLIC_URIS;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        for (String publicUri : SecurityConfig.PUBLIC_URIS) {
            PathPattern pattern = patternParser.parse(publicUri);
            if (pattern.matches(exchange.getRequest().getPath())) {
                System.out.println("here");
                return chain.filter(exchange);
            }
        }
        Optional<String> accessTokenOpt = cookiesManager.getAccessTokenFromCookies(exchange.getRequest());
        Optional<String> refreshTokenOpt = cookiesManager.getRefreshTokenFromCookies(exchange.getRequest());

        if (accessTokenOpt.isPresent()) {
            String accessToken = accessTokenOpt.get();
            try {
                String email = jwtService.extractUserEmail(accessToken, jwtService.getAccessKey());

                return userDetailsService.findByUsername(email)
                        .switchIfEmpty(Mono.error(new RuntimeException("USER_NOT_FOUND")))
                        .flatMap(userDetails -> {
                            if (jwtService.validateToken(accessToken, userDetails.getUsername(), jwtService.getAccessKey())) {
                                return proceedWithValidToken(exchange, chain, userDetails);
                            } else if (refreshTokenOpt.isPresent()) {
                                return handleTokenRefresh(exchange, chain, refreshTokenOpt.get(), userDetails);
                            } else {
                                return unauthorizedResponse(exchange, "Invalid access token and no refresh token");
                            }
                        })
                        .onErrorResume(ex -> unauthorizedResponse(exchange, ex.getMessage()));
            } catch (ExpiredJwtException e) {
                return tryRefresh(refreshTokenOpt, exchange, chain);

            } catch (Exception e) {
                return unauthorizedResponse(exchange, "Invalid token: " + e.getMessage());
            }
        }
        return tryRefresh(refreshTokenOpt, exchange, chain);
    }

    private Mono<Void> tryRefresh(Optional<String> refreshTokenOpt, ServerWebExchange exchange, WebFilterChain chain) {
        if (refreshTokenOpt.isPresent()) {
            String refreshToken = refreshTokenOpt.get();

            try {
                String email = jwtService.extractUserEmail(refreshToken, jwtService.getRefreshKey());

                return userDetailsService.findByUsername(email)
                        .switchIfEmpty(Mono.error(new RuntimeException("USER_NOT_FOUND")))
                        .flatMap(userDetails -> handleTokenRefresh(exchange, chain, refreshToken, userDetails))
                        .onErrorResume(ex -> unauthorizedResponse(exchange, ex.getMessage()));
            } catch (Exception ex) {
                return unauthorizedResponse(exchange, "Invalid refresh token");
            }
        }
        return unauthorizedResponse(exchange, "No token in the cookies");
    }

    private Mono<Void> handleTokenRefresh(ServerWebExchange exchange,
                                          WebFilterChain chain,
                                          String refreshToken,
                                          UserDetails userDetails) {
        try {
            if (!jwtService.validateToken(refreshToken, userDetails.getUsername(), jwtService.getRefreshKey())) {
                return unauthorizedResponse(exchange, "Session expired - please login again");
            }

            String email = userDetails.getUsername();
            String role = ((UserPrincipal) userDetails).getUser().getRole().name();
            String newAccessToken = jwtService.generateAccessToken(email, role);

            cookiesManager.updateAccessTokenCookie(newAccessToken, exchange.getResponse());

            return proceedWithValidToken(exchange, chain, userDetails);
        } catch (Exception e) {
            return unauthorizedResponse(exchange, "Token refresh failed");
        }
    }

    private Mono<Void> proceedWithValidToken(ServerWebExchange exchange,
                                             WebFilterChain chain,
                                             UserDetails userDetails) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        System.out.println("UserRole: " + userDetails.getAuthorities());
        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
