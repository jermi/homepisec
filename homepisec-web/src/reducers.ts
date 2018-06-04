import {AppState, initialState} from "./model";
import {Action} from "redux";
import {ActionTypes, GetAlarmStatusSuccessAction, GetReadingsSuccessAction} from "./actions";
import {CALL_HISTORY_METHOD, LOCATION_CHANGE, routerReducer} from "react-router-redux";

export const rootReducer = (state: AppState = initialState, action: Action): AppState => {
  switch (action.type) {
    case ActionTypes.GET_ALARM_STATUS_SUCCESS:
      return {
        alarmStatus: (action as GetAlarmStatusSuccessAction).payload,
        readings: state.readings,
        router: state.router
      };
    case ActionTypes.GET_READINGS_SUCCESS:
      return {
        alarmStatus: state.alarmStatus,
        readings: (action as GetReadingsSuccessAction).payload,
        router: state.router
      };
    case LOCATION_CHANGE:
    case CALL_HISTORY_METHOD:
      return {
        alarmStatus: state.alarmStatus,
        readings: state.readings,
        router: routerReducer(state.router, action)
      };
    default:
      return {
        alarmStatus: state.alarmStatus,
        readings: state.readings,
        router: state.router
      };
  }
};