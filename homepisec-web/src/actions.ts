import {Action, Dispatch} from "redux"
import {
  AlarmcontrollerApi,
  AlarmStatus,
  DeviceEvent,
  ReadingscontrollerApi
} from "./generated/control-api";

const alarmApi = new AlarmcontrollerApi(fetch, ".");
const readingsApi = new ReadingscontrollerApi(fetch, ".");

export enum ActionTypes {
  GET_READINGS = "GET_READINGS",
  GET_READINGS_SUCCESS = "GET_READINGS_SUCCESS",
  GET_ALARM_STATUS = "GET_ALARM_STATUS",
  GET_ALARM_STATUS_SUCCESS = "GET_ALARM_STATUS_SUCCESS",
  ARM_ALARM = "ARM_ALARM",
  DISARM_ALARM = "DISARM_ALARM"
}

interface GetReadingsAction extends Action {
  type: ActionTypes.GET_READINGS
}

export interface GetReadingsSuccessAction extends Action {
  type: ActionTypes.GET_READINGS_SUCCESS
  payload: DeviceEvent[]
}

export function getReadingsAction() {
  return (dispatch: Dispatch<GetReadingsAction | GetReadingsSuccessAction>) => {
    dispatch({
      type: ActionTypes.GET_READINGS
    });
    readingsApi.getReadingsUsingGET().then((readings: DeviceEvent[]) => {
      dispatch({
        payload: readings,
        type: ActionTypes.GET_READINGS_SUCCESS
      })
    });
  }
}

interface GetAlarmStatusAction extends Action {
  type: ActionTypes.GET_ALARM_STATUS
}

export interface GetAlarmStatusSuccessAction extends Action {
  type: ActionTypes.GET_ALARM_STATUS_SUCCESS
  payload: AlarmStatus
}

export function getAlarmStatus() {
  return (dispatch: Dispatch<GetAlarmStatusAction | GetAlarmStatusSuccessAction>) => {
    dispatch({
      type: ActionTypes.GET_ALARM_STATUS
    });
    alarmApi.getAlarmStatusUsingGET().then((alarmStatus: AlarmStatus) => {
      dispatch({
        payload: alarmStatus,
        type: ActionTypes.GET_ALARM_STATUS_SUCCESS
      });
    })
  }
}
