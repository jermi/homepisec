package org.homepisec.sensor.core;

import org.apache.commons.codec.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class GpioProvider {

    private static final String GPIO_PIN_PATH = "/sys/class/gpio/gpio";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final BashCmdHelper bashCmdHelper;

    public GpioProvider(BashCmdHelper bashCmdHelper) {
        this.bashCmdHelper = bashCmdHelper;
    }

    @PostConstruct
    public void gpioCheck() {
        final String cmd = "gpio -v";
        try {
            final Process process = Runtime.getRuntime().exec(cmd);
            bashCmdHelper.checkExitCode(cmd, process);
        } catch (Exception e) {
            logger.error("*** SERIOUS PROBLEM DETECTED! *** unable to execute gpio command \"" + cmd + "\": " + e.getMessage()
                    + ", verify that Wiring Pi is installed and available under $PATH - consult http://wiringpi.com/download-and-install/");
        }
    }

    public boolean readPin(int pin) {
        try {
            enablePin(pin, getCurrentPinDirection(pin));
            final String pinValuePath = GPIO_PIN_PATH + pin + "/value";
            final String pinValue = readFileContents(pinValuePath);
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

    private static String readFileContents(String pinValuePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(pinValuePath)), Charsets.toCharset("UTF-8")).trim();
    }

    public void writePin(int pin, boolean value) {
        try {
            enablePin(pin, Direction.OUT);
            logger.debug("setting pin {} to {}", pin, value);
            final String cmd = "gpio -g write " + pin + " " + (value ? "1" : "0");
            logger.debug("setting pin {} to {} with command: {}", pin, value, cmd);
            final Process process = Runtime.getRuntime().exec(cmd);
            bashCmdHelper.checkExitCode(cmd, process);
        } catch (Exception e) {
            String msg = "failed to wrtie to pin " + pin + " value " + value + " because: " + e.getMessage();
            logger.error(msg, e);
            throw new GpioException(msg, e);
        }
    }

    private void enablePin(int pin, final Direction direction) {
        try {
            final boolean pinEnabled = new File(GPIO_PIN_PATH + pin).exists();
            if (!pinEnabled) {
                final String cmd = "gpio export " + pin + " " + direction.code;
                logger.debug("enabling pin with command: {}", cmd);
                final Process process = Runtime.getRuntime().exec(cmd);
                bashCmdHelper.checkExitCode(cmd, process);
            } else {
                logger.debug("pin {} already enabled", pin);
            }
        } catch (Exception e) {
            final String msg = "failed to enable pin " + pin + ": " + e.getMessage();
            logger.error(msg, e);
            throw new GpioException(msg, e);
        }
    }

    public void setPinDirection(final int pin, final Direction direction) {
        try {
            enablePin(pin, direction);
            final Direction oldDirection = getCurrentPinDirection(pin);
            if (!oldDirection.equals(direction)) {
                final String cmd = "gpio -g mode " + pin + " " + direction.code;
                logger.debug("changing pin {} direction from {} to {} with command: {}", pin, oldDirection, direction, cmd);
                final Process process = Runtime.getRuntime().exec(cmd);
                bashCmdHelper.checkExitCode(cmd, process);
            }
        } catch (Exception e) {
            final String msg = "failed to set pin " + pin + " direction to " + direction + ": " + e.getMessage();
            logger.error(msg, e);
            throw new GpioException(msg, e);
        }
    }

    private static Direction getCurrentPinDirection(int pin) throws IOException {
        final String directionPath = GPIO_PIN_PATH + pin + "/direction";
        if (new File(directionPath).exists()) {
            final String directionString = readFileContents(directionPath);
            return Direction.getForCode(directionString);
        }
        return Direction.IN;
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
