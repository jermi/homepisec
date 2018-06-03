package org.homepisec.control.core.alarm;

import io.swagger.annotations.ApiModelProperty;
import org.homepisec.control.rest.dto.Device;

import javax.validation.constraints.NotNull;

public class AlarmStatus {

    public static class CountdownStatus {

        @NotNull
        @ApiModelProperty(required = true)
        private Long start;
        @NotNull
        @ApiModelProperty(required = true)
        private Long end;
        @NotNull
        @ApiModelProperty(required = true)
        private Device source;

        private CountdownStatus() {
        }

        public CountdownStatus(Long start, Long end, Device source) {
            this.start = start;
            this.end = end;
            this.source = source;
        }

        public Long getStart() {
            return start;
        }

        public void setStart(@NotNull Long start) {
            this.start = start;
        }

        public Long getEnd() {
            return end;
        }

        public void setEnd(@NotNull Long end) {
            this.end = end;
        }

        public Device getSource() {
            return source;
        }

        public void setSource(@NotNull Device source) {
            this.source = source;
        }

        @Override
        public String toString() {
            return "CountDown{" +
                    "start=" + start +
                    ", end=" + end +
                    ", source=" + source +
                    '}';
        }
    }

    public static class TriggerStatus {

        @NotNull
        @ApiModelProperty(required = true)
        private Long start;
        @NotNull
        @ApiModelProperty(required = true)
        private Device source;

        private TriggerStatus() {
        }

        public TriggerStatus(@NotNull Long start, @NotNull Device source) {
            this.start = start;
            this.source = source;
        }

        public Long getStart() {
            return start;
        }

        public void setStart(Long start) {
            this.start = start;
        }

        public Device getSource() {
            return source;
        }

        public void setSource(Device source) {
            this.source = source;
        }

        @Override
        public String toString() {
            return "TriggeredStatus{" +
                    "start=" + start +
                    ", source=" + source +
                    '}';
        }
    }

    @NotNull
    @ApiModelProperty(required = true)
    private AlarmState state = AlarmState.DISARMED;
    private CountdownStatus countdown;
    private TriggerStatus trigger;

    public AlarmState getState() {
        return state;
    }

    public void setState(AlarmState state) {
        this.state = state;
    }

    public CountdownStatus getCountdown() {
        return countdown;
    }

    public void setCountdown(CountdownStatus countdown) {
        this.countdown = countdown;
    }

    public TriggerStatus getTrigger() {
        return trigger;
    }

    public void setTrigger(TriggerStatus trigger) {
        this.trigger = trigger;
    }

    @Override
    public String toString() {
        return "AlarmStatus{" +
                "state=" + state +
                ", countdown=" + countdown +
                ", trigger=" + trigger +
                '}';
    }
}
