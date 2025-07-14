package re1kur.core.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RegionPayload(
        // TODO: valid messages
        @NotBlank(message = "Region name cannot be empty or contain backspace symbols.")
        @Size(max = 64)
        String name,

        List<RegionCodePayload> codes
) {
}
