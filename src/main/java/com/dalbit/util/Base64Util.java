package com.dalbit.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@Slf4j
@Component
public class Base64Util {
    private static final String BASE_64_PREFIX = "data:image/png;base64,";

    public static byte[] decodeBase64ToBytes(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }

    public static File decodeBase64ToFile(String base64String, String fileName, String fileExtension) {
        byte[] bytes = decodeBase64ToBytes(base64String);
        File returnFile = new File(fileName + "." + fileExtension);
        try {
            Files.copy(new ByteArrayInputStream(bytes), returnFile.toPath());
            return returnFile;
        } catch (IOException e) {
            log.error(e.getMessage());
            //e.printStackTrace();
            return null;
        }
    }

    public String encode(byte[] encodeByte){
        byte[] buf = null;
        String strResult = null;

        BASE64Encoder encoder = new BASE64Encoder();
        ByteArrayInputStream bin = new ByteArrayInputStream(encodeByte);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        try{
            encoder.encodeBuffer(bin, bout);
        }catch(Exception ex){
            log.error(String.valueOf(ex));
        }

        buf = bout.toByteArray();
        strResult = new String(buf).trim();
        return strResult;
    }

    public byte[] decode(String strEncode){
        byte[] buf = null;
        BASE64Decoder decoder = new BASE64Decoder();
        ByteArrayInputStream bin = new ByteArrayInputStream(strEncode.getBytes());
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        try{
            decoder.decodeBuffer(bin, bout);
        }catch(Exception ex){
            log.error(String.valueOf(ex));
        }
        buf = bout.toByteArray();
        return buf;
    }
}
