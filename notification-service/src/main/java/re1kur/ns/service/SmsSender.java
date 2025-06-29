package re1kur.ns.service;

import re1kur.core.dto.SmsMessage;

public interface SmsSender {
    void send(SmsMessage sms);
}
