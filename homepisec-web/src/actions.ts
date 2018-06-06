import {Action, Dispatch} from "redux"
import {
  AlarmcontrollerApi,
  AlarmStatus,
  DeviceEvent,
  ReadingscontrollerApi,
  RelaycontrollerApi
} from "./generated/control-api";

const alarmApi = new AlarmcontrollerApi(fetch, ".");
const readingsApi = new ReadingscontrollerApi(fetch, ".");
const relaycontrollerApi = new RelaycontrollerApi(fetch, ".");

export enum ActionTypes {
  GET_READINGS = "GET_READINGS",
  GET_READINGS_SUCCESS = "GET_READINGS_SUCCESS",
  GET_ALARM_STATUS = "GET_ALARM_STATUS",
  GET_ALARM_STATUS_SUCCESS = "GET_ALARM_STATUS_SUCCESS",
  ARM_ALARM = "ARM_ALARM",
  ARM_ALARM_SUCCESS = "ARM_ALARM_SUCCESS",
  DISARM_ALARM = "DISARM_ALARM",
  DISARM_ALARM_SUCCESS = "DISARM_ALARM_SUCCESS",
  SWITCH_RELAY = "SWITCH_RELAY",
  SWITCH_RELAY_SUCCESS = "SWITCH_RELAY_SUCCESS",
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

interface ArmAlarmAction extends Action {
  type: ActionTypes.ARM_ALARM
}

interface ArmAlarmSuccessAction extends Action {
  type: ActionTypes.ARM_ALARM_SUCCESS
}

export function armAlarmAction() {
  return (dispatch: Dispatch<ArmAlarmAction | ArmAlarmSuccessAction>) => {
    dispatch({
      type: ActionTypes.ARM_ALARM
    });
    alarmApi.postAlarmArmUsingPOST().then(() => {
      dispatch({
        type: ActionTypes.ARM_ALARM_SUCCESS
      })
    });
  }
}

interface DisarmAlarmAction extends Action {
  type: ActionTypes.DISARM_ALARM
}

interface DisarmAlarmSuccessAction extends Action {
  type: ActionTypes.DISARM_ALARM_SUCCESS
}

export function disarmAlarmAction() {
  return (dispatch: Dispatch<DisarmAlarmAction | DisarmAlarmSuccessAction>) => {
    dispatch({
      type: ActionTypes.DISARM_ALARM
    });
    alarmApi.postAlarmDisarmUsingPOST().then(() => {
      dispatch({
        type: ActionTypes.DISARM_ALARM_SUCCESS
      });
    });
  }
}

interface SwitchRelayAction extends Action {
  type: ActionTypes.SWITCH_RELAY,
  payload: { relayId: string, value: boolean }
}

export interface SwitchRelaySuccessAction extends Action {
  type: ActionTypes.SWITCH_RELAY_SUCCESS,
  payload: { relayId: string, value: boolean }
}

export function switchRelayAction(relayId: string, value: boolean) {
  const payload = {relayId, value};
  return (dispatch: Dispatch<SwitchRelayAction | SwitchRelaySuccessAction>) => {
    dispatch({
      payload,
      type: ActionTypes.SWITCH_RELAY
    });
    relaycontrollerApi.switchRelayUsingPOST(payload).then(() => {
      dispatch({
        payload,
        type: ActionTypes.SWITCH_RELAY_SUCCESS
      });
    });
  }
}