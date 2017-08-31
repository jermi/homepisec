package org.homepisec.sensor.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

@Component
public class DeviceRegistryProvider {

    private static final Charset CHARSET_UTF8 = Charsets.toCharset("UTF-8");
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String devicesPath;
    private final ObjectMapper objectMapper;

    public DeviceRegistryProvider(
            @Value("${devicesPath:#{null}}") String devicesPath,
            ObjectMapper objectMapper
    ) {
        this.devicesPath = devicesPath;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        if(devicesPath != null) {
            logger.info("devices registry will be loaded from {}", devicesPath);
        } else {
            logger.info("will use default devices definition");
        }
    }

    public DeviceRegistry loadRegistry() {
        try {
            final String devicesJson;
            if (devicesPath != null) {
                logger.debug("loading devices definition from file: {}", devicesPath);
                devicesJson = new String(Files.readAllBytes(Paths.get(devicesPath)), CHARSET_UTF8);
            } else {
                logger.debug("loading default devices definition");
                final InputStream is = getClass().getClassLoader().getResourceAsStream("devices.json");
                devicesJson = new Scanner(is).useDelimiter("\\A").next();
            }
            return objectMapper.readValue(devicesJson, DeviceRegistry.class);
        } catch (Exception e) {
            final String msg = "error when loading configuration: " + e.getMessage();
            logger.error(msg, e);
            throw new DeviceRegistryException(msg, e);
        }
    }

    public static class DeviceRegistryException extends RuntimeException {
        public DeviceRegistryException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
