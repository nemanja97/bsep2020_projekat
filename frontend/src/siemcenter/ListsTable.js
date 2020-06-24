import React from "react";

const ListsTable = (props) => {
  return (
    <>
      <table className="table">
        <thead>
          <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Review</th>
            <th>Delete</th>
          </tr>
        </thead>
        <tbody>
          {props.lists &&
            props.lists.map((list) => {
              return (
                <tr key={list.id}>
                  <th>{list.id}</th>
                  <td>{list.name}</td>
                  <td>
                    <button
                      className="button"
                      onClick={() => props.edit(list.id)}
                    >
                      <span className="icon is-small">
                        <i class="fas fa-edit"></i>
                      </span>
                    </button>
                  </td>
                  <td>
                    <button
                      className="button"
                      onClick={() => props.delete(list.id)}
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
    </>
  );
};

export default ListsTable;
