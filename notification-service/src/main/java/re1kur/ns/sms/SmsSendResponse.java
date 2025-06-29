package re1kur.ns.sms;

import lombok.Data;

import java.util.Map;

@Data
public class SmsSendResponse {
    private String status;
    private int status_code;
    private Map<String, SmsMessageResult> sms;
    private double balance;

    public SMSRuResponseStatus getStatusEnum() {
        return SMSRuResponseStatus.forValue(status_code);
    }
}
