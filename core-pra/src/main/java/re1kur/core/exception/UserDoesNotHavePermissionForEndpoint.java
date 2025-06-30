package re1kur.core.exception;

public class UserDoesNotHavePermissionForEndpoint extends RuntimeException {
    public UserDoesNotHavePermissionForEndpoint(String message) {
        super(message);
    }
}
