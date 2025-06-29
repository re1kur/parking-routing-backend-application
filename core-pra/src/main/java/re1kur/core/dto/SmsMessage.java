package re1kur.core.dto;

import lombok.Builder;

@Builder
public record SmsMessage(
        String to,
        String msg
) {
}
