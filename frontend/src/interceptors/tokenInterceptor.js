import axios from 'axios'
import { AuthenticationService } from '../services/AuthenticationService'

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
            if (AuthenticationService.checkTokensExp() && !originalRequest._retry) {
                originalRequest._retry = true;
                return AuthenticationService.refreshToken(originalRequest);
            }
            return Promise.reject(error);
        })
}