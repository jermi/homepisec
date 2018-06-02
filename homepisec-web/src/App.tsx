import * as React from 'react';

import './App.css';

import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import {Readings} from "./components/Readings";
import {Alarm} from "./components/Alarm";
import {
  AlarmcontrollerApi,
  AlarmStatus,
  DeviceEvent,
  ReadingscontrollerApi
} from "./generated/control-api"
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import {Paper} from "material-ui";
import {AlarmIcon, ReadingsIcon} from "./icons";

interface AppState {
  currentTab: number
  readings: DeviceEvent[]
  alarmStatus?: AlarmStatus
}

class App extends React.Component<{}, AppState> {

  private static api = new AlarmcontrollerApi(fetch, ".");
  private static readingsApi = new ReadingscontrollerApi(fetch, ".");

  constructor(props: {}) {
    super(props);
    this.state = {currentTab: 0, readings: []};
  }

  componentDidMount() {
    App.readingsApi.getReadingsUsingGET().then((readings: DeviceEvent[]) => {
      this.setState({readings, alarmStatus: this.state.alarmStatus});
    });
    App.api.getAlarmStatusUsingGET().then((alarmStatus: AlarmStatus) => {
      this.setState({readings: this.state.readings, alarmStatus})
    });
  };

  tabOnClickHandler = (event: any, currentTab: number) => {
    this.setState({currentTab});
  };

  public render() {
    return (
        <MuiThemeProvider>
          <div>
            <AppBar position="static">
              <Tabs value={this.state.currentTab} onChange={this.tabOnClickHandler}>
                <Tab label="Alarm" icon={<AlarmIcon className="menu-icon"/>}/>
                <Tab label="Readings" icon={<ReadingsIcon className="menu-icon"/>}/>
              </Tabs>
            </AppBar>
            {this.state.currentTab === 0 && !this.state.alarmStatus && (
                <Paper style={{margin: "1em", padding: "1em"}}>
                  <span>No alarm state?</span>
                </Paper>
            )}
            {this.state.currentTab === 0 && this.state.alarmStatus && (
                <Paper style={{margin: "1em", padding: "1em"}}>
                  <Alarm status={this.state.alarmStatus}/>
                </Paper>
            )}
            {this.state.currentTab === 1 && (
                <div style={{padding: "1em"}}>
                  <Readings readings={this.state.readings}/>
                </div>
            )}
          </div>
        </MuiThemeProvider>
    );
  }

}

export default App;
