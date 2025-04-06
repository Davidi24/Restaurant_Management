package pos.userservice.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import pos.userservice.DTO.Mapper.UserMapper;
import pos.userservice.DTO.UserRequest;
import pos.userservice.DTO.UserResponse;
import pos.userservice.Exeption.IllegalRoleExeption;
import pos.userservice.Exeption.PasswordNotMatchExeption;
import pos.userservice.Exeption.UserExistException;
import pos.userservice.Exeption.UserNotFoundException;
import pos.userservice.Module.Role;
import pos.userservice.Module.User;
import pos.userservice.Repository.UserRepository;
import pos.userservice.Security.TokenManger;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenManger tokenManger;

    public UserService(UserMapper userMapper, UserRepository userRepository, PasswordEncoder passwordEncoder, TokenManger tokenManger) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenManger = tokenManger;
    }

    public UserResponse createWaiter(UserRequest request) {
        return getUserResponse(request, Role.WAITER);
    }

    public UserResponse updateWaiter(UserRequest userRequest) {
        return updateUser(userRequest, Role.WAITER);
    }

    public boolean deleteWaiter(String email) {
        return deleteUser(email, Role.WAITER);
    }

    public List<UserResponse> getAllWaiters() {
        return getAllUsers(Role.WAITER);
    }

    public UserResponse getWaiterByEmail(String email) {
        return getUserByEmail(email, Role.WAITER);
    }

    public UserResponse createManager(UserRequest request) {
        return getUserResponse(request, Role.MANAGER);
    }

    public UserResponse updateManager(UserRequest userRequest) {
        return updateUser(userRequest, Role.MANAGER);
    }

    public boolean deleteManager(String email) {
        return deleteUser(email, Role.MANAGER);
    }

    public List<UserResponse> getAllManagers() {
        return getAllUsers(Role.MANAGER);
    }

    public UserResponse getManagerByEmail(String email) {
        return getUserByEmail(email, Role.MANAGER);
    }

    public UserResponse createAdmin(UserRequest request) {
        return getUserResponse(request, Role.ADMIN);
    }

    public UserResponse updateAdmin(UserRequest userRequest) {
        return updateUser(userRequest, Role.ADMIN);
    }

    public boolean deleteAdmin(String email) {
        return deleteUser(email, Role.ADMIN);
    }

    public List<UserResponse> getAllAdmins() {
        return getAllUsers(Role.ADMIN);
    }

    public UserResponse getAdminByEmail(String email) {
        return getUserByEmail(email, Role.ADMIN);
    }

    private UserResponse getUserResponse(UserRequest request, Role role) {
        User existingUser = userRepository.findByEmail(request.email());
        if (existingUser != null) {
            throw new UserExistException("Email already exists");
        }
        User user = userMapper.mapUser(request, role);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        User createdUser = userRepository.save(user);
        return userMapper.fromUser(createdUser);
    }

    private UserResponse updateUser(UserRequest userRequest, Role role) {
        User existingUser = userRepository.findByEmail(userRequest.email());
        if (existingUser == null) {
            throw new UserNotFoundException("User not found");
        }

        if (!existingUser.getRole().equals(role)) {
            throw new IllegalRoleExeption("User is not a " + role.name().toLowerCase());
        }

        if (userRequest.name() != null && !userRequest.name().isBlank()) {
            existingUser.setName(userRequest.name());
        }

        if (userRequest.password() != null && !userRequest.password().isBlank()) {
            String encodedPassword = passwordEncoder.encode(userRequest.password());
            existingUser.setPassword(encodedPassword);
        }

        if (userRequest.newEmail() != null && !userRequest.newEmail().isBlank()) {
            if (userRequest.newEmail().equalsIgnoreCase(existingUser.getEmail())) {
                throw new UserExistException("Email is same as previous");
            }
            if (userRepository.findByEmail(userRequest.newEmail()) != null) {
                throw new UserExistException("New email is already in use");
            }
            existingUser.setEmail(userRequest.newEmail());
        }

        User updatedUser = userRepository.save(existingUser);
        return userMapper.fromUser(updatedUser);
    }

    private boolean deleteUser(String email, Role role) {
        User existingUser = userRepository.findByEmail(email);
        if (existingUser == null) {
            throw new UserNotFoundException("User not found");
        }

        if (!existingUser.getRole().equals(role)) {
            throw new IllegalRoleExeption("User is not a " + role.name().toLowerCase());
        }

        userRepository.delete(existingUser);
        return true;
    }

    private List<UserResponse> getAllUsers(Role role) {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() == role)
                .map(userMapper::fromUser)
                .toList();
    }

    private UserResponse getUserByEmail(String email, Role role) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        if (!user.getRole().equals(role)) {
            throw new IllegalRoleExeption("User is not a " + role.name().toLowerCase());
        }
        return userMapper.fromUser(user);
    }

    public String changePassword(ServerWebExchange exchange, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new PasswordNotMatchExeption("The confirm password does not match the new password.");
        }
        String existingEmail = tokenManger.extractEmailFromRefreshToken(exchange)
                .orElseThrow(() -> new UserNotFoundException("No user is logged in"));

        User existingUser = userRepository.findByEmail(existingEmail);
        if (existingUser == null) {
            throw new UserNotFoundException("User not found");
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        existingUser.setPassword(encodedPassword);

        userRepository.save(existingUser);

        log.info("Password changed successfully for user: {}", existingEmail);

        return "Password changed successfully";
    }
}
