package pos.gatewayservice.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import pos.gatewayservice.DTO.UserResponse;
import pos.gatewayservice.Model.User;
import pos.gatewayservice.Service.AuthService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    public ResponseEntity<Mono<UserResponse>> login(@RequestBody User user, ServerHttpResponse response) {
       Mono<UserResponse> userResponse = authService.login(user,  response);
       return ResponseEntity.ok().body(userResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Mono<String>> logout(ServerHttpResponse response) {
        Mono<String> message = authService.logout(response);
        return ResponseEntity.ok().body(message);
    }
}
