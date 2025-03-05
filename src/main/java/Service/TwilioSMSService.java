package Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

public class TwilioSMSService {
    private static final String ACCOUNT_SID = "";
    private static final String AUTH_TOKEN = "";
    private static final String FROM_NUMBER = "+15737474358"; // Numéro Twilio

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public static void sendSMS(String to, String message) {
        Message.creator(
                new com.twilio.type.PhoneNumber(to),
                new com.twilio.type.PhoneNumber(FROM_NUMBER),
                message
        ).create();
    }
}
