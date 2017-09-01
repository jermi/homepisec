package org.homepisec.sensor.core;

import org.homepisec.sensor.rest.client.model.Device;
import org.homepisec.sensor.rest.client.model.DeviceReading;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class W1TempReader {

    private static final BigDecimal TEMP_DIVISOR = new BigDecimal("1000");
    private static final String READ_CMD = "cat /sys/bus/w1/devices/%s/w1_slave";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final BashCmdHelper bashCmdHelper;

    public W1TempReader(BashCmdHelper bashCmdHelper) {
        this.bashCmdHelper = bashCmdHelper;
    }

    public List<DeviceReading> getTempReadings(final List<DeviceRegistry.DeviceW1> tempSensors) {
        return tempSensors
                .stream()
                .map(this::readTemp)
                .collect(Collectors.toList())
                ;
    }

    private DeviceReading readTemp(DeviceRegistry.DeviceW1 sensor) {
        final String cmd = String.format(READ_CMD, sensor.getId());
        try {
            final Process process = Runtime.getRuntime().exec(cmd);
            bashCmdHelper.checkExitCode(cmd, process);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            final String line1 = bufferedReader.readLine();
            if (line1.endsWith("YES")) {
                final String line2 = bufferedReader.readLine();
                final String tempString = line2.substring(line2.indexOf("t=") + 2).trim();
                final BigDecimal temp = new BigDecimal(tempString)
                        .setScale(3, RoundingMode.HALF_UP)
                        .divide(TEMP_DIVISOR, RoundingMode.HALF_UP);
                final Device device = new Device().id(sensor.getId()).type(Device.TypeEnum.SENSOR_TEMP);
                return new DeviceReading().device(device).value(temp.toPlainString());
            }
            throw new W1ReaderException("invalid first line of temp reading: " + line1 + "\nfor read command: " + cmd);
        } catch (Exception e) {
            final String msg = "unable to read temp with command: " + cmd;
            logger.error(msg, e);
            throw new W1ReaderException(msg, e);
        }
    }

    public static class W1ReaderException extends RuntimeException {
        public W1ReaderException(String message) {
            super(message);
        }

        public W1ReaderException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
