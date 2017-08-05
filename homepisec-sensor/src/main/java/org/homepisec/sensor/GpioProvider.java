package org.homepisec.sensor;

import org.apache.commons.codec.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class GpioProvider {

    private static final Charset CHARSET_UTF8 = Charsets.toCharset("UTF-8");
    private static final String GPIO = "/sys/class/gpio";
    private static final String GPIO_PIN = "/gpio";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public boolean readPin(int pin) {
        try {
            enablePin(pin);
            setPinDirection(pin, Direction.IN);
            final String pinValuePath = GPIO + GPIO_PIN + pin + "/value";
            final String pinValue = new String(Files.readAllBytes(Paths.get(pinValuePath)), CHARSET_UTF8).trim();
            switch (pinValue) {
                case "1":
                    return true;
                case "0":
                    return false;
                default:
                    throw new GpioException("unexpected pin value: " + pinValue + " for pin " + pin);
            }
        } catch (Exception e) {
            final String msg = "failed to read pin " + pin + " value: " + e.getMessage();
            logger.error(msg, e);
            throw new GpioException(msg, e);
        }
    }

    private void enablePin(int pin) {
        try {
            final boolean pinEnabled = new File(GPIO + GPIO_PIN + pin).exists();
            if (!pinEnabled) {
                final String cmd = "echo 1 > " + GPIO + "/export/" + pin;
                logger.debug("enabling pin with command: {}", cmd);
                final Process process = Runtime.getRuntime().exec(cmd);
                checkExitCode(cmd, process);
            } else {
                logger.debug("pin {} already enabled", pin);
            }
        } catch (IOException e) {
            final String msg = "failed to enable pin " + pin + ": " + e.getMessage();
            logger.error(msg, e);
            throw new GpioException(msg, e);
        }
    }

    private void checkExitCode(final String cmd, final Process exec) {
        final int exitValue = exec.exitValue();
        if (exitValue != 0) {
            logger.error("unexpected exit code {} when executing command {}", exitValue, cmd);
        }
    }

    private void setPinDirection(final int pin, final Direction direction) {
        try {
            final String directionPath = GPIO + GPIO_PIN + pin + "/direction";
            final String directionString = new String(Files.readAllBytes(Paths.get(directionPath)), CHARSET_UTF8).trim();
            final Direction oldDirection = Direction.getForCode(directionString);
            if (!oldDirection.equals(direction)) {
                final String cmd = "echo " + direction.name() + " > " + directionPath;
                logger.debug("changing pin {} direction from {} to {} with command: {}", pin, oldDirection, direction, cmd);
                final Process process = Runtime.getRuntime().exec(cmd);
                checkExitCode(cmd, process);
            }
        } catch (Exception e) {
            final String msg = "failed to set pin " + pin + " direction to " + direction + ": " + e.getMessage();
            logger.error(msg, e);
            throw new GpioException(msg, e);
        }
    }

    public enum Direction {

        IN("in"),
        OUT("out");

        private final String code;

        Direction(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public static Direction getForCode(String code) {
            for (Direction value : values()) {
                if (value.code.equals(code)) {
                    return value;
                }
            }
            throw new IllegalArgumentException("invalid code - " + code);
        }

    }

    public static final class GpioException extends RuntimeException {
        public GpioException(String message, Throwable cause) {
            super(message, cause);
        }

        public GpioException(String message) {
            super(message);
        }
    }

}
