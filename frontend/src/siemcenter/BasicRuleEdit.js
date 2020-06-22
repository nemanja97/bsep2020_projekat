import React from "react";
import { severityLevel } from "./Enums";
import { Combobox } from "react-widgets";

const BasicRuleEdit = (props) => {
  return (
    <>
      <div class="container is-fluid">
        <div class="columns is-mobile">
          <div class="column is-9">
            <div class="control">
              <input
                class="input"
                type="text"
                value={props.rule.name}
                placeholder={props.rule.name}
                style={{ marginBottom: "10px" }}
                onChange={props.inputChange("name")}
              />
              <textarea
                class="textarea has-fixed-size"
                readonly
                rows="20"
                onChange={props.inputChange("content")}
              >
                {props.rule.content}
              </textarea>
            </div>
          </div>
          <div class="column is-3">
            <textarea
              style={{ marginTop: "50px" }}
              class="textarea has-fixed-size"
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
            <div class="control">
              <button
                class="button is-fullwidth is-primary"
                style={{ marginTop: "5px", marginBottom: "5px" }}
                onClick={() => props.save()}
              >
                Save
              </button>
            </div>
            <div class="control">
              <button
                class="button is-fullwidth is-info"
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
