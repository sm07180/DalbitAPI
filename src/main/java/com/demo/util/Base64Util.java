package com.demo.util;


import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

/**
 * TODO - photo_server
 */
@Slf4j
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
            e.printStackTrace();
            return null;
        }
    }
}
