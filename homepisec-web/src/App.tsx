import * as React from 'react';

import './App.css';

import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import {Menu} from "./components/Menu";
import {Readings} from "./components/Readings";
import {Alarm} from "./components/Alarm";
import {
  AlarmcontrollerApi,
  AlarmStatus, DeviceEvent,
  DeviceReading,
  ReadingscontrollerApi
} from "./generated/control-api"

interface AppState {
  readings: DeviceEvent[]
}

class App extends React.Component<{}, AppState> {

  private static api = new AlarmcontrollerApi(fetch, ".");
  private static readingsApi = new ReadingscontrollerApi(fetch, ".");

  constructor(props: {}) {
    super(props);
    this.state = {readings: []};
  }

  componentDidMount() {
    App.readingsApi.getReadingsUsingGET().then((readings: DeviceEvent[]) => {
      this.setState({readings});
    });
  };

  public render() {

    App.api.getAlarmStatusUsingGET().then((alarmStatus: AlarmStatus) => {
      // tslint:disable-next-line
      console.log("got status", alarmStatus)

    }, (error) => {
      // tslint:disable-next-line
      console.error("failed to get alarm status", ...error)
    });
    return (
        <MuiThemeProvider>
          <div>
            <Menu/>
            <Alarm/>
            <Readings readings={this.state.readings}/>
          </div>
        </MuiThemeProvider>
    );
  }

}

export default App;
