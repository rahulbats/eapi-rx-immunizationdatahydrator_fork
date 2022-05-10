package com.wba.eapi.eapirximmunizationdatahydrator.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class CommonUtils {


    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

    public static String getSecretFromPath(String fileName) {
        String data = "";
        try {
            data = new String(Files.readAllBytes(Paths.get(fileName))); //NOSONAR
            LOGGER.debug("File path  - {} data - {} " , fileName, data);
        } catch (IOException e) {
            LOGGER.error("Error reading the secret from the path => {} ", fileName);
        }
        return data;
    }


}
