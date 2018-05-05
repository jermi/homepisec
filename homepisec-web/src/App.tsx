import * as React from 'react';

import './App.css';

import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import {Menu} from "./components/Menu";
import {Readings} from "./components/Readings";
import {Settings} from "./components/Settings";

class App extends React.Component {

    public render() {
        return (
            <MuiThemeProvider>
                <div>
                    <Menu/>
                    <Settings/>
                    <Readings/>
                </div>
            </MuiThemeProvider>
        );
    }

}

export default App;
