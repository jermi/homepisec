import * as React from 'react';
import {DeviceReading} from "../generated/control-api";
import ThermIcon from '../icons/therm.svg';
import InfoIcon from '../icons/baseline-info-24px.svg';
import "./readings.css";

export const TempReadingEntry = (id: string, value: string) => {
  return (
      <div className="readings-item" key={id}>
        <div><img className="readings-item-icon" src={ThermIcon} width="1em" height="1em"/></div>
        <div>
          {value} {value}
        </div>
      </div>
  )
};

interface ReadingsProps {
  readings: DeviceReading[]
}

export const Readings = (props: ReadingsProps) => (
    <div>
      list
      <div className="readings-container">
        {props.readings.map(r => {
          if (r.device!.type === "SENSOR_TEMP") {
            return TempReadingEntry(r.device!.id!, r.value!);
          } else {
            return (
                <div className="readings-item" key={r.device!.id!}>
                    <div><img className="readings-item-icon" src={InfoIcon} /></div>
                    <div>
                      <div>{r.value}</div>
                      <div>{r.device!.id}</div>
                    </div>
                </div>
            );
          }
        })}
      </div>
    </div>
);
