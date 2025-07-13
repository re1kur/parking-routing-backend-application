package re1kur.core.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CarImagePayload(
        @NotBlank(message = "File id cannot be empty or contain backspace symbols.")
        @Size(max = 128, message = "File id have to be 128 characters maximum long.")
        String fileId
) {
}
