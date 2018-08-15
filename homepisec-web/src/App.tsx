import * as React from 'react';
import {MouseEvent} from 'react';

import './App.css';

import {Readings} from "./components/Readings";
import {Alarm} from "./components/Alarm";
import {AlarmStatus} from "./generated/control-api"
import AppBar from '@material-ui/core/AppBar';
import Tabs, {TabsProps} from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import {Paper} from "@material-ui/core";
import {AlarmIcon, ReadingsIcon} from "./icons";
import {Redirect, Route, RouteComponentProps, Switch, withRouter} from 'react-router-dom';
import {TabProps} from "@material-ui/core/Tab/Tab";

import {connect} from 'react-redux'
import {AppState} from "./model";
import {getAlarmStatus, getReadingsAction, switchRelayAction} from "./actions";
import Timer = NodeJS.Timer;


enum Routes {
  ALARM = "/alarm",
  READINGS = "/readings"
}

interface DispatchProps {
  getReadings(): void

  getAlarmStatus(): void

  switchRelay(relayId: string, value: boolean): (event: MouseEvent<HTMLElement>) => void | undefined
}

type AppProps = {} & AppState & DispatchProps;

interface AppStateInternal {
  interval: Timer
}

class App extends React.Component<AppProps, AppStateInternal> {

  componentDidMount() {
    const props = this.props;
    const interval: Timer = setInterval(function refreshReadings() {
      props.getReadings();
      props.getAlarmStatus();
    }, 2000);
    this.setState({interval})
  };

  componentWillUnmount() {
    clearInterval(this.state.interval);
  }

  public render() {
    return (
        <div>
          <AppBar position="static">
            <TabsWithRouter value={null}>
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
          {renderRoutes(this.props)}
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

type TabsWithRouterProps = RouteComponentProps<any, any> & TabsProps;

const TabsWithRouter = withRouter<TabsWithRouterProps>((props: TabsWithRouterProps) =>
    <Tabs
        value={determineCurrentTab(props.location.pathname)}
    >
      {props.children}
    </Tabs>
);

const renderRoutes = (props: AppProps) =>
    <Switch>
      <Route
          path={`${Routes.ALARM}`}
          component={renderAlarmView.bind(null, props.alarmStatus)}
      />
      <Route
          path={`${Routes.READINGS}`}
          component={renderReadingView.bind(null, props)}
      />
      <Redirect exact={true} from="/" to={`${Routes.ALARM}`}/>
      <Route component={NotFoundView}/>
    </Switch>
;

const renderAlarmView = (alarmStatus?: AlarmStatus) =>
    <Paper style={{margin: "1em", padding: "1em"}}>
      {!alarmStatus && (
          <span>No alarm state?</span>
      )}
      {alarmStatus && (
          <Alarm status={alarmStatus}/>
      )}
    </Paper>;

const renderReadingView = (props: AppProps) =>
    <div style={{padding: "1em"}}>
      <Readings readings={props.readings} switchRelay={props.switchRelay}/>
    </div>;

const NotFoundView: React.SFC = () =>
    <Paper style={{margin: "1em", padding: "1em", fontSize: "2em"}}>
      <span>Here comes nothing... <br/><br/>404 error</span>
    </Paper>;

const mapStateToProps = (state: AppState, ownProps: AppState) => state;

const mapDispatchToProps = (dispatch: any): DispatchProps => {
  return {
    getAlarmStatus: () => dispatch(getAlarmStatus()),
    getReadings: () => dispatch(getReadingsAction()),
    switchRelay: (relayId: string, value: boolean) => dispatch(switchRelayAction(relayId, value))
  }
};

export default connect<AppState, DispatchProps>(mapStateToProps, mapDispatchToProps)(App);
