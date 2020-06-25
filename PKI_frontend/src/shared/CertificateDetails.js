import React, { useState } from "react";

const CertificateDetails = () => {

    return (
        <div className=".container">
            <form>
                <div className="form-group">
                    <label for="common-name">Common name</label>
                    <input type="text" className="form-control" id="common-name" placeholder="Common name" required/>
                </div>
                <div className="form-row">
                    <div class="col">
                        <div className="form-group">
                            <label for="organization">Organization</label>
                            <input type="text" className="form-control" id="organization" placeholder="Organization" required/>
                        </div>
                    </div>
                    <div class="col">
                        <div className="form-group">
                            <label for="organization-unit">Organization unit</label>
                            <input type="text" className="form-control" id="organization-unit" placeholder="Organization unit" required/>
                        </div>
                    </div>
                </div>
               
                <div className="form-group">
                    <label for="country">Country ISO-code</label>
                    <input type="text" className="form-control" id="country" placeholder="Country ISO-code" required/>
                </div>
                <div className="form-group">
                    <label for="contact-email">Contact email</label>
                    <input type="email" className="form-control" id="contact-email" placeholder="Contact email" required/>
                </div>
               
                <div className="form-check">
                    <input className="form-check-input" type="checkbox" value="" id="isCA"/>
                    <label className="form-check-label" for="isCA">
                        CA
                    </label>
                </div>
            </form>
        </div>
    )
}

export default CertificateDetails;