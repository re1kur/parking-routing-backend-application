package re1kur.ns.mapper;

import org.springframework.mail.SimpleMailMessage;
import re1kur.core.event.CodeGeneratedEvent;

public interface MailMapper {
    SimpleMailMessage code(CodeGeneratedEvent event, String from);
}
