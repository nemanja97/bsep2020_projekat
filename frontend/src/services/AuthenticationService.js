import axios from "axios"
import moment from 'moment'
import qs from "querystring"
import jwt_decode from 'jwt-decode'

export const AuthenticationService = {
    authenticate,
    refreshToken,
    checkTokensExp
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
    return axios.post(`${process.env.REACT_APP_AUTH_URL}`, qs.stringify(userCredentials), config)
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
    return axios.post(`${process.env.REACT_APP_AUTH_URL}`, qs.stringify(requestData), config)
            .then(response => {
               if (response.status === 200) {
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

function checkTokensExp(){
    const dateNow = moment();
    const session = JSON.parse(localStorage.getItem("session"));
    const refreshDecoded = jwt_decode(session.refreshToken);
    const refreshExp = refreshDecoded.exp;
    const refreshDate = moment(1000 * refreshExp);
    if (dateNow.isAfter(refreshDate)){
        purgeToken();
        window.location.reload();
    }
    const accessDecoded = jwt_decode(session.token);
    const accessExp = accessDecoded.exp;
    const accessDate = moment(1000 * accessExp);
    return dateNow.isAfter(accessDate);
}

function purgeToken(){
    localStorage.removeItem("session");
}