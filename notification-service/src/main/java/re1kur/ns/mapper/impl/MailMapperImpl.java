package re1kur.ns.mapper.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import re1kur.core.event.CodeGeneratedEvent;
import re1kur.ns.mapper.MailMapper;

@Component
public class MailMapperImpl implements MailMapper {
    @Value("${mail.verification-code.text}")
    private String mailVerificationCodeText;

    @Override
    public SimpleMailMessage code(CodeGeneratedEvent event, String from) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(event.email());
        message.setText(mailVerificationCodeText.formatted(event.code()));
        return message;
    }
}
