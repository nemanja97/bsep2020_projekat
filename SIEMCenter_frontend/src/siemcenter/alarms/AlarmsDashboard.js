import React, { useState } from "react";
import AlarmsTable from "./AlarmsTable";
import Pagination from "react-js-pagination";

const AlarmsDashboard = (props) => {
  return (
    <>
      <div className="tabs is-toggle is-fullwidth is-normal">
        <ul>
          <li
            className={
              props.activeTab.alarms === "liveAlarms" ? "is-active" : ""
            }
          >
            <a onClick={props.handleTabChange("liveAlarms", "alarm")}>
              <span className="icon is-small">
                <i className="far fa-chart-bar" aria-hidden="true"></i>
              </span>
              <span>Live</span>
            </a>
          </li>
          <li
            className={
              props.activeTab.alarms === "searchAlarms" ? "is-active" : ""
            }
          >
            <a onClick={props.handleTabChange("searchAlarms", "alarm")}>
              <span className="icon is-small">
                <i className="fas fa-search" aria-hidden="true"></i>
              </span>
              <span>Search</span>
            </a>
          </li>
        </ul>
      </div>
      {props.activeTab.alarms === "liveAlarms" && (
        <AlarmsTable alarms={props.liveAlarms} explain={props.explain} />
      )}
      {props.activeTab.alarms === "searchAlarms" && (
        <>
          <AlarmsTable alarms={props.searchAlarms} explain={props.explain} />
          {props.searchAlarms.length > 0 && (
            <Pagination
              activePage={props.searchPage.activePage}
              itemsCountPerPage={props.searchPage.itemsCountPerPage}
              totalItemsCount={props.searchPage.totalItemsCount}
              pageRangeDisplayed={3}
              onChange={props.handlePageChange.bind(this)}
            />
          )}
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
                is caused by
              </p>
              <button
                className="delete"
                aria-label="close"
                onClick={() => props.explain(null)}
              ></button>
            </header>
            <section className="modal-card-body">
              <div class="container">
                {props.explanationLogs &&
                  props.explanationLogs.map((log) => {
                    return (
                      <div style={{marginBottom: 10}}>
                        <h1 style={{marginBottom: 2}} class="subtitle">LOG - ID: {log.id}</h1>
                        <ul>
                          <li>Timestamp: {log.timestamp}</li>
                          <li>Facility type: {log.facilityType}</li>
                          <li>Severity level: {log.severityLevel}</li>
                          <li>Type: {log.type}</li>
                          <li>Hostname: {log.hostname}</li>
                          <li>Message: {log.message}</li>
                        </ul>
                      </div>
                    );
                  })}
              </div>
            </section>
            <footer className="modal-card-foot"></footer>
          </div>
        </div>
      )}
    </>
  );
};

export default AlarmsDashboard;
