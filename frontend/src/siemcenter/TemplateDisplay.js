import React from "react";
import Template1 from "./templates/Template1";
import Template2 from "./templates/Template2";
import Template4 from "./templates/Template4";
import Template3 from "./templates/Template3";

const TemplateDisplay = (props) => {
  return (
    <>
      <div className="container is-fluid">
        <div className="columns is-mobile">
          <div className="column is-9">
            {props.activeTemplate === 1 && (
              <Template1
                message={props.variables[1].message}
                inputChange={props.inputChange}
              />
            )}
            {props.activeTemplate === 2 && (
              <Template2
                message={props.variables[2].message}
                duration={props.variables[2].duration}
                numAlarms={props.variables[2].numAlarms}
                inputChange={props.inputChange}
              />
            )}
            {props.activeTemplate === 3 && (
              <Template3
                message={props.variables[3].message}
                blacklist={props.variables[3].blacklist}
                inputChange={props.inputChange}
              />
            )}
            {props.activeTemplate === 4 && (
              <Template4
                message={props.variables[4].message}
                whitelist={props.variables[4].whitelist}
                inputChange={props.inputChange}
              />
            )}
          </div>
        </div>
      </div>
    </>
  );
};

export default TemplateDisplay;
