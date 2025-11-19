package com.coljuegos.sivo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class AesUtil {

    private static final String ALGORITHM = "AES";

    @Value("${aes.secretKey}")
    private String secretKey;

    public String decrypt(String encryptedBase64) throws Exception {
        byte[] keyBytes = this.secretKey.getBytes();
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        byte[] decoded = Base64.getDecoder().decode(encryptedBase64);
        byte[] decrypted = cipher.doFinal(decoded);

        return new String(decrypted);
    }

    public static String encrypt(String plainText, String secretKey) throws Exception {
        byte[] keyBytes = secretKey.getBytes();
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        byte[] encrypted = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

}
