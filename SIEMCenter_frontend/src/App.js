import React from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route,
} from "react-router-dom";
import { PrivateRoute } from './shared/PrivateRoute'
import Login from './shared/Login';
import SIEMCenterHome from './siemcenter/SIEMCenterHome';
import ReportsDashboard from './siemcenter/reports/ReportsDashboard';
import RulesDashboard from './siemcenter/rules/RulesDashboard';
import ModifyRule from './siemcenter/rules/ModifyRule';
import AddRule from './siemcenter/rules/AddRule';
import BlacklistsDashboard from './siemcenter/blacklists/BlacklistsDashboard';
import WhitelistsDashboard from './siemcenter/whitelists/WhitelistsDashboard';
import Whitelist from './siemcenter/whitelists/Whitelist';
import Blacklist from './siemcenter/blacklists/Blacklist';


function App() {
  return (
    <Router>
      <Switch>
        <Route path='/(|login)' component={Login}/>
        <PrivateRoute exact path='/siemcenter' component={SIEMCenterHome} roles={['SIEM center admin', 'SIEM center operator']}/>
        <PrivateRoute exact path='/siemcenter/reports' component={ReportsDashboard} roles={['SIEM center admin', 'SIEM center operator']}/>
        <PrivateRoute exact path='/siemcenter/rules' component={RulesDashboard} roles={['SIEM center admin']}/>
        <PrivateRoute exact path='/siemcenter/rule' component={AddRule} roles={['SIEM center admin']}/>
        <PrivateRoute exact path='/siemcenter/rule/:id' component={ModifyRule} roles={['SIEM center admin']}/>
        <PrivateRoute exact path='/siemcenter/whitelists' component={WhitelistsDashboard} roles={['SIEM center admin']}/>
        <PrivateRoute exact path='/siemcenter/whitelist' component={Whitelist} roles={['SIEM center admin']}/>
        <PrivateRoute exact path='/siemcenter/whitelist/:id' component={Whitelist} roles={['SIEM center admin']}/>
        <PrivateRoute exact path='/siemcenter/blacklists' component={BlacklistsDashboard} roles={['SIEM center admin']}/>
        <PrivateRoute exact path='/siemcenter/blacklist' component={Blacklist} roles={['SIEM center admin']}/>
        <PrivateRoute exact path='/siemcenter/blacklist/:id' component={Blacklist} roles={['SIEM center admin']}/>
      </Switch>
    </Router>
  );
}

export default App;
