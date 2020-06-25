import React from "react";

const ExpirableLinksTable = (props) => {
  return (
    <>
      <table className="table">
        <thead>
          <tr>
            <th>Id</th>
            <th>Link</th>
            <th>Expired?</th>
          </tr>
        </thead>
        <tbody>
          {props.expirableLinks &&
            props.expirableLinks.map((expirableLink) => {
              return (
                <tr key={expirableLink.id}>
                  <th>{expirableLink.id}</th>
                  <th>{expirableLink.link}</th>
                  <td>
                    {expirableLink.certificateId === null ? (
                      <p>No</p>
                    ) : (
                      <p>Yes</p>
                    )}
                  </td>
                </tr>
              );
            })}
        </tbody>
      </table>
    </>
  );
};

export default ExpirableLinksTable;
