import React from "react";

const DangerousAlarmMessage = (props) => {
  return (
    <>
      <article className="message is-danger">
        <div className="message-header">
          <p>
            {props.alarm.severityLevel}, ID: {props.alarm.id}
          </p>
          <button
            className="delete"
            aria-label="delete"
            onClick={() => props.close()}
          ></button>
        </div>
        <div className="message-body">{props.alarm.message}</div>
      </article>
    </>
  );
};

export default DangerousAlarmMessage;
