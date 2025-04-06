package pos.userservice.Exeption;

public class PasswordNotMatchExeption extends RuntimeException {
    public PasswordNotMatchExeption(String mess) {
        super(mess);
    }
}
