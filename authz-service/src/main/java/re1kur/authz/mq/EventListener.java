package re1kur.authz.mq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import re1kur.authz.service.AuthzService;
import re1kur.core.event.ServiceRegisteredEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventListener {
    private final AuthzService service;

    @RabbitListener(queues = "${mq.listen.service-registered-queue}")
    private void listenServiceRegisteredEvent(ServiceRegisteredEvent event) {
        log.info("Listening service registered event: {}", event.toString());
        service.registerPrivacyPolicy(event);
        log.info("Service policy privacy registered.");
    }

}
