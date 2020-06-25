import React from "react";

const Template4 = (props) => {
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
        placeholder="Whitelist name"
        value={props.whitelist}
        onChange={props.inputChange("whitelist")}
      />
    </>
  );
};

export default Template4;
