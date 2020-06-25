import React, { useEffect, useState } from "react";
import SIEMCenterNavbar from "../../shared/SIEMCenterNavbar";
import { useHistory } from "react-router-dom";
import { BlacklistService } from "../../services/BlacklistService";
import ListsTable from "../../shared/ListsTable";

function BlacklistsDashboard() {
  const [blacklists, setBlacklists] = useState([]);
  const [dangerousAlarms, setDangerousAlarms] = useState([]);

  const history = useHistory();

  // ****************************************************************************************************
  // Initial data
  // ****************************************************************************************************

  useEffect(() => {
    async function fetchBlacklists() {
      const response = await BlacklistService.getBlacklists();
      setBlacklists(response.data);
    }
    fetchBlacklists();
  }, []);

  // ****************************************************************************************************
  // API handling
  // ****************************************************************************************************

  const handleDelete = async (id) => {
    await BlacklistService.deleteBlacklist(id).then(() => {
      setBlacklists((prevState) => prevState.filter((el) => el.id !== id));
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
          lists={blacklists}
          edit={(id) => {
            history.push(`/siemcenter/blacklist/${id}`);
          }}
          delete={handleDelete}
        />
        <button
          class="button is-fullwidth is-primary"
          onClick={() => history.push(`/siemcenter/blacklist`)}
        >
          Create new blacklist
        </button>
      </div>
    </>
  );
}

export default BlacklistsDashboard;
