package re1kur.core.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserPayload(
        @Email(message = "The email is invalid.")
        String email,
        @Size(min = 10, max = 10, message = "The phone number must be 10 digits long.")
        @NotBlank(message = "The phone number can not be empty or contain backspaces.")
        String phoneNumber,
        @NotBlank(message = "The first name can be empty or contain backspace.")
        @Size(min = 3, max = 64, message = "The firstname must be between 3 and 64 characters long.")
        String firstName,
        @NotBlank(message = "The last name can not be empty or contain backspaces.")
        @Size(max = 64, message = "The last name must be between 3 and 64 characters long.")
        String lastName,
        @Size(min = 3, max = 64, message = "The middle name must be between 3 and 64 characters long.")
        @Null
        String middleName) {
}
