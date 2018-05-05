import AppBar from 'material-ui/AppBar';
import Drawer from 'material-ui/Drawer';
import MenuItem from 'material-ui/MenuItem';
import * as React from 'react';

interface IMenuState {
    drawerOpen: boolean
}

export class Menu extends React.Component<{}, IMenuState> {

    public state: IMenuState = {
        drawerOpen: false
    };

    public render() {
        return (
            <div>
                <AppBar
                    title="Title"
                    iconClassNameRight="muidocs-icon-navigation-expand-more"
                    onLeftIconButtonClick={this.toggleDrawwer}
                />
                <Drawer open={this.state.drawerOpen}>
                    <MenuItem onClick={this.menuItemClick}>Readings</MenuItem>
                    <MenuItem onClick={this.menuItemClick}>Alarm</MenuItem>
                </Drawer>
            </div>
        )
    }

    private toggleDrawwer = () => {
        this.setState({drawerOpen: !this.state.drawerOpen})
    };

    private menuItemClick = () => {
        this.toggleDrawwer();
    }

}
