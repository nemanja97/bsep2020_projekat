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
import RulesDashboard from './siemcenter/RulesDashboard';


function App() {
  return (
    <Router>
      <Switch>
        <Route path='/(|login)' component={Login}/>
        <PrivateRoute exact path='/pki' component={PKIHome} roles={['PKI admin']}/>
        <PrivateRoute exact path='/pki/:id/addCertificate' component={PKIAddCertificate} roles={['PKI admin']}/>
        <PrivateRoute exact path='/pki/:id/' component={CertificateDisplay} roles={['PKI admin']}/>
        <PrivateRoute exact path='/siemcenter' component={SIEMCenterHome} roles={['SIEM center admin', 'SIEM center operator']}/>
        <PrivateRoute exact path='/siemcenter/rules' component={RulesDashboard} roles={['SIEM center admin']}/>
      </Switch>
    </Router>
  );
}

export default App;
