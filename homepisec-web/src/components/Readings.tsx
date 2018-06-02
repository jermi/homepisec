import * as React from 'react';
import {ReactNode} from 'react';
import {DeviceEvent, DeviceTypeEnum, RelaycontrollerApiFp} from "../generated/control-api";

import "./readings.css";
import {InfoIcon, MotionIcon, RelayOffIcon, RelayOnIcon, ThermIcon} from "../icons";
import {Paper} from "material-ui";
import {Button} from "@material-ui/core";

interface ReadingsProps {
  readings: DeviceEvent[]
}

export const Readings: React.SFC<ReadingsProps> = (props) =>
    <div className="readings-container">
      {props.readings.map(r => {
        const itemProps: ReadingItemProps = {
          icon: determineIcon(r.device!.type!, r.payload!),
          primary: r.payload!,
          secondary: r.device!.id!
        };
        return <Paper key={r.device!.id!} style={{padding: "1em"}}>
          {renderItem(r, itemProps)}
        </Paper>
      })}
    </div>;

function renderItem(r: DeviceEvent, itemProps: ReadingItemProps) {
  if (r.device!.type === "RELAY") {
    return <RelayReadingListItem
        {...itemProps}
    />;
  } else if (r.device!.type! === "SENSOR_MOTION") {
    return <MotionReadingListItem
        {...itemProps}
    />;
  } else {
    return <ReadingListItem
        {...itemProps}
    />;
  }
}

interface ReadingItemProps {
  icon: ReactNode;
  primary: string;
  secondary: string;
}

const ReadingListItem: React.SFC<ReadingItemProps> = (props) =>
    <div className="readings-item">
      {props.icon}
      <div className="readings-item-primary">{props.primary}</div>
      <div className="readings-item-secondary">{props.secondary}</div>
    </div>
;

const RelayReadingListItem: React.SFC<ReadingItemProps> = (props) =>
    <div className="readings-item">
      {props.icon}
      <div className="readings-item-primary">
        <span>{props.primary === "true" ? "ON" : "OFF"}</span>
        <Button
            variant="raised"
            style={{marginLeft: "5em", verticalAlign: "baseline"}}
            onClick={switchRelay(props.secondary, props.primary !== "true")}
        >
          Switch
        </Button>
      </div>
      <div className="readings-item-secondary">{props.secondary}</div>
    </div>
;

const MotionReadingListItem: React.SFC<ReadingItemProps> = (props) =>
    <div className="readings-item">
      {props.icon}
      <div className="readings-item-primary">{props.primary === "true" ? "MOVEMENT" : "CLEAR"}</div>
      <div className="readings-item-secondary">{props.secondary}</div>
    </div>
;

function determineIcon(type: DeviceTypeEnum, payload: string) {
  let icon;
  if (type === "SENSOR_TEMP") {
    icon = <ThermIcon
        className="readings-item-icon"
        style={{stroke: determineThermIconColor(parseInt(payload, 10))}}
    />;
  } else if (type === "SENSOR_MOTION") {
    icon = <MotionIcon
        className="readings-item-icon"
        style={{fill: payload === 'true' ? '#000000' : '#cdcdcd'}}
    />;
  } else if (type === "RELAY" && payload === "true") {
    icon = <RelayOnIcon
        className="readings-item-icon"
        style={{filter: payload === 'true' ? "drop-shadow( 0px 0px 4px #ffff00 )" : "none"}}
    />;
  } else if (type === "RELAY" && payload === "false") {
    icon = <RelayOffIcon className="readings-item-icon"/>;
  } else {
    icon = <InfoIcon className="readings-item-icon"/>
  }
  return icon;
}

function determineThermIconColor(temperature: number): string {
  let color;
  if (!isNaN(temperature)) {
    if (temperature > 40) {
      color = '#FF0000';
    } else if (temperature <= 40 && temperature > 25) {
      color = '#ff9900';
    } else if (temperature <= 25 && temperature > 15) {
      color = '#00ce10';
    } else if (temperature <= 15 && temperature > 0) {
      color = '#07dfff';
    } else if (temperature <= 0 && temperature > -15) {
      color = '#0095ff';
    } else {
      color = '#0015ff';
    }
  } else {
    color = '#000000';
  }
  return color;
}

function switchRelay(relayId: string, value: boolean): () => void {
  return function switchRelayHandler() {
    RelaycontrollerApiFp.switchRelayUsingPOST({relayId, value})(fetch, ".")
  }
}