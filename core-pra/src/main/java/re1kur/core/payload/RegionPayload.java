package re1kur.core.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RegionPayload(
        @NotBlank(message = "Region name cannot be empty or contain backspace symbols.")
        @Size(max = 64, message = "Region must be between 6 and 64 characters long.")
        String name,

        List<RegionCodePayload> codes
) {
}
