package re1kur.core.dto;



import lombok.Builder;

@Builder
public record Credentials (
        String sub,
        String phone,
        String email,
        String scope
) {
}
