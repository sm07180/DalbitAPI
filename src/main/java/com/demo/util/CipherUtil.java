package com.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Component
public class CipherUtil {

    public static enum UPPER_LOWER_CASE {
        UPPER,
        LOWER;

        private UPPER_LOWER_CASE() {
        }
    }

    public static enum CIPHER_ALGORITHM {
        SHA1,
        SHA256,
        SHA512,
        AES128,
        AES256;

        private CIPHER_ALGORITHM() {
        }
    }

    public static String encryptSha(CipherUtil.CIPHER_ALGORITHM algorithm, String originalMsg, CipherUtil.UPPER_LOWER_CASE upperLowerCase) {
        String returnMessage = "";
        if (upperLowerCase == CipherUtil.UPPER_LOWER_CASE.UPPER) {
            returnMessage = encryptSha(algorithm, originalMsg).toUpperCase();
        } else {
            returnMessage = encryptSha(algorithm, originalMsg).toLowerCase();
        }

        return returnMessage;
    }

    public static String encryptSha(CipherUtil.CIPHER_ALGORITHM algorithm, String originalMsg) {
        String rtValue = "";
        String algorithmString = "";
        switch(algorithm) {
            case SHA1:
                algorithmString = "SHA-1";
                break;
            case SHA256:
                algorithmString = "SHA-256";
                break;
            case SHA512:
                algorithmString = "SHA-512";
        }

        try {
            StringBuilder sb = new StringBuilder();
            MessageDigest md = MessageDigest.getInstance(algorithmString);
            md.update(originalMsg.getBytes());
            byte[] digestBytes = md.digest();
            byte[] var7 = digestBytes;
            int var8 = digestBytes.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                byte b = var7[var9];
                sb.append(String.format("%02x", b));
            }

            rtValue = sb.toString();
        } catch (NoSuchAlgorithmException var11) {
            log.error(var11.getMessage());
        }

        return CommonUtils.isNullBlank(rtValue) ? "" : rtValue;
    }

    public static String encryptSha256WithSalt(String text, String salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

}
