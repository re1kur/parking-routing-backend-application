package re1kur.ns.mapper;

import re1kur.core.dto.SmsMessage;
import re1kur.core.event.CodeGeneratedEvent;

public interface SmsMapper {
    SmsMessage code(CodeGeneratedEvent event);
}
