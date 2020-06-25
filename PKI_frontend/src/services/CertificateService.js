import axios from "axios"

export const CertificateService = {
    createCertificate,
    getAll,
    get,
    revoke,
    download
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

function revoke(id, reason){
    return axios.delete(`${process.env.REACT_APP_API_URL}/v1/certificates/${id}?reason=${reason}`);
}

function download(id){
    return axios.get(`${process.env.REACT_APP_API_URL}/v1/certificates/${id}?format=pem_key`, {responseType: "arraybuffer"});
}