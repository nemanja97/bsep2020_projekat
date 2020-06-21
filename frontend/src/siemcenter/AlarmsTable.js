import React from "react";

const AlarmsTable = (props) => {
  return (
    <>
      <table className="table" style={{minHeight:"650px"}}>
        <thead>
          <tr>
            <th>Id</th>
            <th>Timestamp</th>
            <th>Facility</th>
            <th>Severity</th>
            <th>Message</th>
            <th>Hostnames</th>
            <th>Type</th>
            <th>Explanation</th>
          </tr>
        </thead>
        <tfoot>
          <tr>
            <th>Id</th>
            <th>Timestamp</th>
            <th>Facility</th>
            <th>Severity</th>
            <th>Message</th>
            <th>Hostnames</th>
            <th>Type</th>
            <th>Explanation</th>
          </tr>
        </tfoot>
        <tbody>
          {props.alarms &&
            props.alarms.map((alarm) => {
              return (
                <tr key={alarm.id}>
                  <th>{alarm.id}</th>
                  <td>{alarm.timestamp}</td>
                  <td>{alarm.facilityType}</td>
                  <td>{alarm.severityLevel}</td>
                  <td>{alarm.message}</td>
                  <td>{alarm.hostnames.join(",")}</td>
                  <td>{alarm.type}</td>
                  <td>
                    <button
                      className="button"
                      onClick={() => props.explain(alarm)}
                    >
                      <span className="icon is-small">
                        <i className="fas fa-question"></i>
                      </span>
                    </button>
                  </td>
                </tr>
              );
            })}
        </tbody>
      </table>
    </>
  );
};

export default AlarmsTable;
