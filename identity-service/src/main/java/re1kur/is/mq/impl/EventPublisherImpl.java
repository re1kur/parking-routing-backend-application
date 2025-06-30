package re1kur.is.mq.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import re1kur.core.event.CodeGeneratedEvent;
import re1kur.core.event.ServiceRegisteredEvent;
import re1kur.is.entity.User;
import re1kur.is.mapper.EventMapper;
import re1kur.is.mq.EventPublisher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublisherImpl implements EventPublisher {
    private final RabbitTemplate template;
    private final EventMapper mapper;


    @Value("${mq.exchange}")
    private String exchange;

    @Value("${mq.privacy-policy-exchange}")
    private String privacyPolicyExchange;

    @Value("${mq.routing-key.verification-code}")
    private String codeGeneratedRoutKey;

    @Value("${mq.routing-key.service-registration}")
    private String serviceRegisteredRoutKey;

    @Value("${policy.endpoints.file-path}")
    private String filePath;

    @Override
    public void verificationCode(User user, String codeValue) {
        log.info("Generating verification code event for user: {}", user.getId());
        CodeGeneratedEvent event = mapper.codeGenerated(user, codeValue);
        template.convertAndSend(exchange, codeGeneratedRoutKey, event);
        log.info("Sent verification code event: {}", event);
    }

    @PostConstruct
    public void registrationServicePrivacyPolicy() throws IOException {
        log.info("Service registered and started. Publishing registration privacy policy to Authz-service.");
        ObjectMapper ymlMapper = new ObjectMapper(new YAMLFactory());
        ymlMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SnakeCaseStrategy.INSTANCE);

        byte[] bytes = Files.readAllBytes(Path.of(filePath));
        log.info("File successfully read.");
        String yml = new String(bytes);
        ServiceRegisteredEvent event = ymlMapper.readValue(yml, ServiceRegisteredEvent.class);
        template.convertAndSend(privacyPolicyExchange, serviceRegisteredRoutKey, event);
        log.info("Sent service registered event: {}", event);
    }
}
