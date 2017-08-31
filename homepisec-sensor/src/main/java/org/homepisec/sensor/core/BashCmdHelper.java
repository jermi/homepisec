package org.homepisec.sensor.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BashCmdHelper {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void checkExitCode(final String cmd, final Process process) {
        try {
            final int exitValue = process.waitFor();
            if (exitValue != 0) {
                final String msg = "unexpected exit code {} when executing command {}";
                logger.error(msg, exitValue, cmd);
                throw new CmdException(msg);
            }
        } catch (InterruptedException e) {
            final String msg = "failed to check exit code for command " + cmd + ": " + e.getMessage();
            logger.error(msg, e);
            Thread.currentThread().interrupt();
        }
    }

    public static class CmdException extends RuntimeException {
        public CmdException(String message) {
            super(message);
        }
    }

}
