package re1kur.core.payload;

public record GenerateCodeRequest(
        String phoneNumber,
        String email
) {
}
