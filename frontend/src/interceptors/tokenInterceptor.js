import axios from 'axios'
import moment from 'moment'
import { AuthenticationService } from '../services/AuthenticationService'
import jwt_decode from 'jwt-decode'

export const setupInterceptors = () => {
    axios.interceptors.request.use((config) => {
        if(config.url !== `${process.env.REACT_APP_AUTH_URL}`){
            config.headers.Authorization = `Bearer ${JSON.parse(localStorage.getItem('session')).token}`
        }
        return config
    })

    axios.interceptors.response.use((response) => {
        return response
    },
        function (error) {
            const originalRequest = error.config;
            let dateNow = moment();
            let tokenExp = jwt_decode(JSON.parse(localStorage.getItem("session")).token).exp;
            let tokenDate = moment(1000 * tokenExp);
            debugger
            if (dateNow.isAfter(tokenDate) && !originalRequest._retry) {
                originalRequest._retry = true;
                return AuthenticationService.refreshToken(originalRequest);
            }
            return Promise.reject(error);
        })
}