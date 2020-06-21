import React, { useState } from "react";
import LogsTable from "./LogsTable";
import Pagination from "react-js-pagination";

const LogsDashboard = (props) => {
    return (
      <>
        <div className="tabs is-toggle is-fullwidth is-normal">
          <ul>
            <li className={props.activeTab.logs === "liveLogs" ? "is-active" : ""}>
              <a onClick={props.handleTabChange("liveLogs", "log")}>
                <span className="icon is-small">
                  <i className="far fa-chart-bar" aria-hidden="true"></i>
                </span>
                <span>Live</span>
              </a>
            </li>
            <li className={props.activeTab.logs === "searchLogs" ? "is-active" : ""}>
              <a onClick={props.handleTabChange("searchLogs", "log")}>
                <span className="icon is-small">
                  <i className="fas fa-search" aria-hidden="true"></i>
                </span>
                <span>Search</span>
              </a>
            </li>
          </ul>
        </div>
        {props.activeTab.logs === "liveLogs" && (
          <LogsTable logs={props.liveLogs}/>
        )}
        {props.activeTab.logs === "searchLogs" && (
          <>
            <LogsTable logs={props.searchLogs}/>
            {props.searchLogs.length > 0 &&(
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
      </>
    );
  };
  
  export default LogsDashboard;