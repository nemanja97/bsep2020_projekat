import axios from 'axios';

export const CertificateService = {
    get
}

function get(){
    return axios.get(`${process.env.REACT_APP_API_URL}/v1/certificates`);
}