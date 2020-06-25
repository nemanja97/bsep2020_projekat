import React, { useEffect, useState } from "react";
import SIEMCenterNavbar from "../../shared/SIEMCenterNavbar";
import { useHistory } from "react-router-dom";
import { WhitelistService } from "../../services/WhitelistService";
import ListsTable from "../../shared/ListsTable";

function WhitelistsDashboard() {
  const [whitelists, setWhitelists] = useState([]);
  const [dangerousAlarms, setDangerousAlarms] = useState([]);

  const history = useHistory();

  // ****************************************************************************************************
  // Initial data
  // ****************************************************************************************************

  useEffect(() => {
    async function fetchWhitelists() {
      const response = await WhitelistService.getWhitelists();
      setWhitelists(response.data);
    }
    fetchWhitelists();
  }, []);

  // ****************************************************************************************************
  // API handling
  // ****************************************************************************************************

  const handleDelete = async (id) => {
    await WhitelistService.deleteWhitelist(id).then(() => {
      setWhitelists((prevState) => prevState.filter((el) => el.id !== id));
    });
  };

  return (
    <>
      <SIEMCenterNavbar />
      {dangerousAlarms.length > 0 && (
        <article className="message is-danger">
          <div className="message-header">
            <p>
              {dangerousAlarms[0].severityLevel}, ID: {dangerousAlarms[0].id}
            </p>
            <button
              className="delete"
              aria-label="delete"
              onClick={() => setDangerousAlarms(dangerousAlarms.slice(1))}
            ></button>
          </div>
          <div className="message-body">{dangerousAlarms[0].message}</div>
        </article>
      )}
      <div class="container">
        <ListsTable
          lists={whitelists}
          edit={(id) => {
            history.push(`/siemcenter/whitelist/${id}`);
          }}
          delete={handleDelete}
        />
        <button
          class="button is-fullwidth is-primary"
          onClick={() => history.push(`/siemcenter/whitelist`)}
        >
          Create new whitelist
        </button>
      </div>
    </>
  );
}

export default WhitelistsDashboard;
