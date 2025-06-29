package re1kur.is.mapper;

import re1kur.core.event.CodeGeneratedEvent;
import re1kur.is.entity.User;

public interface EventMapper {
    CodeGeneratedEvent codeGenerated(User user, String codeValue);
}
