import React, { useEffect, useState } from "react";
import Stomp from "stompjs";
import { AlarmService } from "../services/AlarmService";
import AlarmsDashboard from "./AlarmsDashboard";
import SIEMCenterNavbar from "./SIEMCenterNavbar";

var stompClient = null;

function SIEMCenterHome() {
  const [liveAlarms, setLiveAlarms] = useState([]);
  const [searchAlarms, setSearchAlarms] = useState([]);
  const [dangerousAlarms, setDangerousAlarms] = useState([]);
  const [alarmExplanation, setAlarmExplanation] = useState(null);

  const [alarm_query, setAlarmQuery] = useState({
    ids: undefined,
    logIds: [],
    fromDate: undefined,
    toDate: undefined,
    facility: [],
    severity: [],
    hostnames: [],
    message: undefined,
    type: undefined,
    page: undefined,
    size: undefined,
    sort: "timestamp,desc",
  });

  // ****************************************************************************************************
  // Initial data
  // ****************************************************************************************************

  useEffect(() => {
    // Get alarms from database
    async function fetchPreviousAlarms() {
      const response = await AlarmService.getAlarms(alarm_query);
      setLiveAlarms(response.data.content.slice(0, 10));
    }
    fetchPreviousAlarms();

    // Establish socket connection
    websocket_connectAndReconnect();

    // Websocket cleanup
    window.addEventListener("unload", () => websocket_handleStop());
    return () => {websocket_handleStop();}
  }, []);

  // ****************************************************************************************************
  // Websocket handling
  // ****************************************************************************************************

  const websocket_connectAndReconnect = (socketInfo, successCallback) => {
    const ws = new WebSocket("wss://localhost:8044/connect");
    stompClient = Stomp.over(ws);
    stompClient.connect(
      {},
      (frame) => {
        console.log("Connected: " + frame);
        stompClient.subscribe("/topic/messages", function (messageOutput) {
          websocket_updateAlarms(messageOutput);
        });
      },
      () => {
        setTimeout(() => {
          websocket_connectAndReconnect(socketInfo, successCallback);
        }, 1000);
      }
    );
  }

  const websocket_updateAlarms = (message) => {
    const latestAlarm = JSON.parse(message.body);
    if (latestAlarm.body === null) {
      return;
    }
    const newAlarm = JSON.parse(message.body);

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
    if (e === undefined || stompClient === null) {
      return;
    }
    e.preventDefault();
    stompClient.disconnect();
  };

  // ****************************************************************************************************
  // Alarm handling
  // ****************************************************************************************************

  const handleAlarmSearch = (event) => {
    event.preventDefault();
    console.log(alarm_query);
  };

  const handleAlarmExplanation = (alarm) => {
    // TODO add logs explaining the alarm
    setAlarmExplanation(alarm);
  };

  // ****************************************************************************************************
  // Log handling
  // ****************************************************************************************************

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
      <AlarmsDashboard
        liveAlarms={liveAlarms}
        searchAlarms={searchAlarms}
        explanation={alarmExplanation}
        explain={handleAlarmExplanation}
      />
    </>
  );
}

export default SIEMCenterHome;
