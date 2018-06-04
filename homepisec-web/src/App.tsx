import * as React from 'react';

import './App.css';

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
import {Redirect, Route, RouteComponentProps, Switch, withRouter} from 'react-router-dom';
import {StaticContext} from "react-router";
import {TabProps} from "@material-ui/core/Tab/Tab";

import {connect} from 'react-redux'
import {createStore, Dispatch} from 'redux'
import {AppState} from "./model";
import {getAlarmStatus, getReadingsAction} from "./actions";


enum Routes {
  ALARM = "/alarm",
  READINGS = "/readings"
}

interface DispatchProps {
  getReadings(): void
  getAlarmStatus(): void
}

type AppProps = {} & AppState & DispatchProps;

class App extends React.Component<AppProps, {}> {

  componentDidMount() {
    this.props.getReadings();
    this.props.getAlarmStatus();
  };

  public render() {
    return (
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
          {renderRoutes(this.props.readings, this.props.alarmStatus)}
        </div>
    );
  }
}

const TabWithRouter = withRouter<{ url: string } & RouteComponentProps<any, any> & TabProps>((props) => {
      // router not ts friendly, need wrap tab component...
      // also...
      // cannot into lambda cauze perf issua!!!!oneoneone
      function _onClickHandler() {
        props.history.push(props.url)
      }

      return <Tab
          label={props.label}
          icon={props.icon}
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

const TabsWithRouter = withRouter<RouteComponentProps<any, any> & TabsProps>((props) =>
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

const mapStateToProps = (state: AppState, ownProps: AppState) => state;

const mapDispatchToProps = (dispatch: any):DispatchProps => {
  return {
    getAlarmStatus: () => dispatch(getAlarmStatus()),
    getReadings: () => dispatch(getReadingsAction())
  }
};

export default connect<AppState, DispatchProps>(mapStateToProps, mapDispatchToProps)(App);
