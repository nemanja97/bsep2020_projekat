import React from "react";
import { severityLevel } from "./Enums";
import { Combobox } from "react-widgets";

const BasicRuleEdit = (props) => {
  return (
    <>
      <div className="container is-fluid">
        <div className="columns is-mobile">
          <div className="column is-9">
            <div className="control">
              <input
                className="input"
                type="text"
                value={props.rule.name}
                placeholder="Rule name"
                style={{ marginBottom: "10px" }}
                onChange={props.inputChange("name")}
              />
              <textarea
                className="textarea has-fixed-size"
                rows="20"
                onChange={props.inputChange("content")}
                value={props.rule.content}
              >
              </textarea>
            </div>
          </div>
          <div className="column is-3">
            <textarea
              style={{ marginTop: "50px" }}
              className="textarea has-fixed-size"
              readonly
              rows="10"
              placeholder="Validation output"
              value={props.validateMessage}
            />
            <div style={{ marginTop: "5px" }}>
              <Combobox
                data={severityLevel}
                placeholder="Reads logs with severity level"
                defaultValue={props.rule.consumes}
                onChange={props.widgetChange("consumes")}
              />
            </div>
            <div style={{ marginTop: "5px", marginBottom: "5px" }}>
              <Combobox
                data={severityLevel}
                placeholder="Produces alarms with severity level"
                defaultValue={props.rule.produces}
                onChange={props.widgetChange("produces")}
              />
            </div>
            <div className="control">
              <button
                className="button is-fullwidth is-primary"
                style={{ marginTop: "5px", marginBottom: "5px" }}
                onClick={() => props.save()}
              >
                Save
              </button>
            </div>
            <div className="control">
              <button
                className="button is-fullwidth is-info"
                onClick={() => props.validate()}
              >
                Validate
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default BasicRuleEdit;
