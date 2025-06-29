package re1kur.ns.mapper.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import re1kur.core.event.CodeGeneratedEvent;
import re1kur.ns.mapper.MailMapper;

@Component
public class MailMapperImpl implements MailMapper {
    @Value("${mail.login-code.text}")
    private String mailLoginCodeText;

    @Value("${mail.login-code.theme}")
    private String mailLoginCodeTheme;

    @Override
    public SimpleMailMessage code(CodeGeneratedEvent event, String from) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(mailLoginCodeTheme);
        message.setFrom(from);
        message.setTo(event.getEmail());
        message.setText(mailLoginCodeText.formatted(event.getCode()));
        return message;
    }
}
