import React from "react";
import Moment from "moment";
import momentLocalizer from "react-widgets-moment";
import { DropdownList, Multiselect, DateTimePicker } from "react-widgets";
import { facilityType, severityLevel, logType, searchType } from '../shared/Enums';

Moment.locale("en");
momentLocalizer();

const Search = ({ ids, handleSearchFormInputChange, handleSearchFormWidgetChange, handleSearchChange, handleSearchSubmit, handleIdsChange }) => {
  return (
    <>
      <div className="field is-grouped">
        <div className="column">
          <div className="control">
            <input className="input" type="text" placeholder="Ids" onChange={handleIdsChange} />
          </div>
        </div>
        <div className="column">
          <div className="control">
            <Multiselect data={facilityType} placeholder="Facility" onChange={handleSearchFormWidgetChange("facilityTypes")} />
          </div>
        </div>
        <div className="column">
          <div className="control">
            <Multiselect data={severityLevel} placeholder="Severity" onChange={handleSearchFormWidgetChange("severityLevels")} />
          </div>
        </div>
        <div className="column">
          <div className="control">
            <input class="input" type="text" placeholder="Message regex" onChange={handleSearchFormInputChange("message")} />
          </div>
        </div>
        <div className="column">
          <div className="control">
            <input className="input" type="text" placeholder="Hostnames regex" onChange={handleSearchFormInputChange("hostnames")} />
          </div>
        </div>
        <div className="column">
          <div className="control">
            <Multiselect data={logType} placeholder="Log type" onChange={handleSearchFormWidgetChange("type")} />
          </div>
        </div>
        <div className="column">
          <div className="control">
            <DateTimePicker placeholder="From" onChange={handleSearchFormWidgetChange("fromDate")} />
          </div>
        </div>
        <div className="column">
          <div className="control">
            <DateTimePicker placeholder="To" onChange={handleSearchFormWidgetChange("toDate")} />
          </div>
        </div>
        <div className="column">
          <div className="control">
            <DropdownList data={searchType} defaultValue={"LOGS"} placeholder="Search for" style={{ minWidth: "150px" }} onChange={handleSearchChange} />
          </div>
        </div>
        <div className="column">
          <div className="control">
            <button className="button is-primary" onClick={handleSearchSubmit}>Submit</button>
          </div>
        </div>
      </div>
    </>
  );
};

export default Search;
