import React from "react";
import Pagination from "react-js-pagination";

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
            <th>Review</th>
            <th>Delete</th>
          </tr>
        </thead>
        <tfoot>
          <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Produces alarm of type</th>
            <th>Checks logs of type</th>
            <th>Review</th>
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
                    <button className="button" onClick={() => props.edit(rule.id)}>
                      <span className="icon is-small">
                        <i class="fas fa-edit"></i>
                      </span>
                    </button>
                  </td>
                  <td>
                    <button
                      className="button"
                      onClick={() => props.delete(rule.id)}
                    >
                      <span className="icon is-small">
                        <i class="fas fa-backspace"></i>
                      </span>
                    </button>
                  </td>
                </tr>
              );
            })}
        </tbody>
      </table>
      {props.rules.length > 0 && (
        <Pagination
          activePage={props.searchPage.activePage}
          itemsCountPerPage={props.searchPage.itemsCountPerPage}
          totalItemsCount={props.searchPage.totalItemsCount}
          pageRangeDisplayed={3}
          onChange={props.handlePageChange.bind(this)}
        />
      )}
    </>
  );
};

export default RulesTable;
