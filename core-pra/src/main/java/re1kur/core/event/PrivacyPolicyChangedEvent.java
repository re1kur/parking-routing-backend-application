package re1kur.core.event;

import re1kur.core.dto.PrivacyPolicy;

public record PrivacyPolicyChangedEvent(
        String serviceName,
        PrivacyPolicy policy
) {
}
