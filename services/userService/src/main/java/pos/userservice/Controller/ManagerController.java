package pos.userservice.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import pos.userservice.DTO.UserRequest;
import pos.userservice.Module.Role;
import pos.userservice.Service.UserService;


@Slf4j
@RestController
@RequestMapping("/manager")
public class ManagerController {
    private final UserService userService;
    public ManagerController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("allWaiters")
    public ResponseEntity<?> getAllWaiters() {
        return ControllerHelper.getResponseGetAll(userService, Role.WAITER);
    }

    @GetMapping("waiter/{email}")
    public ResponseEntity<?> getWaiter(@PathVariable String email) {
        return ControllerHelper.getResponseByEmail(email, userService, Role.WAITER);
    }

    @PostMapping("/waiter")
    public ResponseEntity<?> createWaiter(@RequestBody UserRequest userRequest) {
        return ControllerHelper.getResponseEntityCreate(userRequest, userService, Role.WAITER);
    }

    @PutMapping("/waiter/{email}")
    public ResponseEntity<?> updateWaiter(@PathVariable String email, @RequestBody UserRequest userRequest) {
        UserRequest updatedRequest = new UserRequest(userRequest.name(), email, userRequest.password(), userRequest.newEmail());
        return ControllerHelper.getResponseUpdate(updatedRequest, userService, Role.WAITER);
    }

    @DeleteMapping("/waiter/{email}")
    public ResponseEntity<?> deleteWaiter(@PathVariable String email) {
        return ControllerHelper.getResponseDelete(email, userService, Role.WAITER);
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
