package re1kur.core.event;

import lombok.Builder;

@Builder
public record CodeGeneratedEvent(
        String email,
        String phone,
        String code
) {
}
