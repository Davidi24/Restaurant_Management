package pos.userservice.DTO.Mapper;

import org.springframework.stereotype.Component;
import pos.userservice.DTO.UserRequest;
import pos.userservice.DTO.UserResponse;
import pos.userservice.Module.Role;
import pos.userservice.Module.User;

@Component
public class UserMapper {

    public User mapUser(UserRequest userRequest, Role role) {
        return User.builder()
                .name(userRequest.name())
                .email(userRequest.email())
                .password(userRequest.password())
                .role(role)
                .build();
    }

    public UserResponse fromUser(User createdUser) {
        return new UserResponse(
                createdUser.getName(),
                createdUser.getEmail()
        );
    }
}
