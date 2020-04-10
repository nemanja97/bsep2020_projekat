import React, { useState, useEffect } from 'react';
import { useParams, useHistory } from 'react-router-dom';
import { CertificateService } from '../services/CertificateService';

function CertificateDisplay() {
    const [certificate, setCertificate] = useState(
        {
            id: "",
            commonName: "",
            alias: "",
            issuerAlias: "",
            organization: "",
            organizationUnit: "",
            country: "",
            email: "",
            template: "",
        }
    );
    const params = useParams();
    const history = useHistory();
    useEffect(() => {
        CertificateService.get(params.id)
            .then(response => setCertificate(response.data))
    }, [])

    const handleChange = (change) => (event) => {
        return;
    };

    const [reason, setReason] = useState('unspecified')

    const handleSubmit = (event) => {
        event.preventDefault();
        CertificateService.revoke(certificate.id, reason)
            .then(() => history.push('/pki'));
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
                    <h1 className="display-4">Certificate details</h1>
                </div>
            </div>
            <div className="row">
                <div className="col-md-6 offset-md-3">
                    <form onSubmit={handleSubmit}>
                        <div className="form-row">
                            <div className="col">
                                <div className="form-group">
                                    <label htmlFor="common-name">Common name</label>
                                    <input disabled value={certificate.commonName} type="text" className="form-control" id="common-name" placeholder="Common name" onChange={handleChange("commonName")} required />
                                </div>
                            </div>
                            <div className="col">
                                <div className="form-group">
                                    <label htmlFor="alias">Alias</label>
                                    <input disabled value={certificate.alias} type="text" className="form-control" id="alias" placeholder="Alias" onChange={handleChange("alias")} required />
                                </div>
                            </div>
                        </div>
                        <div className="form-row">
                            <div className="col">
                                <div className="form-group">
                                    <label htmlFor="organization">Organization</label>
                                    <input disabled value={certificate.organization} type="text" className="form-control" id="organization" placeholder="Organization" onChange={handleChange("organization")} required />
                                </div>
                            </div>
                            <div className="col">
                                <div className="form-group">
                                    <label htmlFor="organization-unit">Organization unit</label>
                                    <input disabled value={certificate.organizationUnit} type="text" className="form-control" id="organization-unit" placeholder="Organization unit" onChange={handleChange("organizationUnit")} required />
                                </div>
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="country">Country ISO-code</label>
                            <input disabled value={certificate.country} type="text" className="form-control" id="country" placeholder="Country ISO-code" onChange={handleChange("country")} required />
                        </div>
                        <div className="form-group">
                            <label htmlFor="contact-email">Contact email</label>
                            <input disabled value={certificate.email} type="email" className="form-control" id="contact-email" placeholder="Contact email" onChange={handleChange("email")} required />
                        </div>

                        <div className="form-group">
                            <label htmlFor="template">Template</label>
                            <select disabled id="template" value={certificate.template} onChange={handleChange("template")} className="form-control">
                                <option value="INTERMEDIATE_CA">Intermediate CA</option>
                                <option value="TLS_SERVER">TLS server certificate</option>
                                <option value="SIEM_CENTER">SIEM center</option>
                                <option value="SIEM_AGENT">SIEM agent</option>
                            </select>
                        </div>
                        <div className="form-group">
                            <label htmlFor="reason">Reason</label>
                            <select id="reason" value={reason} onChange={(e) => setReason(e.target.value)} className="form-control">
                                <option value="unspecified">unspecified</option>
                                <option value="keyCompromise">keyCompromise</option>
                                <option value="cACompromise">cACompromise</option>
                                <option value="affiliationChanged">affiliationChanged</option>
                                <option value="superseded">superseded</option>
                                <option value="cessationOfOperation">cessationOfOperation</option>
                                <option value="certificateHold">certificateHold</option>
                                <option value="removeFromCRL">removeFromCRL</option>
                                <option value="privilegeWithdrawn">privilegeWithdrawn</option>
                                <option value="aACompromise">aACompromise</option>
                            </select>
                        </div>
                        <button type="submit" className="btn btn-danger">Revoke certificate</button>
                    </form>
                </div>
            </div>
        </div>
    )
}

export default CertificateDisplay;