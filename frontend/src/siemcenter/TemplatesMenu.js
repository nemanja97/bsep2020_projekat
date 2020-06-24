import React from "react";

const TemplatesMenu = (props) => {
  return (
    <>
      <aside className="menu" style={{ height: "100vh" }}>
        <p className="menu-label">Templates</p>
        <ul className="menu-list">
          <li>
            <a
              className={props.activeTemplate === 0 ? "is-active" : undefined}
              onClick={() => props.setActiveTemplate(0)}
            >
              Blank template
            </a>
          </li>
          <li>
            <a
              className={props.activeTemplate === 1 ? "is-active" : undefined}
              onClick={() => props.setActiveTemplate(1)}
            >
              Make alarm when log of type X with message Y happens
            </a>
          </li>
          <li>
            <a
              className={props.activeTemplate === 2 ? "is-active" : undefined}
              onClick={() => props.setActiveTemplate(2)}
            >
              Make alarm when more than N of log of type X with message Y happen
            </a>
          </li>
          <li>
            <a
              className={props.activeTemplate === 3 ? "is-active" : undefined}
              onClick={() => props.setActiveTemplate(3)}
            >
              Make alarm when log content is blacklisted
            </a>
          </li>
          <li>
            <a
              className={props.activeTemplate === 4 ? "is-active" : undefined}
              onClick={() => props.setActiveTemplate(4)}
            >
              Make alarm when log content is not in whitelist
            </a>
          </li>
        </ul>
      </aside>
    </>
  );
};

export default TemplatesMenu;
