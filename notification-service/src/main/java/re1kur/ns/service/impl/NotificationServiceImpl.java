package re1kur.ns.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import re1kur.core.dto.SmsMessage;
import re1kur.core.event.CodeGeneratedEvent;
import re1kur.ns.mapper.MailMapper;
import re1kur.ns.mapper.SmsMapper;
import re1kur.ns.service.NotificationService;
import re1kur.ns.service.SmsSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final MailSender mailSender;
    private final MailMapper mailMapper;
    private final SmsSender smsSender;
    private final SmsMapper smsMapper;


    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendVerificationCodeNotification(CodeGeneratedEvent event) {
        try {
            SimpleMailMessage mail = mailMapper.code(event, from);
            mailSender.send(mail);
            log.info("Send mail of log in code notification to {}", event.getEmail());
        } catch (MailException e) {
            log.error("Error while sending mail log in code: {}", e.getMessage());
        }
        SmsMessage sms = smsMapper.code(event);
        smsSender.send(sms);
    }
}
