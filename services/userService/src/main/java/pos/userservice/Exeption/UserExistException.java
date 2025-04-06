package pos.userservice.Exeption;


public class UserExistException extends RuntimeException {
    public UserExistException(String message) {
        super(message);
    }
}
