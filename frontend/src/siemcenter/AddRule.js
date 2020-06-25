import React, { useEffect, useState } from "react";
import SIEMCenterNavbar from "./SIEMCenterNavbar";
import BasicRuleEdit from "./BasicRuleEdit";
import { RuleService } from "../services/RuleService";
import TemplatesMenu from "./TemplatesMenu";
import TemplateDisplay from "./TemplateDisplay";

function AddRule() {
  const [rule, setRule] = useState({
    id: undefined,
    name: undefined,
    produces: undefined,
    consumes: undefined,
    content: undefined,
  });
  const [activeTemplate, setActiveTemplate] = useState(0);
  const [templateVariables, setTemplateVariables] = useState([
    {},
    { message: undefined },
    { message: undefined, duration: undefined, numAlarms: undefined },
    { message: undefined, blacklist: undefined },
    { message: undefined, whitelist: undefined },
  ]);
  const [validateMessage, setValidateMessage] = useState(undefined);

  // ****************************************************************************************************
  // Value change handling
  // ****************************************************************************************************

  const handleFormInputChange = (name) => (event) => {
    event.preventDefault();
    const val = event.target.value;
    setRule({ ...rule, [name]: val });
  };

  const handleTemplateFormInputChange = (name) => (event) => {
    event.preventDefault();
    const val = event.target.value;
    setTemplateVariables((prevState) => {
      const newItems = [...prevState];
      newItems[activeTemplate][name] = val;
      return newItems;
    });
  };

  const handleFormWidgetChange = (name) => (val) => {
    setRule({ ...rule, [name]: val });
  };

  const handleTemplateChange = (id) => {
    setActiveTemplate(id);
  };

  useEffect(() => {
    let content = "";
    switch (activeTemplate) {
      case 1:
        content = `package bsep.integration.custom;
        import tim6.bsep.SIEMCenter.model.drools.LogWrapper;
        import tim6.bsep.SIEMCenter.model.drools.Alarm
        import tim6.bsep.SIEMCenter.model.enums.SeverityLevel
        import org.apache.commons.lang3.StringUtils;

        rule "${rule.name}"
            when
                $logWrapper: LogWrapper(log.getSeverityLevel() == SeverityLevel.${rule.consumes},
                handled == false,
                log.getMessage().matches("${templateVariables[1].message}"))
            then
                System.out.println("Alert");
                Alarm alarm = new Alarm($logWrapper.getLog(), SeverityLevel.${rule.produces}, $logWrapper.getLog().getMessage());
                $logWrapper.setHandled(true);
                update($logWrapper);
                insert(alarm);
        end`;
        setRule({ ...rule, content: content });
        break;
      case 2:
        content = `package bsep.integration.custom;
        import tim6.bsep.SIEMCenter.model.drools.LogWrapper;
        import tim6.bsep.SIEMCenter.model.drools.Alarm
        import tim6.bsep.SIEMCenter.model.enums.SeverityLevel
        import java.util.List;
        import org.apache.commons.lang3.StringUtils;

        rule "${rule.name}"
        salience 2
            when
                $logWrapper: LogWrapper(
                    log.getSeverityLevel() == SeverityLevel.${rule.consumes},
                    handled == false,
                    log.getMessage().matches("${templateVariables[2].message}")
                )
                $list : List() from collect (
                    Alarm(message == "${templateVariables[2].message}") over window:time(${templateVariables[2].duration})
                )
                eval($list.size() >= ${templateVariables[2].numAlarms})
            then
                String alarmMessage = "${templateVariables[2].message}";
                System.out.println(alarmMessage);

                Alarm alarm = new Alarm($logWrapper.getLog(), SeverityLevel.${rule.produces}, alarmMessage);
                Alarm iter;
                for(int i = 0; i < $list.size(); i++) {
                    iter = (Alarm) $list.get(i);
                    alarm.getLogIds().addAll(iter.getLogIds());
                    alarm.getHostnames().addAll(iter.getHostnames());
                }

                $logWrapper.setHandled(true);
                update($logWrapper);
                insert(alarm);
        end`;
        setRule({ ...rule, content: content });
        break;
      case 3:
        content = `package bsep.integration.custom;
        import tim6.bsep.SIEMCenter.model.drools.LogWrapper;
        import tim6.bsep.SIEMCenter.model.drools.Alarm
        import tim6.bsep.SIEMCenter.model.enums.SeverityLevel
        import tim6.bsep.SIEMCenter.model.Blacklist;
        
        global tim6.bsep.SIEMCenter.service.BlacklistService blacklistService  
        
        rule "${rule.name}"
            when
                $logWrapper: LogWrapper(
                    log.getSeverityLevel() == SeverityLevel.${rule.consumes}, handled == false,
                    log.getMessage().contains("${templateVariables[3].message}")
                )
                eval(blacklistService.getByName("${templateVariables[3].blacklist}") != null &&
                     blacklistService.getByName("${templateVariables[3].blacklist}").getContent().contains(${templateVariables[3].message})
                )
            then
                String alarmMessage = "${rule.name}";
                System.out.println(alarmMessage);
        
                Alarm alarm = new Alarm($logWrapper.getLog(), SeverityLevel.${rule.produces}, alarmMessage);
                $logWrapper.setHandled(true);
                update($logWrapper);
                insert(alarm);
        end`;
        setRule({ ...rule, content: content });
        break;
      case 4:
        content = `package bsep.integration.custom;
        import tim6.bsep.SIEMCenter.model.drools.LogWrapper;
        import tim6.bsep.SIEMCenter.model.drools.Alarm
        import tim6.bsep.SIEMCenter.model.enums.SeverityLevel
        import tim6.bsep.SIEMCenter.model.Whitelist;
        
        global tim6.bsep.SIEMCenter.service.WhitelistService whitelistService  
        
        rule "${rule.name}"
            when
                $logWrapper: LogWrapper(
                    log.getSeverityLevel() == SeverityLevel.${rule.consumes}, handled == false,
                    log.getMessage().contains("${templateVariables[4].message}")
                )
                eval(whitelistService.getByName("${templateVariables[4].whitelist}") != null &&
                     !whitelistService.getByName("${templateVariables[4].whitelist}").getContent().contains(${templateVariables[4].message})
                )
            then
                String alarmMessage = "${rule.name}";
                System.out.println(alarmMessage);
        
                Alarm alarm = new Alarm($logWrapper.getLog(), SeverityLevel.${rule.produces}, alarmMessage);
                $logWrapper.setHandled(true);
                update($logWrapper);
                insert(alarm);
        end`;
        setRule({ ...rule, content: content });
        break;
      default:
        break;
    }
  }, [
    activeTemplate,
    templateVariables,
    rule.name,
    rule.consumes,
    rule.produces,
  ]);

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
      <div className="columns">
        <div className="column is-one-fifth">
          <TemplatesMenu
            activeTemplate={activeTemplate}
            setActiveTemplate={handleTemplateChange}
          />
        </div>
        <div className="column" style={{marginTop: "3vh"}}>
          <TemplateDisplay
            activeTemplate={activeTemplate}
            inputChange={handleTemplateFormInputChange}
            variables={templateVariables}
          />
          <BasicRuleEdit
            rule={rule}
            validateMessage={validateMessage}
            inputChange={handleFormInputChange}
            widgetChange={handleFormWidgetChange}
            save={handleSave}
            validate={handleValidate}
          />
        </div>
      </div>
    </>
  );
}

export default AddRule;
