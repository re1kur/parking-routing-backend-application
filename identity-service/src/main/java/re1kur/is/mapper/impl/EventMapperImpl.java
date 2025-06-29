package re1kur.is.mapper.impl;

import org.springframework.stereotype.Component;
import re1kur.core.event.CodeGeneratedEvent;
import re1kur.is.entity.User;
import re1kur.is.mapper.EventMapper;

@Component
public class EventMapperImpl implements EventMapper {
    @Override
    public CodeGeneratedEvent codeGenerated(User user, String codeValue) {
        return CodeGeneratedEvent.builder()
                .phone(user.getPhoneNumber())
                .email(user.getEmail())
                .code(codeValue)
                .build();
    }
}
