import {AppState, initialState} from "./model";
import {Action} from "redux";
import {
  ActionTypes,
  GetAlarmStatusSuccessAction,
  GetReadingsSuccessAction,
  SwitchRelaySuccessAction
} from "./actions";
import {routerReducer} from "react-router-redux";
import {AlarmStatus, DeviceEvent} from "./generated/control-api";

export const rootReducer = (state: AppState = initialState, action: Action): AppState => {
  return {
    alarmStatus: alarmStatusReducer(state.alarmStatus, action),
    readings: readingsReducer(state.readings, action),
    router: routerReducer(state.router, action)
  };
};

const alarmStatusReducer = (state: AlarmStatus | undefined, action: Action) => {
  switch (action.type) {
    case ActionTypes.GET_ALARM_STATUS_SUCCESS:
      return (action as GetAlarmStatusSuccessAction).payload;
    default:
      return state;
  }
};

const readingsReducer = (state: DeviceEvent[], action: Action) => {
  switch (action.type) {
    case ActionTypes.GET_READINGS_SUCCESS:
      return (action as GetReadingsSuccessAction).payload;
    // case ActionTypes.SWITCH_RELAY_SUCCESS:
    //   const {relayId, value} = (action as SwitchRelaySuccessAction).payload;
    //   return state.map(e => {
    //     if (e.device.id === relayId) {
    //       return {
    //         device: e.device,
    //         payload: value.toString(),
    //         time: e.time,
    //         type: e.type
    //       }
    //     }
    //     return e
    //   });
    default:
      return state;
  }
};