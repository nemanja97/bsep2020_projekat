import axios from 'axios';

export const CertificateService = {
    getAll,
    get
}

function getAll(){
    return axios.get(`${process.env.REACT_APP_API_URL}/v1/certificates`);
}

function get(id){
    return axios.get(`${process.env.REACT_APP_API_URL}/v1/certificates/${id}`);
}