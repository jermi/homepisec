import * as React from 'react';
import * as ReactDOM from 'react-dom';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import App from './App';
import './index.css';
import registerServiceWorker from './registerServiceWorker';

import {Provider} from 'react-redux'
import {applyMiddleware, compose, createStore} from 'redux'
import {initialState} from "./model";
import {rootReducer} from "./reducers";
import thunk from "redux-thunk";
import {ConnectedRouter, routerMiddleware} from 'react-router-redux';
import createHistory from 'history/createHashHistory'

// tslint:disable-next-line
const composeEnhancers = window['__REDUX_DEVTOOLS_EXTENSION_COMPOSE__'] || compose;

const history = createHistory();
const store = createStore(
    rootReducer,
    initialState,
    composeEnhancers(
        applyMiddleware(thunk, routerMiddleware(history))
    )
);

ReactDOM.render(
    <MuiThemeProvider>
      <Provider store={store}>
        <ConnectedRouter history={history}>
          <App/>
        </ConnectedRouter>
      </Provider>
    </MuiThemeProvider>,
    document.getElementById('root') as HTMLElement
);
registerServiceWorker();
