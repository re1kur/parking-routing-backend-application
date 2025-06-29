package re1kur.ns.service;

import re1kur.core.event.CodeGeneratedEvent;

public interface NotificationService {
    void sendVerificationCodeNotification(CodeGeneratedEvent event);
}
