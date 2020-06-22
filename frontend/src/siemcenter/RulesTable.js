import React from "react";

const RulesTable = (props) => {
  return (
    <>
      <table className="table" style={{ minHeight: "650px" }}>
        <thead>
          <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Produces alarm of type</th>
            <th>Checks logs of type</th>
            <th>Read</th>
            <th>Edit</th>
            <th>Delete</th>
          </tr>
        </thead>
        <tfoot>
          <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Produces alarm of type</th>
            <th>Checks logs of type</th>
            <th>Read</th>
            <th>Edit</th>
            <th>Delete</th>
          </tr>
        </tfoot>
        <tbody>
          {props.rules &&
            props.rules.map((rule) => {
              return (
                <tr key={rule.id}>
                  <th>{rule.id}</th>
                  <td>{rule.name}</td>
                  <td>{rule.produces}</td>
                  <td>{rule.consumes}</td>
                  <td>
                    <button
                      className="button"
                      onClick={() => props.read(rule)}
                    >
                      <span className="icon is-small">
                        <i className="fas fa-question"></i>
                      </span>
                    </button>
                  </td>
                  <td>
                    <button
                      className="button"
                      onClick={() => props.edit(rule)}
                    >
                      <span className="icon is-small">
                        <i className="fas fa-question"></i>
                      </span>
                    </button>
                  </td>
                  <td>
                    <button
                      className="button"
                      onClick={() => props.delete(rule)}
                    >
                      <span className="icon is-small">
                        <i className="fas fa-question"></i>
                      </span>
                    </button>
                  </td>
                </tr>
              );
            })}
        </tbody>
      </table>
    </>
  );
};

export default RulesTable;
