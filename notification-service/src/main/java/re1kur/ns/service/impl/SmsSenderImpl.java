package re1kur.ns.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import re1kur.core.dto.SmsMessage;
import re1kur.ns.service.SmsSender;
import re1kur.ns.sms.SmsSendResponse;

@Component
@RequiredArgsConstructor
public class SmsSenderImpl implements SmsSender {
    private final RestTemplate template;

    @Value("${sms-sender.endpoint}")
    private String endpoint;

    @Value("${sms-sender.api-id}")
    private String apiId;

    @Override
    public void send(SmsMessage sms) {
        String formatted = endpoint.formatted(apiId, sms.to(), sms.msg());
        ResponseEntity<SmsSendResponse> exchange = template.exchange(
                formatted,
                HttpMethod.GET,
                new HttpEntity<>(null),
                SmsSendResponse.class);
        exchange.getStatusCode();
    }
}
