package pos.gatewayservice.Service;

import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import pos.gatewayservice.DTO.UserResponse;
import pos.gatewayservice.Model.User;
import pos.gatewayservice.Model.UserPrincipal;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

    private final UserDetailsRepositoryReactiveAuthenticationManager authenticationManager;
    private final CookiesManager cookiesManager;

    public AuthService(UserDetailsRepositoryReactiveAuthenticationManager authenticationManager, UserDetailsRepositoryReactiveAuthenticationManager authenticationManager1, CookiesManager cookiesManager) {

        this.authenticationManager = authenticationManager1;
        this.cookiesManager = cookiesManager;
    }

    public Mono<UserResponse> login(User user, ServerHttpResponse response) {
        return authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()))
                .map(auth -> {
                    UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
                    cookiesManager.setCookies(userPrincipal, response);
                    return new UserResponse(userPrincipal.getName(), userPrincipal.getEmail());
                })
                .onErrorResume(e -> Mono.error(new RuntimeException("Login failed: " + e.getMessage())));
    }


    public Mono<String> logout(ServerHttpResponse response) {
        try {
            cookiesManager.removeCookies(response);
            return Mono.just("User logged out successfully");
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Something went wrong, User did not logout!!!", e));
        }
    }

}
