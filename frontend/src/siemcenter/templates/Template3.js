import React from "react";

const Template3 = (props) => {
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
        placeholder="Blacklist name"
        value={props.blacklist}
        onChange={props.inputChange("blacklist")}
      />
    </>
  );
};

export default Template3;
