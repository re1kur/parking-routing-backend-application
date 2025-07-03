package re1kur.core.exception;

public class ParkingPlaceAlreadyExistsException extends RuntimeException {
    public ParkingPlaceAlreadyExistsException(String message) {
        super(message);
    }
}
