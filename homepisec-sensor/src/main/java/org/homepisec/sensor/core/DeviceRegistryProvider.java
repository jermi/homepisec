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
    private final String capabilitiesPath;
    private final ObjectMapper objectMapper;

    @Autowired
    public DeviceRegistryProvider(
            @Value("${capabilitiesPath:#{null}}") String capabilitiesPath,
            ObjectMapper objectMapper
    ) {
        this.capabilitiesPath = capabilitiesPath;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        if(capabilitiesPath != null) {
            logger.info("device registry will be loaded from {}", capabilitiesPath);
        } else {
            logger.info("will be used default device registry");
        }
    }

    public DeviceRegistry loadCapabilities() {
        try {
            final String capabilitiesJson;
            if (capabilitiesPath != null) {
                logger.debug("loading capabilities from file: {}", capabilitiesPath);
                capabilitiesJson = new String(Files.readAllBytes(Paths.get(capabilitiesPath)), CHARSET_UTF8);
            } else {
                logger.debug("loading default capabilities");
                final InputStream is = getClass().getClassLoader().getResourceAsStream("deviceRegistry.json");
                capabilitiesJson = new Scanner(is).useDelimiter("\\A").next();
            }
            return objectMapper.readValue(capabilitiesJson, DeviceRegistry.class);
        } catch (Exception e) {
            final String msg = "error when loading capabilities: " + e.getMessage();
            logger.error(msg, e);
            throw new CapabilitiesProviderException(msg, e);
        }
    }

    public static class CapabilitiesProviderException extends RuntimeException {
        public CapabilitiesProviderException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
