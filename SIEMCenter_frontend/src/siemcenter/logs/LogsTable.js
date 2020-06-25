import React from "react";

const LogsTable = (props) => {
  return (
    <>
      <table className="table" style={{ minHeight: "650px", minWidth: "50vw" }}>
        <thead>
          <tr>
            <th>Id</th>
            <th>Timestamp</th>
            <th>Facility</th>
            <th>Severity</th>
            <th>Message</th>
            <th>Hostname</th>
            <th>Type</th>
          </tr>
        </thead>
        <tfoot>
          <tr>
            <th>Id</th>
            <th>Timestamp</th>
            <th>Facility</th>
            <th>Severity</th>
            <th>Message</th>
            <th>Hostname</th>
            <th>Type</th>
          </tr>
        </tfoot>
        <tbody>
          {props.logs &&
            props.logs.map((log) => {
              return (
                <tr key={log.id}>
                  <th>{log.id}</th>
                  <td>{log.timestamp}</td>
                  <td>{log.facilityType}</td>
                  <td>{log.severityLevel}</td>
                  {log.message.length <= 50 && <td>{log.message}</td>}
                  {log.message.length > 50 && <td>{log.message.substr(0, 50)})...</td>}
                  <td>{log.hostname}</td>
                  <td>{log.type}</td>
                </tr>
              );
            })}
        </tbody>
      </table>
    </>
  );
};

export default LogsTable;
