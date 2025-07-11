package re1kur.core.payload;

import jakarta.validation.constraints.Size;

public record GenerateCodeRequest(
        @Size(min = 10, max = 10)
        String phoneNumber
) {
}
