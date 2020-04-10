import axios from "axios"
import moment from 'moment'
import qs from "querystring"
import jwt_decode from 'jwt-decode'

export const CertificateService = {
    createCertificate,
    getAll,
    get
}

function createCertificate(certificate) {
    return axios.post(`${process.env.REACT_APP_API_URL}/v1/certificates/create`, certificate)
        .then(response => {
            console.log(response);
        })
}

function getAll(){
    return axios.get(`${process.env.REACT_APP_API_URL}/v1/certificates`);
}

function get(id){
    return axios.get(`${process.env.REACT_APP_API_URL}/v1/certificates/${id}`);
}