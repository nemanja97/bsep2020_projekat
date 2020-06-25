import React, { useEffect, useState } from "react";
import { useParams, useHistory } from "react-router-dom";
import SIEMCenterNavbar from "../../shared/SIEMCenterNavbar";
import { RuleService } from "../../services/RuleService";
import BasicRuleEdit from "../../shared/BasicRuleEdit";

function ModifyRule() {
  const [rule, setRule] = useState(undefined);
  const [validateMessage, setValidateMessage] = useState(undefined);

  const params = useParams();
  const history = useHistory();

  // ****************************************************************************************************
  // Initial data
  // ****************************************************************************************************

  useEffect(() => {
    async function fetchRules() {
      const response = await RuleService.getRuleById(params.id);
      setRule(response.data.content[0]);
    }
    fetchRules();
  }, []);

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
    await RuleService.updateRules(rule)
      .then((res) => setValidateMessage("Validation succesful.\nRule updated"))
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
      {rule && (
        <BasicRuleEdit
          rule={rule}
          validateMessage={validateMessage}
          inputChange={handleFormInputChange}
          widgetChange={handleFormWidgetChange}
          save={handleSave}
          validate={handleValidate}
        />
      )}
    </>
  );
}

export default ModifyRule;
