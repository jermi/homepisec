import {AlarmStatus, DeviceEvent} from "./generated/control-api";
import {RouterState} from "react-router-redux";

export interface AppState {
  readonly readings: DeviceEvent[]
  readonly alarmStatus?: AlarmStatus
  readonly router: RouterState
}

export const initialState: AppState = {
  readings: [],
  router: {
    location: null
  }
};
