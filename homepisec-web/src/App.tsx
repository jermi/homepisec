import * as React from 'react';

import './App.css';

import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import {Menu} from "./components/Menu";
import {Readings} from "./components/Readings";
import {Settings} from "./components/Settings";
import {
  AlarmcontrollerApi,
  AlarmStatus,
  DeviceReading,
  ReadingscontrollerApi
} from "./generated/control-api"

interface AppState {
  readings: DeviceReading[]
}

class App extends React.Component<{}, AppState> {

  private api = new AlarmcontrollerApi(fetch, ".");
  private readingsApi = new ReadingscontrollerApi(fetch, ".");

  constructor(props: {}) {
    super(props);
    this.state = {readings: []};
  }

  componentDidMount() {
    this.readingsApi.getReadingsUsingGET().then(readings => {
      this.setState({readings});
    });
  };

  public render() {

    this.api.getAlarmStatusUsingGET().then((alarmStatus: AlarmStatus) => {
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
            <Settings/>
            <Readings readings={this.state.readings || []}/>
          </div>
        </MuiThemeProvider>
    );
  }

}

export default App;
