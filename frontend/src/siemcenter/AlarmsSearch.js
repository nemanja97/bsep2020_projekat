import React from "react";

const AlarmsSearch = (props) => {
  return (
    <>
      <div class="field is-grouped">
        <div class="control">
          <div class="select is-multiple">
            <select multiple>
              <option>Select dropdown</option>
              <option>With options</option>
            </select>
          </div>
        </div>
        <p class="control">
          <a class="button is-primary">Submit</a>
        </p>
        <p class="control">
          <a class="button is-light">Cancel</a>
        </p>
      </div>
    </>
  );
};

export default AlarmsSearch;
