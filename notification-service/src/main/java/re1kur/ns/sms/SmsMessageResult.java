package re1kur.ns.sms;

import lombok.Data;

@Data
public class SmsMessageResult {
    private String status;
    private int status_code;
    private String sms_id;

    public SMSRuResponseStatus getStatusEnum() {
        return SMSRuResponseStatus.forValue(status_code);
    }
}
