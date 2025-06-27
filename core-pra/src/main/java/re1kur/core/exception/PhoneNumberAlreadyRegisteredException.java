package re1kur.core.exception;

public class PhoneNumberAlreadyRegisteredException extends RuntimeException {
    public PhoneNumberAlreadyRegisteredException(String message) {
        super(message);
    }
}
