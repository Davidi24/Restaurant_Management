package pos.userservice.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import pos.userservice.Service.UserService;

@RestController
@RequestMapping("/waiter")
@Slf4j
public class WaiterController {

    private final UserService userService;

    public WaiterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            ServerWebExchange exchange
    ) {
        try {
            String message = userService.changePassword(exchange, newPassword, confirmPassword);
            return ResponseEntity.ok().body(message);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("Something went wrong");
        }
    }
}
