package lhj.studycafekiosk.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class PasswordEncryption {

    private static final int SALT_SIZE = 16;

    public String hashing(byte[] password, String salt) {

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 알고리즘을 찾을 수 없습니다.", e);
        }

        for (int i = 0; i < 10000; i++) {
            String temp = byteArrToString(password) + salt;
            md.update(temp.getBytes());
            password = md.digest();
        }

        return byteArrToString(password);
    }

    public String getSalt() {

        byte[] salt = new byte[SALT_SIZE];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);
        return byteArrToString(salt);
    }

    public String byteArrToString(byte[] bytes) {

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
