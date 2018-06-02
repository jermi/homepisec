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
import {Paper, TabsProps} from "material-ui";
import {AlarmIcon, ReadingsIcon} from "./icons";
import {
  HashRouter,
  Redirect,
  Route,
  RouteComponentProps,
  Switch,
  withRouter
} from 'react-router-dom';
import {StaticContext} from "react-router";
import {TabProps} from "@material-ui/core/Tab/Tab";

interface AppState {
  readings: DeviceEvent[]
  alarmStatus?: AlarmStatus
}

enum Routes {
  ALARM = "/alarm",
  READINGS = "/readings"
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
          <HashRouter>
            <div>
              <AppBar position="static">
                <TabsWithRouter>
                  <TabWithRouter
                      label="Alarm"
                      icon={<AlarmIcon className="menu-icon"/>}
                      url={Routes.ALARM}
                  />
                  <TabWithRouter
                      label="Readings"
                      icon={<ReadingsIcon className="menu-icon"/>}
                      url={Routes.READINGS}
                  />
                </TabsWithRouter>
              </AppBar>
              {renderRoutes(this.state.readings, this.state.alarmStatus)}
            </div>
          </HashRouter>
        </MuiThemeProvider>
    );
  }
}

const TabWithRouter = withRouter<{ url: string } & RouteComponentProps<any, StaticContext> & TabProps>((props) => {
      // router not ts friendly, need wrap tab component...
      // also...
      // cannot into lambda cauze perf issua!!!!oneoneone
      function _onClickHandler() {
        props.history.push(props.url)
      }

      return <Tab
          {...props}
          onClick={_onClickHandler}
      />
    }
);

function determineCurrentTab(pathname: string): number | undefined {
  switch (pathname) {
    case Routes.ALARM:
      return 0;
    case Routes.READINGS:
      return 1;
    default:
      return undefined;
  }
}

const TabsWithRouter = withRouter<RouteComponentProps<any, StaticContext> & TabsProps>((props) =>
    <Tabs
        value={determineCurrentTab(props.location.pathname)}
    >
      {props.children}
    </Tabs>
);

const renderRoutes = (readings: DeviceEvent[], alarmStatus?: AlarmStatus) =>
    <Switch>
      <Route
          path={`${Routes.ALARM}`}
          component={renderAlarmView.bind(null, alarmStatus)}
      />
      <Route
          path={`${Routes.READINGS}`}
          component={renderReadingView.bind(null, readings)}
      />
      <Redirect exact={true} from="/" to={`${Routes.ALARM}`}/>
      <Route component={NotFoundView}/>
    </Switch>
;

const renderAlarmView = (props?: AlarmStatus) =>
    <Paper style={{margin: "1em", padding: "1em"}}>
      {!props && (
          <span>No alarm state?</span>
      )}
      {props && (
          <Alarm status={props}/>
      )}
    </Paper>;

const renderReadingView = (readings: DeviceEvent[]) =>
    <div style={{padding: "1em"}}>
      <Readings readings={readings}/>
    </div>;

const NotFoundView: React.SFC = () =>
    <Paper style={{margin: "1em", padding: "1em", fontSize: "2em"}}>
      <span>Here comes nothing... <br/><br/>404 error</span>
    </Paper>;

export default App;
