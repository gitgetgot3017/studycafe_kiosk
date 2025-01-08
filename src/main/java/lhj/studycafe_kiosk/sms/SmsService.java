package lhj.studycafe_kiosk.sms;

import jakarta.annotation.PostConstruct;
import lhj.studycafe_kiosk.sms.exception.SendSmsFailException;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SmsService {

    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    @Value("${coolsms.api.number}")
    private String fromPhoneNumber;

    private Map<String, String> verificationStore = new ConcurrentHashMap<>();

    private DefaultMessageService defaultMessageService;

    @PostConstruct
    public void init() {
         defaultMessageService = NurigoApp.INSTANCE.initialize(
            apiKey,
            apiSecret,
            "https://api.coolsms.co.kr"
        );
    }

    public void sendSms(String toPhoneNumber) {

        Message message = new Message();
        message.setFrom(fromPhoneNumber);
        message.setTo(toPhoneNumber);

        String verificationCode = generateRandomNumber();
        verificationStore.put(toPhoneNumber, verificationCode);
        message.setText("[인증번호] " + verificationCode + " 입니다.");

        try {
            defaultMessageService.send(message);
        } catch (NurigoMessageNotReceivedException | NurigoEmptyResponseException | NurigoUnknownException e) {
            throw new SendSmsFailException("메시지 전송 실패");
        }
    }

    private String generateRandomNumber() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    public boolean verifyCode(String phone, String verificationCode) {
        String storedCode = verificationStore.get(phone);
        return storedCode != null && storedCode.equals(verificationCode);
    }
}
