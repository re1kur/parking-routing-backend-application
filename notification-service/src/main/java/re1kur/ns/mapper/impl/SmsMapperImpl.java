package re1kur.ns.mapper.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import re1kur.core.dto.SmsMessage;
import re1kur.core.event.CodeGeneratedEvent;
import re1kur.ns.mapper.SmsMapper;

@Slf4j
@Component
public class SmsMapperImpl implements SmsMapper {
    @Value("${sms-sender.code-text}")
    private String codeText;

    @Override
    public SmsMessage code(CodeGeneratedEvent event) {
        return SmsMessage.builder()
                .to(event.getPhone())
                .msg(codeText.formatted(event.getCode()))
                .build();
    }
}
