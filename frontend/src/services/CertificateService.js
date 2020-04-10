import axios from "axios"
import moment from 'moment'
import qs from "querystring"
import jwt_decode from 'jwt-decode'

export const CertificateService = {
    createCertificate
}


function createCertificate(certificate){
        return axios.post(`${process.env.REACT_APP_API_URL}/v1/certificates/leaf`, qs.stringify(certificate))
        .then(response => {
            console.log(response);
        })
}