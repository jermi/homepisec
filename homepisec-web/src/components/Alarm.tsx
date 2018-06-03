import * as React from 'react';
import {AlarmcontrollerApiFp, AlarmStatus, AlarmStatusStateEnum} from "../generated/control-api";
import "./alarm.css";
import {ErrorIcon, OkIcon, WarningIcon} from "../icons";
import {Button} from "@material-ui/core";

interface AlarmProps {
    status: AlarmStatus
}

export const Alarm: React.SFC<AlarmProps> = (props) =>
    <div className="alarm-container">
        <div>State - {getDisplayState(props.status.state)}</div>
        {props.status.trigger && <div>Triggered at {new Date(props.status.trigger.start).toString()}</div>}
        {props.status.trigger && <div>Triggered by {props.status.trigger.source.id}</div>}
        {props.status.countdown && <div>Countdown started at {new Date(props.status.countdown.start).toString()}</div>}
        {props.status.countdown && props.status.countdown.end > Date.now() && (
            <div>Countdown end
                in {Math.floor((props.status.countdown.end - Date.now()) / 1000)} seconds</div>
        )}
        <div className="alarm-actions">
            <Button variant="raised" color="primary" onClick={ArmAlarm}>
                Arm
            </Button>
            <Button variant="raised" color="secondary" onClick={DisarmAlarm}>
                Disarm
            </Button>
        </div>
    </div>
;


function getDisplayState(state: AlarmStatusStateEnum) {
    let color;
    let icon;
    switch (state) {
        case "DISARMED":
        case "ARMED":
            color = "green";
            icon = <OkIcon className="alarm-state-icon"/>;
            break;
        case "COUNTDOWN":
            color = "yellow";
            icon = <WarningIcon className="alarm-state-icon"/>;
            break;
        case "TRIGGERED":
            color = "red";
            icon = <ErrorIcon className="alarm-state-icon"/>;
    }
    return <span className={`alarm-state ${color}`}>
    <span className="alarm-state-label">{state}</span>
        {icon}
  </span>
}


function ArmAlarm() {
    AlarmcontrollerApiFp.postAlarmArmUsingPOST({})(fetch, ".");
}

function DisarmAlarm() {
    AlarmcontrollerApiFp.postAlarmDisarmUsingPOST({})(fetch, ".");
}