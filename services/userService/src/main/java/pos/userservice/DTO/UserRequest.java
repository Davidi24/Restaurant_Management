package pos.userservice.DTO;

public record UserRequest(
        String name,
        String email,
         String password,
        String newEmail
) {
}
