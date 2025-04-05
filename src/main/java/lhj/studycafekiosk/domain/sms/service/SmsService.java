package lhj.studycafekiosk.domain.sms.service;

import jakarta.annotation.PostConstruct;
import lhj.studycafekiosk.domain.sms.exception.SendSmsFailException;
import lhj.studycafekiosk.domain.sms.exception.VerificationAttemptLimitExceededException;
import lombok.AllArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
@AllArgsConstructor
public class SmsService {

    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    @Value("${coolsms.api.number}")
    private String fromPhoneNumber;

    private DefaultMessageService defaultMessageService;

    @Qualifier("stringRedisTemplate")
    private final RedisTemplate<String, String> stringRedisTemplate;

    @Qualifier("integerRedisTemplate")
    private final RedisTemplate<String, Integer> integerRedisTemplate;

    public static final Duration REDIS_TTL = Duration.ofMinutes(5);
    public static final int ATTEMPT_LIMIT_COUNT = 5;

    @PostConstruct
    public void init() {
         defaultMessageService = NurigoApp.INSTANCE.initialize(
            apiKey,
            apiSecret,
            "https://api.coolsms.co.kr"
        );
    }

    public void sendSms(String toPhoneNumber) {

        String authPhoneKey = "auth:phone:" + toPhoneNumber;
        String authAttempt = "auth:attempt:" + toPhoneNumber;

        if (integerRedisTemplate.opsForValue().get(authAttempt) >= ATTEMPT_LIMIT_COUNT) {
            throw new VerificationAttemptLimitExceededException("인증 횟수 제한을 초과하였습니다. 5분 후에 다시 시도해주세요.");
        }

        Message message = new Message();
        message.setFrom(fromPhoneNumber);
        message.setTo(toPhoneNumber);

        String verificationCode = generateRandomNumber();
        stringRedisTemplate.opsForValue().set(authPhoneKey, verificationCode, REDIS_TTL);
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

        String authPhoneKey = "auth:phone:" + phone;
        String authAttempt = "auth:attempt:" + phone;

        String redisVerificationCode = stringRedisTemplate.opsForValue().get(authPhoneKey);
        if (redisVerificationCode == null) {
            return -1;
        } else if(!redisVerificationCode.equals(verificationCode)) {
            integerRedisTemplate.opsForValue().increment(authAttempt);
            integerRedisTemplate.expire(authAttempt, REDIS_TTL);
            return -2;
        } else {
            integerRedisTemplate.delete(authAttempt);
            return 1;
        }
    }
}
