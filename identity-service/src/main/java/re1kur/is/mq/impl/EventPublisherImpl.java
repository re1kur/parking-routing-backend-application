package re1kur.is.mq.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import re1kur.core.event.CodeGeneratedEvent;
import re1kur.is.entity.User;
import re1kur.is.mapper.EventMapper;
import re1kur.is.mq.EventPublisher;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublisherImpl implements EventPublisher {
    private final RabbitTemplate template;
    private final EventMapper mapper;

    @Value("${mq.exchange}")
    private String exchange;

    @Value("${mq.routing-key.verification-code}")
    private String codeGeneratedRoutKey;

    @Override
    public void verificationCode(User user, String codeValue) {
        log.info("Generating verification code event for user: {}", user.getId());
        CodeGeneratedEvent event = mapper.codeGenerated(user, codeValue);
        template.convertAndSend(exchange, codeGeneratedRoutKey, event);
        log.info("Sent verification code event: {}", event);
    }
}
