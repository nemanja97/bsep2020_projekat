import React, { useEffect, useState } from "react";
import SIEMCenterNavbar from "./SIEMCenterNavbar";
import BasicRuleEdit from "./BasicRuleEdit";
import { RuleService } from "../services/RuleService";

function AddRule() {
  const [rule, setRule] = useState({
    id: undefined,
    name: undefined,
    produces: undefined,
    consumes: undefined,
    content: undefined,
  });
  const [validateMessage, setValidateMessage] = useState(undefined);

  // ****************************************************************************************************
  // Value change handling
  // ****************************************************************************************************

  const handleFormInputChange = (name) => (event) => {
    const val = event.target.value;
    setRule({ ...rule, [name]: val });
  };

  const handleFormWidgetChange = (name) => (val) => {
    setRule({ ...rule, [name]: val });
  };

  // ****************************************************************************************************
  // API handling
  // ****************************************************************************************************

  const handleSave = async () => {
    setValidateMessage("Validating...");
    await RuleService.createRule(rule)
      .then((res) => setValidateMessage("Validation succesful.\nRule saved"))
      .catch(function (error) {
        if (error.response) {
          let errorMessages = "Validation failed";
          const data = error.response.data;
          console.log(data);

          data.map((err) => (errorMessages += "\n" + err.text));

          console.log(errorMessages);
          setValidateMessage(errorMessages);
        }
      });
  };

  const handleValidate = async () => {
    setValidateMessage("Validating...");
    RuleService.validateRule(rule)
      .then((res) => setValidateMessage("Validation succesful."))
      .catch(function (error) {
        if (error.response) {
          let errorMessages = "Validation failed";
          const data = error.response.data;
          console.log(data);

          data.map((err) => (errorMessages += "\n" + err.text));

          console.log(errorMessages);
          setValidateMessage(errorMessages);
        }
      });
  };

  return (
    <>
      <SIEMCenterNavbar />
      <BasicRuleEdit
        rule={rule}
        validateMessage={validateMessage}
        inputChange={handleFormInputChange}
        widgetChange={handleFormWidgetChange}
        save={handleSave}
        validate={handleValidate}
      />
    </>
  );
}

export default AddRule;
