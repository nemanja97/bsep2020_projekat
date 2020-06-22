import React from "react";
import { Link } from "react-router-dom";

export default function SIEMCenterNavbar() {
  const roles = JSON.parse(localStorage.getItem("session"))["roles"];

  return (
    <nav className="navbar" role="navigation" aria-label="main navigation">
      <div className="navbar-brand">
        <Link className="navbar-item" to="/">
          <i className="fas fa-home"></i>
        </Link>
      </div>

      <div id="navbarBasicExample" className="navbar-menu">
        <div className="navbar-start">
          <Link className="navbar-item" to="/siemcenter">
            Dashboard
          </Link>
          {roles.includes("SIEM center admin") && (
            <Link className="navbar-item" to="/siemcenter/rules">
              Modify rules
            </Link>
          )}
          {roles.includes("SIEM center admin") && (
            <Link className="navbar-item" to="/siemcenter/whitelists">
              Modify whitelists
            </Link>
          )}
          {roles.includes("SIEM center admin") && (
            <Link className="navbar-item" to="/siemcenter/blacklists">
              Modify blacklists
            </Link>
          )}
        </div>

        <div className="navbar-end">
          <div className="navbar-item">
            <div className="buttons">
              <Link className="button is-primary" to="/logout">
                Log out
              </Link>
            </div>
          </div>
        </div>
      </div>
    </nav>
  );
}
