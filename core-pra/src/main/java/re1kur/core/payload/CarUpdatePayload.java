package re1kur.core.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CarUpdatePayload(
        @NotNull(message = "Car id must be.")
        UUID carId,
        UUID ownerId,
        @Size(min = 5, max = 10, message = "License plate must be between 5 and 10 characters long.")
        @NotBlank(message = "License plate cannot be empty or contain backspaces.")
        String licensePlate,
        @Pattern(regexp = "\\d{2,3}", message = "Region code must be 2 or 3 digits.")
        @NotBlank(message = "Region code cannot be empty or contain backspaces.")
        String regionCode,
        @NotBlank(message = "Color cannot be empty or contain backspaces.")
        String color,
        @NotBlank(message = "Model cannot be empty or contain backspaces.")
        String model,
        @NotNull(message = "Make must be.")
        Integer makeId
) {
}
