import React, { useState } from "react";
import AlarmsTable from "./AlarmsTable";
import AlarmsSearch from "./AlarmsSearch";

const AlarmsDashboard = (props) => {
  const [activeTab, setActiveTab] = useState({
    tab: "liveAlarms",
  });

  const handleTabChange = (value) => (event) => {
    event.preventDefault();
    setActiveTab({ ...activeTab, tab: value });
  };

  return (
    <>
      <div className="tabs is-toggle is-fullwidth is-normal">
        <ul>
          <li className={activeTab === "liveAlarms" ? "is-active" : ""}>
            <a onClick={handleTabChange("liveAlarms")}>
              <span className="icon is-small">
                <i className="far fa-chart-bar" aria-hidden="true"></i>
              </span>
              <span>Live</span>
            </a>
          </li>
          <li className={activeTab === "searchAlarms" ? "is-active" : ""}>
            <a onClick={handleTabChange("searchAlarms")}>
              <span className="icon is-small">
                <i className="fas fa-search" aria-hidden="true"></i>
              </span>
              <span>Search</span>
            </a>
          </li>
        </ul>
      </div>
      {activeTab.tab === "liveAlarms" && (
        <AlarmsTable alarms={props.liveAlarms} explain={props.explain} />
      )}
      {activeTab.tab === "searchAlarms" && (
        <>
          <AlarmsSearch />
          <AlarmsTable alarms={props.searchAlarms} explain={props.explain} />
        </>
      )}
      {props.explanation !== null && (
        <div className="modal is-active">
          <div
            className="modal-background"
            onClick={() => props.explain(null)}
          ></div>
          <div className="modal-card">
            <header className="modal-card-head">
              <p className="modal-card-title">
                {props.explanation.severityLevel} - ID: {props.explanation.id}{" "}
                explanation
              </p>
              <button
                className="delete"
                aria-label="close"
                onClick={() => props.explain(null)}
              ></button>
            </header>
            <section className="modal-card-body">
              <p>{JSON.stringify(props.explanation)}</p>
            </section>
            <footer className="modal-card-foot"></footer>
          </div>
        </div>
      )}
    </>
  );
};

export default AlarmsDashboard;
