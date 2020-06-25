import React from "react";

const Template2 = (props) => {
  return (
    <>
      <input
        className="input"
        placeholder="Log message"
        value={props.message}
        onChange={props.inputChange("message")}
      />
      <input
        style={{ marginTop: 10 }}
        className="input"
        placeholder="Number of previous alarms"
        value={props.numAlarms}
        onChange={props.inputChange("numAlarms")}
      />
      <input
        style={{ marginTop: 10 }}
        className="input"
        placeholder="Duration"
        value={props.duration}
        onChange={props.inputChange("duration")}
      />
    </>
  );
};

export default Template2;
