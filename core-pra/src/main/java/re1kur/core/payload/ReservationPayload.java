package re1kur.core.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationPayload(
        @Positive(message = "Place number cannot be negative or zero.")
        @NotNull(message = "Place number must be defined.")
        Integer placeNumber,
        @NotNull(message = "Car ID must be defined.")
        UUID carId,
        @Future(message = "Start date time cannot be past time.")
        @NotNull(message = "Start date time must be defined.")
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime startAt,
        @Future(message = "End date time cannot be past time.")
        @NotNull(message = "End date time must be defined.")
        @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime endAt
) {
}
