import React, { useState } from 'react';
import { AuthenticationService } from '../services/AuthenticationService'
import { useHistory } from 'react-router-dom';
import {Card} from 'react-bootstrap'

function Login() {
    const [user, setUser] = useState({ username: "", password: "" });
    const history = useHistory();

    const handleChange = (name) => (event) => {
        const val = event.target.value;
        setUser({ ...user, [name]: val });
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        AuthenticationService.authenticate(user)
            .then(() => history.push("/pki"))
            .catch(() => {});
    }
    
    return (
        <div className="container" style={{marginTop:"15%"}}>
            <div className="row text-center justify-content-center">
                <div className="col-md-6">
                    <Card>
                        <Card.Body>
                            <form onSubmit={handleSubmit}>
                                <div className="form-group">
                                    <label html-for="username">Username</label>
                                    <input className="form-control" type="text" name="username" onChange={handleChange("username")}/>
                                </div>
                                <div className="form-group">
                                    <label html-for="password">Password</label>
                                    <input className="form-control" type="password" name="password" onChange={handleChange("password")}/>
                                </div>
                                <button type="submit" className="btn btn-primary">Login</button>
                            </form>
                        </Card.Body>
                    </Card>
                </div>
            </div>
        </div>
    )
}

export default Login;