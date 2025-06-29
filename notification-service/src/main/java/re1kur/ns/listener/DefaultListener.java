package re1kur.ns.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import re1kur.core.event.CodeGeneratedEvent;
import re1kur.ns.service.NotificationService;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultListener {
    private final NotificationService service;

    @RabbitListener(queues = "${mq.listen.code-generated-queue}")
    public void listenVerificationCodeGeneration(CodeGeneratedEvent event) {
        log.info("Listening verification code generation: {}", event);
        service.sendVerificationCodeNotification(event);
    }
}
