import React from "react";
import Moment from "moment";
import momentLocalizer from "react-widgets-moment";
import { DropdownList, Multiselect, DateTimePicker } from "react-widgets";
import { severityLevel } from "./Enums";

Moment.locale("en");
momentLocalizer();

const SearchRules = ({
  ids,
  handleSearchFormInputChange,
  handleSearchFormWidgetChange,
  handleSearchChange,
  handleSearchSubmit,
  handleIdsChange,
  addNew
}) => {
  return (
    <>
      <div className="field is-grouped">
        <div className="column">
          <div className="control">
            <input
              className="input"
              type="text"
              placeholder="Ids"
              onChange={handleIdsChange}
            />
          </div>
        </div>
        <div className="column">
          <div className="control">
            <Multiselect
              data={severityLevel}
              placeholder="Severity"
              onChange={handleSearchFormWidgetChange("produces")}
            />
          </div>
        </div>
        <div className="column">
          <div className="control">
            <Multiselect
              data={severityLevel}
              placeholder="Severity"
              onChange={handleSearchFormWidgetChange("consumes")}
            />
          </div>
        </div>
        <div className="column">
          <div className="control">
            <input
              class="input"
              type="text"
              placeholder="Name regex"
              onChange={handleSearchFormInputChange("name")}
            />
          </div>
        </div>
        <div className="column">
          <div className="control">
            <button className="button is-primary" onClick={handleSearchSubmit}>
              Submit
            </button>
          </div>
        </div>
        <div className="column" >
          <div className="control" style={{marginLeft: "-5.2vw"}}>
            <button className="button is-primary" onClick={addNew}>
              Create new rule
            </button>
          </div>
        </div>
      </div>
    </>
  );
};

export default SearchRules;
