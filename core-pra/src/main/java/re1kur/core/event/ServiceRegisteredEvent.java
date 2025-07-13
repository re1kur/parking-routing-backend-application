package re1kur.core.event;

import re1kur.core.dto.PrivacyPolicy;

import java.util.List;

public record ServiceRegisteredEvent(
        String serviceName,
        String apiPrefix,
        List<PrivacyPolicy> policies
) {
}
