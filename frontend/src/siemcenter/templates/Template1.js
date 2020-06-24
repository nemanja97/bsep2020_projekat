import React from "react";

const Template1 = (props) => {
  return (
    <>
      <input
        className="input"
        placeholder="Log message"
        value={props.message}
        onChange={props.inputChange("message")}
      />
    </>
  );
};

export default Template1;
