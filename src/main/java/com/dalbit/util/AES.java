package com.dalbit.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

@Slf4j
@Component
public class AES {

      private static SecretKeySpec secretKey;
      private static byte[] key;

      public static void setKey(String myKey){
            MessageDigest sha = null;
            try {
                  key = myKey.getBytes("UTF-8");
                  sha = MessageDigest.getInstance("SHA-1");
                  key = sha.digest(key);
                  key = Arrays.copyOf(key, 16);
                  secretKey = new SecretKeySpec(key, "AES");
            }
            catch (NoSuchAlgorithmException e) {
                  log.error("AES.setKey NoSuchAlgorithmException - myKey : [{}]", myKey);
                  //e.printStackTrace();
            }
            catch (UnsupportedEncodingException e) {
                  log.error("AES.setKey UnsupportedEncodingException - myKey : [{}]", myKey);
                  //e.printStackTrace();
            }
      }

      public static String encrypt(String strToEncrypt, String secret){
            try{
                  setKey(secret);
                  Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                  cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                  return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
            }catch (Exception e){
                  log.error("AES.encrypt Error - strToEncrypt : [{}]", strToEncrypt);
                  log.error("AES.encrypt Error - secret : [{}]", secret);
                  //e.printStackTrace();
            }
            return null;
      }

      public static String decrypt(String strToDecrypt, String secret){
            try{
                  setKey(secret);
                  Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
                  cipher.init(Cipher.DECRYPT_MODE, secretKey);
                  return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
            }catch (Exception e){
                  log.error("AES.decrypt Error - strToEncrypt : [{}]", strToDecrypt);
                  log.error("AES.decrypt Error - secret : [{}]", secret);
                  //e.printStackTrace();
            }
            return null;
      }
}
