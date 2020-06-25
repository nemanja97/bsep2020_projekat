import React, { useEffect, useState } from "react";
import { AlarmService } from "../services/AlarmService";
import { LogService } from "../services/LogService";
import AlarmsDashboard from "./alarms/AlarmsDashboard";
import SIEMCenterNavbar from "../shared/SIEMCenterNavbar";
import LogsDashboard from "./logs/LogsDashboard";
import Search from "./Search";
import { Client } from "@stomp/stompjs";

var alarmStompClient = null;
var logStompClient = null;

function SIEMCenterHome() {
  const [liveAlarms, setLiveAlarms] = useState([]);
  const [searchAlarms, setSearchAlarms] = useState([]);
  const [dangerousAlarms, setDangerousAlarms] = useState([]);
  const [alarmExplanation, setAlarmExplanation] = useState(null);
  const [logExplanations, setLogExplanation] = useState([]);

  const [liveLogs, setLiveLogs] = useState([]);
  const [searchLogs, setSearchLogs] = useState([]);

  const [search, setSearch] = useState("LOGS");

  const [logSearchPage, setLogSearchPage] = useState({
    activePage: 0,
    itemsCountPerPage: 10,
    totalItemsCount: 0,
  });

  const [alarmSearchPage, setAlarmSearchPage] = useState({
    activePage: 0,
    itemsCountPerPage: 10,
    totalItemsCount: 0,
  });

  const [activeTab, setActiveTab] = useState({
    logs: "liveLogs",
    alarms: "liveAlarms",
  });

  const [query, setQuery] = useState({
    ids: undefined,
    logIds: [],
    fromDate: undefined,
    toDate: undefined,
    facilityTypes: [],
    severityLevels: [],
    hostnames: [],
    message: undefined,
    type: undefined,
    page: undefined,
    size: 10,
    sort: "timestamp,desc",
  });

  // ****************************************************************************************************
  // Initial data
  // ****************************************************************************************************

  useEffect(() => {
    // Get alarms from database
    async function fetchPreviousAlarms() {
      const response = await AlarmService.getAlarms(query);
      setLiveAlarms(response.data.content.slice(0, 10));
    }
    async function fetchPreviousLogs() {
      const response = await LogService.getLogs(query);
      setLiveLogs(response.data.content.slice(0, 10));
    }
    fetchPreviousAlarms();
    fetchPreviousLogs();

    // Establish socket connection
    websocket_connectAndReconnect();

    // Websocket cleanup
    window.addEventListener("unload", () => websocket_handleStop());
    return () => {
      websocket_handleStop();
    };
  }, []);

  // ****************************************************************************************************
  // Websocket handling
  // ****************************************************************************************************

  const websocket_connectAndReconnect = () => {
    const alarmStompClient = new Client({
      brokerURL: "wss://localhost:8044/connect",
      debug: function (str) {
        console.log(str);
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });
    const logStompClient = new Client({
      brokerURL: "wss://localhost:8044/connect",
      debug: function (str) {
        console.log(str);
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });

    alarmStompClient.onConnect = function (frame) {
      console.log("Alarm STOMP connected: " + frame);
      alarmStompClient.subscribe("/alarms", function (messageOutput) {
        websocket_updateAlarms(messageOutput);
      });
    };
    alarmStompClient.onStompError = function (frame) {
      console.log(
        "Alarm STOMP Broker reported error: " + frame.headers["message"]
      );
      console.log("Additional details: " + frame.body);
    };

    logStompClient.onConnect = function (frame) {
      console.log("Log STOMP connected: " + frame);
      logStompClient.subscribe("/logs", function (messageOutput) {
        websocket_updateLogs(messageOutput);
      });
    };
    logStompClient.onStompError = function (frame) {
      console.log(
        "Log STOMP Broker reported error: " + frame.headers["message"]
      );
      console.log("Additional details: " + frame.body);
    };

    alarmStompClient.activate();
    logStompClient.activate();
  };

  const websocket_updateLogs = (message) => {
    const newLog = JSON.parse(message.body);
    if (newLog.body === null) {
      return;
    }
    setLiveLogs((prevState) => {
      return [newLog].concat(prevState).slice(0, 10);
    });
  };

  const websocket_updateAlarms = (message) => {
    const newAlarm = JSON.parse(message.body);
    if (newAlarm.body === null) {
      return;
    }
    setLiveAlarms((prevState) => {
      return [newAlarm].concat(prevState).slice(0, 10);
    });

    if (
      newAlarm.severityLevel === "EMERGENCY" ||
      newAlarm.severityLevel === "CRITICAL"
    ) {
      setDangerousAlarms((prevState) => {
        return [newAlarm].concat(prevState);
      });
    }
  };

  const websocket_handleStop = (e) => {
    console.log("Closing websocket connections");
    if (
      e === undefined ||
      alarmStompClient === null ||
      alarmStompClient === null
    ) {
      return;
    }
    e.preventDefault();
    alarmStompClient.deactivate();
    logStompClient.deactivate();
  };

  // ****************************************************************************************************
  // Alarm handling
  // ****************************************************************************************************

  const handleAlarmExplanation = (alarm) => {
    setAlarmExplanation(alarm);
  };

  useEffect(() => {
    // Get logs from database
    async function fetchLogs() {
      const response = await LogService.getLogs({
        ids: alarmExplanation.logIds
      });
      setLogExplanation(response.data.content);
    }
    if (alarmExplanation != null)
      fetchLogs();
  }, [alarmExplanation]);

  // ****************************************************************************************************
  // Page info handling
  // ****************************************************************************************************

  const updateSearchPageInfo = (response, type) => {
    const frontendPageNumber = response.number + 1;
    if (type === "logs") {
      setLogSearchPage({
        ...logSearchPage,
        activePage: frontendPageNumber,
        itemsCountPerPage: response.numberOfElements,
        totalItemsCount: response.totalElements,
      });
    } else if (type === "alarms") {
      setAlarmSearchPage({
        ...alarmSearchPage,
        activePage: frontendPageNumber,
        itemsCountPerPage: response.numberOfElements,
        totalItemsCount: response.totalElements,
      });
    }
    setQuery({ ...query, page: undefined });
  };

  const handlePageChange = (selectedPage) => {
    const backendPageNumber = selectedPage - 1;
    setQuery({ ...query, page: backendPageNumber });
  };

  useEffect(() => {
    const executeSearchQuery = () => {
      if (search === "LOGS") {
        LogService.getLogs(query).then((res) => {
          setSearchLogs(res.data.content);
          updateSearchPageInfo(res.data, "logs");
          handleTabChange("searchLogs", "log");
        });
      } else if (search === "ALARMS") {
        AlarmService.getAlarms(query).then((res) => {
          setSearchAlarms(res.data.content);
          updateSearchPageInfo(res.data, "alarms");
          handleTabChange("searchAlarms", "alarm");
        });
      } else if (search === "BOTH") {
        LogService.getLogs(query).then((res) => {
          setSearchLogs(res.data.content);
          updateSearchPageInfo(res.data, "logs");
          handleTabChange("", "both");
        });
        AlarmService.getAlarms(query).then((res) => {
          setSearchAlarms(res.data.content);
          updateSearchPageInfo(res.data, "alarms");
          handleTabChange("42", "both");
        });
      }
    };
    if (query.page !== undefined) {
      console.log(query);
      executeSearchQuery();
    }
  }, [query.page]);

  // ****************************************************************************************************
  // Search handling
  // ****************************************************************************************************

  const handleSearchFormInputChange = (name) => (event) => {
    const val = event.target.value;
    setQuery({ ...query, [name]: val });
  };

  const handleSearchFormWidgetChange = (name) => (val) => {
    setQuery({ ...query, [name]: val });
  };

  const handleSearchChange = (value) => {
    setSearch(value);
  };

  const handleSearchSubmit = () => {
    setQuery({ ...query, page: 0 });
  };

  const handleTabChangeEvent = (value, view) => (event) => {
    event.preventDefault();
    handleTabChange(value, view);
  };

  const handleTabChange = (value, view) => {
    if (view === "log") {
      setActiveTab({ ...activeTab, logs: value });
    } else if (view === "alarm") {
      setActiveTab({ ...activeTab, alarms: value });
    } else if (view === "both") {
      setActiveTab({ logs: "searchLogs", alarms: "searchAlarms" });
    }
  };

  const handleIdsChange = (event) => {
    const val = event.target.value;
    const newIds = val.split(" ").filter((x) => x !== "");
    setQuery({ ...query, ids: newIds });
  };

  return (
    <>
      <SIEMCenterNavbar />
      {dangerousAlarms.length > 0 && (
        <article className="message is-danger">
          <div className="message-header">
            <p>
              {dangerousAlarms[0].severityLevel}, ID: {dangerousAlarms[0].id}
            </p>
            <button
              className="delete"
              aria-label="delete"
              onClick={() => setDangerousAlarms(dangerousAlarms.slice(1))}
            ></button>
          </div>
          <div className="message-body">{dangerousAlarms[0].message}</div>
        </article>
      )}
      <section>
        <div
          className="columns"
          style={{ marginTop: "25px", marginLeft: "25px" }}
        >
          <Search
            ids={query.ids}
            handleSearchFormInputChange={handleSearchFormInputChange}
            handleSearchFormWidgetChange={handleSearchFormWidgetChange}
            handleSearchChange={handleSearchChange}
            handleSearchSubmit={handleSearchSubmit}
            handleIdsChange={handleIdsChange}
          />
        </div>
        <div className="columns">
          <div className="column">
            <h2 className="title">Logs</h2>
            <LogsDashboard
              liveLogs={liveLogs}
              searchLogs={searchLogs}
              searchPage={logSearchPage}
              activeTab={activeTab}
              handlePageChange={handlePageChange}
              handleTabChange={handleTabChangeEvent}
            />
          </div>
          <div className="column">
            <h2 className="title">Alarms</h2>
            <AlarmsDashboard
              liveAlarms={liveAlarms}
              searchAlarms={searchAlarms}
              searchPage={alarmSearchPage}
              explanation={alarmExplanation}
              explanationLogs={logExplanations}
              explain={handleAlarmExplanation}
              activeTab={activeTab}
              handlePageChange={handlePageChange}
              handleTabChange={handleTabChangeEvent}
            />
          </div>
        </div>
      </section>
    </>
  );
}

export default SIEMCenterHome;
