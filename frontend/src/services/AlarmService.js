import axios from "axios"
import moment from 'moment'
import qs from "querystring"
import jwt_decode from 'jwt-decode'

export const AlarmService = {
    getNewestAlarms
}

function getNewestAlarms() {
    return axios.get(`https://localhost:8044/api/v1/alarms`).then(r => console.log(r));
}