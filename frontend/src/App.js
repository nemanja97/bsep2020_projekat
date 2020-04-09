import React from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route,
} from "react-router-dom";
import { PrivateRoute } from './shared/PrivateRoute'
import Login from './shared/Login';
import PKIHome from './pki/PKIHome';


function App() {
  return (
    <Router>
      <Switch>
        <Route path='/login' component={Login}/>
        <PrivateRoute exact path='/pki' component={PKIHome}/>
      </Switch>
    </Router>
  );
}

export default App;
