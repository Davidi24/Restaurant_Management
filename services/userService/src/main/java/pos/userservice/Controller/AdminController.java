package pos.userservice.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import pos.userservice.DTO.UserRequest;
import pos.userservice.DTO.UserResponse;
import pos.userservice.Module.Role;
import pos.userservice.Service.UserService;

@RestController
@RequestMapping("admin")
@Slf4j
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("allManagers")
    public ResponseEntity<?> getAllManagers() {
        return ControllerHelper.getResponseGetAll(userService, Role.MANAGER);
    }

    @GetMapping("manager/{email}")
    public ResponseEntity<?> getManager(@PathVariable String email) {
        return ControllerHelper.getResponseByEmail(email, userService, Role.WAITER);
    }

    @PostMapping("/manager")
    public ResponseEntity<?> createManager(@RequestBody UserRequest userRequest) {
        return ControllerHelper.getResponseEntityCreate(userRequest, userService, Role.WAITER);
    }

    @PutMapping("/manager/{email}")
    public ResponseEntity<?> updateManager(@PathVariable String email, @RequestBody UserRequest userRequest) {
        UserRequest updatedRequest = new UserRequest(userRequest.name(), email, userRequest.password(), userRequest.newEmail());
        return ControllerHelper.getResponseUpdate(updatedRequest, userService, Role.WAITER);
    }

    @DeleteMapping("/manager/{email}")
    public ResponseEntity<?> deleteManger(@PathVariable String email) {
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
