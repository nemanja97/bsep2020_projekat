import React from "react";
import { Link, useHistory } from "react-router-dom";
import { AuthenticationService } from "../services/AuthenticationService";

export default function PKINavbar() {
  const history = useHistory();

  return (
    <nav className="navbar" role="navigation" aria-label="main navigation">
      <div className="navbar-brand">
        <Link className="navbar-item" to="/">
          <i className="fas fa-home"></i>
        </Link>
      </div>

      <div id="navbarBasicExample" className="navbar-menu">
        <div className="navbar-start">
          <Link className="navbar-item" to="/pki">
            Dashboard
          </Link>
          <Link className="navbar-item" to="/requests">
            Requests
          </Link>
        </div>

        <div className="navbar-end">
          <div className="navbar-item">
            <div className="buttons">
              <Link
                className="button is-primary"
                onClick={() => {
                  AuthenticationService.purgeToken();
                  history.push("/login");
                }}
              >
                Log out
              </Link>
            </div>
          </div>
        </div>
      </div>
    </nav>
  );
}
