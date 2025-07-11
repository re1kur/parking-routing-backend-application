package re1kur.core.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record MakePayload(
        @NotBlank(message = "Name cannot be empty or contain backspaces.")
        String name
) {
}
