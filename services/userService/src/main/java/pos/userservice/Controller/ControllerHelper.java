package pos.userservice.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pos.userservice.DTO.UserRequest;
import pos.userservice.DTO.UserResponse;
import pos.userservice.Exeption.IllegalRoleExeption;
import pos.userservice.Exeption.UserExistException;
import pos.userservice.Exeption.UserNotFoundException;
import pos.userservice.Service.UserService;
import pos.userservice.Module.Role;

import java.util.List;

@Slf4j
public class ControllerHelper {

    static ResponseEntity<?> getResponseEntityCreate(UserRequest userRequest, UserService userService, Role role) {
        try {
            UserResponse userResponse = switch (role) {
                case MANAGER -> userService.createManager(userRequest);
                case ADMIN -> userService.createAdmin(userRequest);
                case WAITER -> userService.createWaiter(userRequest);
                default -> throw new IllegalRoleExeption("This role does not exist");
            };
            return userResponse != null ? ResponseEntity.ok().body(userResponse) : ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error while creating {}", role.name().toLowerCase(), e);
            return ResponseEntity.internalServerError().body("Something went wrong while creating " + role.name().toLowerCase());
        }
    }

    public static ResponseEntity<?> getResponseUpdate(UserRequest userRequest, UserService userService, Role role) {
        try {
            UserResponse userResponse = switch (role) {
                case MANAGER -> userService.updateManager(userRequest);
                case ADMIN -> userService.updateAdmin(userRequest);
                case WAITER -> userService.updateWaiter(userRequest);
                default -> throw new IllegalRoleExeption("This role does not exist");
            };
            return userResponse != null ? ResponseEntity.ok().body(userResponse) : ResponseEntity.badRequest().body("Invalid request or " + role.name().toLowerCase() + " not found");
        } catch (Exception e) {
            return handleExceptions(e, role.name().toLowerCase() + " update");
        }
    }

    public static ResponseEntity<?> getResponseDelete(String email, UserService userService, Role role) {
        try {
            boolean isDeleted = switch (role) {
                case MANAGER -> userService.deleteManager(email);
                case ADMIN -> userService.deleteAdmin(email);
                case WAITER ->  userService.deleteWaiter(email);
                default -> throw new IllegalRoleExeption("This role does not exist");

            };
            return isDeleted ? ResponseEntity.ok().body(role.name().toLowerCase() + " deleted successfully") : ResponseEntity.badRequest().body("Invalid request or " + role.name().toLowerCase() + " not found");
        } catch (Exception e) {
            return handleExceptions(e, role.name().toLowerCase() + " deletion");
        }
    }

    public static ResponseEntity<?> getResponseGetAll(UserService userService, Role role) {
        try {
            List<UserResponse> users = switch (role) {
                case MANAGER -> userService.getAllManagers();
                case ADMIN -> userService.getAllAdmins();
                case WAITER -> userService.getAllWaiters();
                default -> throw new IllegalRoleExeption("This role does not exist");
            };
            return (users == null || users.isEmpty()) ? ResponseEntity.ok().body("No " + role.name().toLowerCase() + "s found") : ResponseEntity.ok().body(users);
        } catch (Exception e) {
            log.error("Failed to get the {}s: ", role.name().toLowerCase(), e);
            return ResponseEntity.internalServerError().body("Something went wrong while fetching " + role.name().toLowerCase() + "s");
        }
    }

    public static ResponseEntity<?> getResponseByEmail(String email, UserService userService, Role role) {
        try {
            UserResponse user = switch (role) {
                case MANAGER -> userService.getManagerByEmail(email);
                case ADMIN -> userService.getAdminByEmail(email);
                case WAITER -> userService.getWaiterByEmail(email);
                default -> throw new IllegalRoleExeption("This role does not exist");
            };
            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
            return handleExceptions(e, "fetching " + role.name().toLowerCase() + " with email " + email);
        }
    }

    private static ResponseEntity<?> handleExceptions(Exception e, String action) {
        switch (e) {
            case IllegalRoleExeption illegalRoleExeption -> {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
            case UserNotFoundException userNotFoundException -> {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            case UserExistException userExistException -> {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }
            case null, default -> {
                log.error("Failed to {}", action, e);
                return ResponseEntity.internalServerError().body("Something went wrong during " + action);
            }
        }
    }
}
