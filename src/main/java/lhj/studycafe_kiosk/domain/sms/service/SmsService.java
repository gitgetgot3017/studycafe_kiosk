package lhj.studycafe_kiosk.domain.sms.service;

import jakarta.annotation.PostConstruct;
import lhj.studycafe_kiosk.domain.sms.dto.VerificationInfo;
import lhj.studycafe_kiosk.domain.sms.exception.SendSmsFailException;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    private Map<String, VerificationInfo> verificationStore = new ConcurrentHashMap<>();

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
        verificationStore.put(toPhoneNumber, new VerificationInfo(verificationCode, LocalDateTime.now().plusMinutes(5)));
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

    public int verifyCode(String phone, String verificationCode) {

        VerificationInfo verificationInfo = verificationStore.get(phone);
        if (verificationInfo == null) {
            return -1;
        }

        String storedCode = verificationInfo.getVerificationCode();
        LocalDateTime expiredDateTime = verificationInfo.getExpiredDateTime();
        LocalDateTime curDateTime = LocalDateTime.now();

        if (storedCode == null || !storedCode.equals(verificationCode)) {
            return -1;
        } else if (curDateTime.isAfter(expiredDateTime)) {
            return -2;
        } else {
            return 1;
        }
    }

    public Map<String, VerificationInfo> getVerificationStore() {
        return verificationStore;
    }
}
