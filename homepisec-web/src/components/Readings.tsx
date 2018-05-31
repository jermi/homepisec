import * as React from 'react';
import {DeviceEvent, DeviceTypeEnum, RelaycontrollerApiFp} from "../generated/control-api";
import ThermIcon from '../icons/therm.svg';
import InfoIcon from '../icons/baseline-info-24px.svg';
import RelayOnIcon from "../icons/baseline-power-24px.svg";
import RelayOffIcon from "../icons/baseline-power_off-24px.svg";
import MotionIcon from "../icons/baseline-rss_feed-24px.svg";
import "./readings.css";

interface ReadingItemProps {
  icon: string;
  primary: string;
  secondary: string;
}

export const ReadingListItem: React.SFC<ReadingItemProps> = (props) =>
    <div className="readings-item">
      <img className="readings-item-icon" src={props.icon}/>
      <div className="readings-item-primary">{props.primary}</div>
      <div className="readings-item-secondary">{props.secondary}</div>
    </div>
;

export const RelayReadingListItem: React.SFC<ReadingItemProps> = (props) =>
    <div className="readings-item">
      <img className="readings-item-icon" src={props.icon}/>
      <div className="readings-item-primary">
        <span>{props.primary === "true" ? "ON" : "OFF"}</span>
        <button
            className="readings-item-relay-switch-button"
            onClick={switchRelay(props.secondary, props.primary !== "true")}
        >
          Switch
        </button>
      </div>
      <div className="readings-item-secondary">{props.secondary}</div>
    </div>
;

export const MotionReadingListItem: React.SFC<ReadingItemProps> = (props) =>
    <div className="readings-item">
      <img className="readings-item-icon" src={props.icon}/>
      <div className="readings-item-primary">{props.primary === "true" ? "MOVEMENT" : "CLEAR"}</div>
      <div className="readings-item-secondary">{props.secondary}</div>
    </div>
;

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
            if (r.device!.type === "RELAY") {
              return <RelayReadingListItem
                  key={r.device!.id!}
                  {...itemProps}
              />;
            } else if (r.device!.type! === "SENSOR_MOTION") {
              return <MotionReadingListItem
                  key={r.device!.id!}
                  {...itemProps}
              />;
            } else {
              return <ReadingListItem
                  key={r.device!.id!}
                  {...itemProps}
              />;
            }
          }
      )}
    </div>;

function determineIcon(type: DeviceTypeEnum, payload: string) {
  let icon = InfoIcon;
  if (type === "SENSOR_TEMP") {
    icon = ThermIcon;
  } else if (type === "SENSOR_MOTION") {
    icon = MotionIcon;
  } else if (type === "RELAY" && payload === "true") {
    icon = RelayOnIcon;
  } else if (type === "RELAY" && payload === "false") {
    icon = RelayOffIcon;
  }
  return icon;
}

function switchRelay(relayId: string, value: boolean): () => void {
  return function switchRelayHandler() {
    RelaycontrollerApiFp.switchRelayUsingPOST({relayId, value})(fetch, ".")
  }
}