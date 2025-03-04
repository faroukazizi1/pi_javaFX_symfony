package Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

public class SmsService {
    // Remplace par tes infos Twilio
    public static final String ACCOUNT_SID = "";
    public static final String AUTH_TOKEN = "";
    public static final String TWILIO_PHONE_NUMBER = "+18578556906"; // Numéro Twilio

    // 🔹 Initialisation statique pour éviter les problèmes
    static {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            System.out.println("✅ Twilio initialisé avec succès !");
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'initialisation de Twilio : " + e.getMessage());
        }
    }

    public static void envoyerSms(String numeroDestinataire, String message) {
        try {
            Message sms = Message.creator(
                    new com.twilio.type.PhoneNumber(numeroDestinataire),
                    new com.twilio.type.PhoneNumber(TWILIO_PHONE_NUMBER),
                    message
            ).create();

            System.out.println("📩 SMS envoyé avec succès ! SID: " + sms.getSid());
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'envoi du SMS : " + e.getMessage());
        }
    }
}
