import axios from "axios"
import jwt_decode from 'jwt-decode'
import qs from "querystring"

export const AuthenticationService = {
    authenticate,
    refreshToken
}

function authenticate(userCredentials){
    userCredentials = {
        ...userCredentials,
        client_id:"PKI",
        grant_type:"password",
        client_secret: `${process.env.REACT_APP_SECRET_KEY}`
        }
    const config = {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    }
    axios.post(`${process.env.REACT_APP_AUTH_URL}`, qs.stringify(userCredentials), config)
        .then(response => {
            updateStorageToken(response)
        })

}

function refreshToken(originalRequest){
    let requestData = {
        refresh_token: JSON.parse(localStorage.getItem("session")).refreshToken,
        client_id:"PKI",
        grant_type : "refresh_token",
        client_secret: `${process.env.REACT_APP_SECRET_KEY}`
    }
    const config = {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    }
    axios.post(`${process.env.REACT_APP_AUTH_URL}`, qs.stringify(requestData), config)
            .then(response => {
               if (response.status === 201) {
                   updateStorageToken(response)
                   return axios(originalRequest);
               }
           })
}

function updateStorageToken(response){
    let decodedToken = jwt_decode(response.data.access_token);
    let sessionInfo = {
        token: response.data.access_token,
        tokenExpiration: response.data.expires_in,
        refreshToken: response.data.refresh_token,
        refreshTokenExpiration: response.data.refresh_expires_in,
        roles: decodedToken.realm_access.roles
    }
    localStorage.setItem("session", JSON.stringify(sessionInfo));
}