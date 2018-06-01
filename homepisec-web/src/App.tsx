import * as React from 'react';

import './App.css';

import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import {Menu} from "./components/Menu";
import {Readings} from "./components/Readings";
import {Alarm} from "./components/Alarm";
import {
  AlarmcontrollerApi,
  AlarmStatus,
  DeviceEvent,
  ReadingscontrollerApi
} from "./generated/control-api"

interface AppState {
  readings: DeviceEvent[]
  alarmStatus?: AlarmStatus
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
      this.setState({readings, alarmStatus: this.state.alarmStatus});
    });
    App.api.getAlarmStatusUsingGET().then((alarmStatus: AlarmStatus) => {
      this.setState({readings: this.state.readings, alarmStatus})
    });
  };

  public render() {
    return (
        <MuiThemeProvider>
          <div>
            <Menu/>
            {this.state.alarmStatus && (
                <Alarm status={this.state.alarmStatus}/>
            )}
            <Readings readings={this.state.readings}/>
          </div>
        </MuiThemeProvider>
    );
  }

}

export default App;
