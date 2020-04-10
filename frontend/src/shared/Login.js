import React, { useState } from 'react';
import { AuthenticationService } from '../services/AuthenticationService'

function Login() {
    const [user, setUser] = useState({ username: "", password: "" });

    const handleChange = (name) => (event) => {
        const val = event.target.value;
        setUser({ ...user, [name]: val });
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        AuthenticationService.authenticate(user);
    }
    
    return (
        <div>
            <form onSubmit={handleSubmit}>
                <input type="text" name="username" onChange={handleChange("username")}></input>
                <input type="password" name="password" onChange={handleChange("password")}></input>
                <button type="submit">Submit</button>
            </form>
        </div>
    )
}

export default Login;