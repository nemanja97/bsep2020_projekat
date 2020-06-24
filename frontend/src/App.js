import React from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route,
} from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';
import { PrivateRoute } from './shared/PrivateRoute'
import Login from './shared/Login';
import PKIHome from './pki/PKIHome';
import PKIAddCertificate from './pki/PKIAddCertificate';
import CertificateDisplay from './pki/CertificateDisplay';
import SIEMCenterHome from './siemcenter/SIEMCenterHome';
import ReportsDashboard from './siemcenter/ReportsDashboard';
import RulesDashboard from './siemcenter/RulesDashboard';
import ModifyRule from './siemcenter/ModifyRule';
import AddRule from './siemcenter/AddRule';
import BlacklistsDashboard from './siemcenter/BlacklistsDashboard';
import WhitelistsDashboard from './siemcenter/WhitelistsDashboard';
import Whitelist from './siemcenter/Whitelist';
import Blacklist from './siemcenter/Blacklist';


function App() {
  return (
    <Router>
      <Switch>
        <Route path='/(|login)' component={Login}/>
        <PrivateRoute exact path='/pki' component={PKIHome} roles={['PKI admin']}/>
        <PrivateRoute exact path='/pki/:id/addCertificate' component={PKIAddCertificate} roles={['PKI admin']}/>
        <PrivateRoute exact path='/pki/:id/' component={CertificateDisplay} roles={['PKI admin']}/>
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
