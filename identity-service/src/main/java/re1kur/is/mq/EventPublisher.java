package re1kur.is.mq;

import re1kur.is.entity.User;

public interface EventPublisher {
    void verificationCode(User user, String codeValue);
}
