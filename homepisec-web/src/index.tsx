import * as React from 'react';
import * as ReactDOM from 'react-dom';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import App from './App';
import './index.css';
import registerServiceWorker from './registerServiceWorker';

import {Provider} from 'react-redux'
import {applyMiddleware, combineReducers, createStore} from 'redux'
import {initialState} from "./model";
import {rootReducer} from "./reducers";
import thunk from "redux-thunk";
import {ConnectedRouter, routerMiddleware, routerReducer} from 'react-router-redux';
import createHistory from 'history/createHashHistory'

const history = createHistory();
const store = createStore(
    rootReducer,
    initialState,
    applyMiddleware(thunk, routerMiddleware(history))
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
