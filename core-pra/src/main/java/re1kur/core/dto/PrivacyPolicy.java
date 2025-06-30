package re1kur.core.dto;

import java.util.List;

public record PrivacyPolicy(
        String methodName,
        String methodType,
        List<String> roles
) {
}
