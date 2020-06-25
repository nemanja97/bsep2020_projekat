import React, { useState, useEffect } from "react";
import { useParams, useHistory } from "react-router-dom";

import { CertificateService } from "../services/CertificateService";
import { ExpirableLinksService } from "../services/ExpirableLinksService";
import FileSaver, { saveAs } from "file-saver";

function CSR() {
  const [certificate, setCertificate] = useState({
    commonName: "",
    alias: "",
    issuerAlias: "PKI",
    organization: "",
    organizationUnit: "",
    country: "",
    email: "",
    template: "SIEM_AGENT",
  });
  const [error, setError] = useState({
    message: "",
    show: "hidden",
  });
  const params = useParams();
  const history = useHistory();

  useEffect(() => {
    console.log("Error");
    ExpirableLinksService.isValidLink(params.id).catch(() =>
      setCertificate(null)
    );
  }, []);

  const handleChange = (change) => (event) => {
    const val = event.target.value;
    setCertificate({ ...certificate, [change]: val });
  };

  const handleSubmit = () => {
    certificate.issuerAlias = params.id;
    console.log(certificate);

    CertificateService.createCSRCertificate(certificate, params.id).then(
      (response) => {
        console.log(response);
        FileSaver.saveAs(
          new Blob([response.data], { type: "application/zip" }),
          "data.zip"
        );
      }
    );
  };

  return (
    <>
      {certificate === null && (
        <section class="hero">
          <div class="hero-body">
            <div class="container">
              <h1 class="title">Invalid link</h1>
              <h2 class="subtitle">Check in with your administrators</h2>
            </div>
          </div>
        </section>
      )}
      {certificate != null && (
        <div className="container-fluid">
          <div className="row mt-3">
            <div className="col-md-6 offset-md-3">
              <button
                onClick={() => history.push("/pki")}
                className="btn btn-primary"
              >
                Go back
              </button>
            </div>
          </div>
          <div className="row">
            <div className="col-md-6 offset-md-3">
              <h1 className="display-4">Create new certificate</h1>
            </div>
          </div>
          <div className="row">
            <div className="col-md-6 offset-md-3">
              <div className="form-row">
                <div className="col">
                  <div className="form-group">
                    <label htmlFor="common-name">Common name</label>
                    <input
                      type="text"
                      className="form-control"
                      id="common-name"
                      placeholder="Common name"
                      onChange={handleChange("commonName")}
                      required
                    />
                  </div>
                </div>
                <div className="col">
                  <div className="form-group">
                    <label htmlFor="alias">Alias</label>
                    <input
                      type="text"
                      className="form-control"
                      id="alias"
                      placeholder="Alias"
                      onChange={handleChange("alias")}
                      required
                    />
                  </div>
                </div>
              </div>
              <div className="form-row">
                <div className="col">
                  <div className="form-group">
                    <label htmlFor="organization">Organization</label>
                    <input
                      type="text"
                      className="form-control"
                      id="organization"
                      placeholder="Organization"
                      onChange={handleChange("organization")}
                      required
                    />
                  </div>
                </div>
                <div className="col">
                  <div className="form-group">
                    <label htmlFor="organization-unit">Organization unit</label>
                    <input
                      type="text"
                      className="form-control"
                      id="organization-unit"
                      placeholder="Organization unit"
                      onChange={handleChange("organizationUnit")}
                      required
                    />
                  </div>
                </div>
              </div>
              <div className="form-group">
                <label htmlFor="country">Country ISO-code</label>
                <input
                  type="text"
                  className="form-control"
                  id="country"
                  placeholder="Country ISO-code"
                  onChange={handleChange("country")}
                  minLength="2"
                  maxLength="2"
                  style={{ textTransform: "uppercase" }}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="contact-email">Contact email</label>
                <input
                  type="email"
                  className="form-control"
                  id="contact-email"
                  placeholder="Contact email"
                  onChange={handleChange("email")}
                  required
                />
              </div>

              <button
                onClick={() => handleSubmit()}
                className="btn btn-primary"
              >
                Create certificate
              </button>
              <div
                style={{
                  fontSize: "24px",
                  color: "red",
                  visibility: error.show,
                }}
              >
                {error.message}
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
}

export default CSR;
