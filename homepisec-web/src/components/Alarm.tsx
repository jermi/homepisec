import * as React from 'react';
import {AlarmcontrollerApiFp, AlarmStatus, AlarmStatusStateEnum} from "../generated/control-api";
import "./alarm.css";
import {ErrorIcon, OkIcon, WarningIcon} from "../icons";

interface AlarmProps {
  status: AlarmStatus
}

export const Alarm: React.SFC<AlarmProps> = (props) =>
    <div className="alarm-container">
      <div>Alarm status</div>
        <div>State - {getDisplayState(props.status.state!)}</div>
        {props.status.triggerStart &&
        <div>Triggered at {new Date(props.status.triggerStart).toString()}</div>}
        {props.status.triggerSource && <div>Triggered by {props.status.triggerSource.id}</div>}
        {props.status.countdownStart &&
        <div>Countdown started at {new Date(props.status.countdownStart).toString()}</div>}
        {props.status.countdownEnd && props.status.countdownEnd > Date.now() && (
            <div>Countdown end
              in {Math.floor((props.status.countdownEnd - Date.now()) / 1000)} seconds</div>
        )}
      <div className="alarm-actions">
        <button onClick={ArmAlarm}>Arm</button>
        <button onClick={DisarmAlarm}>Disarm</button>
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