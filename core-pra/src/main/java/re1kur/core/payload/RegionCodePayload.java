package re1kur.core.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegionCodePayload(
    @Pattern(regexp = "\\d{2,3}", message = "Region code must be 2 or 3 digits.")
    @NotBlank(message = "Region code cannot be empty or contain backspaces.")
    String codeValue
) {
}
