package re1kur.core.payload;

public record UserInformationPayload(
        String firstName,
        String lastName,
        String middleName
) {
}
