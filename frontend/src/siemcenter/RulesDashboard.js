import React, { useEffect, useState } from "react";
import SIEMCenterNavbar from "./SIEMCenterNavbar";
import { RuleService } from "../services/RuleService";
import RulesTable from "./RulesTable";

var alarmStompClient = null;
var logStompClient = null;

function RulesDashboard() {
  const [rules, setRules] = useState([]);
  const [dangerousAlarms, setDangerousAlarms] = useState([]);

  // ****************************************************************************************************
  // Initial data
  // ****************************************************************************************************

  useEffect(() => {
    async function fetchRules() {
      const response = await RuleService.getRules({});
      setRules(response.data.content);
    }
    fetchRules();
  }, []);

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
      <RulesTable
        rules={rules}
        read={() => {
          console.log("Read");
        }}
        edit={() => {
          console.log("Edit");
        }}
        delete={() => {
          console.log("Delete");
        }}
      />
    </>
  );
}

export default RulesDashboard;
