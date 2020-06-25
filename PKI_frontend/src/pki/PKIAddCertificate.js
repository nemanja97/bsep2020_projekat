import React, { useState } from 'react';
import { useParams, useHistory } from 'react-router-dom';

import { CertificateService } from '../services/CertificateService';

function PKIAddCertificate (){

    const [certificate, setCertificate] = useState(
        {
            commonName: "",
            alias: "",
            issuerAlias: "",
            organization: "",
            organizationUnit: "",
            country: "",
            email: "",
            template: "INTERMEDIATE_CA",
        }
    );
    const [error, setError] = useState({
        message:"",
        show: "hidden"
    });
    const params = useParams();
    const history = useHistory();

    const handleChange = (change) => (event) => {
        const val = event.target.value;
        setCertificate({ ...certificate, [change]: val });
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        certificate.issuerAlias = params.id;
        console.log(certificate);

        CertificateService.createCertificate(certificate)
            .then(() => history.push('/pki'))
            .catch((error) => setError({show: "visible", message:error.response.data.violations[0].message}))
    }

    return (
        <div className="container-fluid">
            <div className="row mt-3">
                <div className="col-md-6 offset-md-3">
                    <button onClick={() => history.push('/pki')} className="btn btn-primary">Go back</button>
                </div>
            </div>
            <div className="row">
                <div className="col-md-6 offset-md-3">
                    <h1 className="display-4">Create new certificate</h1>
                </div>
            </div>
            <div className="row">
                
                <div className="col-md-6 offset-md-3">
                    <form onSubmit={handleSubmit}>
                        <div className="form-row">
                            <div className="col">
                                <div className="form-group">
                                    <label htmlFor="common-name">Common name</label>
                                    <input type="text" className="form-control" id="common-name" placeholder="Common name" onChange={handleChange("commonName")} required/>
                                </div>
                            </div>
                            <div className="col">
                                <div className="form-group">
                                    <label htmlFor="alias">Alias</label>
                                    <input type="text" className="form-control" id="alias" placeholder="Alias" onChange={handleChange("alias")} required/>
                                </div>
                            </div>
                        </div>
                        <div className="form-row">
                            <div className="col">
                                <div className="form-group">
                                    <label htmlFor="organization">Organization</label>
                                    <input type="text" className="form-control" id="organization" placeholder="Organization" onChange={handleChange("organization")} required/>
                                </div>
                            </div>
                            <div className="col">
                                <div className="form-group">
                                    <label htmlFor="organization-unit">Organization unit</label>
                                    <input type="text" className="form-control" id="organization-unit" placeholder="Organization unit" onChange={handleChange("organizationUnit")} required/>
                                </div>
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="country">Country ISO-code</label>
                            <input type="text" className="form-control" id="country" placeholder="Country ISO-code" onChange={handleChange("country")} minLength="2" maxLength="2" style={{textTransform:"uppercase"}} required/>
                        </div>
                        <div className="form-group">
                            <label htmlFor="contact-email">Contact email</label>
                            <input type="email" className="form-control" id="contact-email" placeholder="Contact email" onChange={handleChange("email")} required/>
                        </div>

                        <div className="form-group">
                            <label htmlFor="template">Template</label>
                            <select id="template" value={certificate.template} onChange={handleChange("template")} className="form-control">
                                <option value="INTERMEDIATE_CA">Intermediate CA</option>
                                <option value="TLS_SERVER">TLS server certificate</option>
                                <option value="SIEM_CENTER">SIEM center</option>
                                <option value="SIEM_AGENT">SIEM agent</option>
                            </select>
                        </div>
                        <button type="submit" className="btn btn-primary">Create certificate</button>
                    </form>
                    <div style={{fontSize:"24px", color:"red", visibility: error.show}}>{error.message}</div>
                </div>
            </div>
        </div>
    )
}

export default PKIAddCertificate;