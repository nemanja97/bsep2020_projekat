import React, { useEffect, useState } from "react";
import Stomp from "stompjs";
import { AlarmService } from "../services/AlarmService";

var stompClient = null;

function SIEMCenterHome() {
  const [data, setData] = useState([]);

  useEffect(() => {
    window.addEventListener("unload", () => handleStop());
    const socket = new WebSocket("wss://localhost:8044/connect");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
      console.log("Connected: " + frame);
      stompClient.subscribe("/topic/messages", function (messageOutput) {
        updateAlarms(messageOutput);
      });
    });
    const response = AlarmService.getNewestAlarms();
    console.log(response);
    return () => handleStop();
  }, []);

  const updateAlarms = (message) => {
    console.log(message);
    var latestAlarm = JSON.parse(message.body);
    if (latestAlarm.body === null) {
      return;
    }
    setData(prevState => {
      return prevState.concat(message)
    });
  };

  const handleStop = (e) => {
    if (e === undefined) {
      return;
    }
    e.preventDefault();
    if (stompClient === null) {
      return;
    }
    //stompClient.send("/ws/stop")
    stompClient.disconnect();
  };

  return (
    <>
      <h1>Test</h1>
      {data &&
        data.map((d) => {
          return JSON.stringify(d);
        })}
    </>
  );
}

export default SIEMCenterHome;
