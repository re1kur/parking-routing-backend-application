package re1kur.is.mq;

import re1kur.is.entity.User;

import java.io.IOException;

public interface EventPublisher {
    void verificationCode(User user, String codeValue);

    void registrationServicePrivacyPolicy() throws IOException;
}
