import React, { useEffect, useState } from "react";
import SIEMCenterNavbar from "../../shared/SIEMCenterNavbar";
import { useParams } from "react-router-dom";
import { BlacklistService } from "../../services/BlacklistService";

function Blacklist() {
  const [blacklist, setBlacklist] = useState({
    id: undefined,
    name: undefined,
    content: [],
  });
  const [successMessage, setSuccessMessage] = useState(undefined);

  const params = useParams();

  // ****************************************************************************************************
  // Initial data
  // ****************************************************************************************************

  useEffect(() => {
    async function fetchBlacklist() {
      const response = await BlacklistService.getBlacklistById(params.id);
      const blacklist = {
        id: response.data.id,
        name: response.data.name,
        content: response.data.content.join("\n"),
      };
      setBlacklist(blacklist);
    }
    if (params.id) fetchBlacklist();
  }, []);

  // ****************************************************************************************************
  // API handling
  // ****************************************************************************************************

  const handleChange = async () => {
    const blacklistDTO = {
      id: blacklist.id,
      name: blacklist.name,
      content: blacklist.content.split("\n"),
    };
    if (params.id) {
      await BlacklistService.updateBlacklists(blacklistDTO)
        .then((res) => setSuccessMessage("Blacklist updated."))
        .catch(function (error) {
          if (error.response) {
            setSuccessMessage("Blacklist update failed.");
          }
        });
    } else {
      await BlacklistService.createBlacklist(blacklistDTO)
        .then((res) => setSuccessMessage("Blacklist created."))
        .catch(function (error) {
          if (error.response) {
            setSuccessMessage("Blacklist creation failed.");
          }
        });
    }
  };

  // ****************************************************************************************************
  // Value change handling
  // ****************************************************************************************************

  const handleFormInputChange = (name) => (event) => {
    const val = event.target.value;
    setBlacklist({ ...blacklist, [name]: val });
  };

  return (
    <>
      <SIEMCenterNavbar />
      {successMessage && (
        <article
          className={
            successMessage.includes("failed")
              ? "message is-danger"
              : "message is-success"
          }
        >
          <div className="message-header">
            <p>{successMessage}</p>
            <button
              className="delete"
              aria-label="delete"
              onClick={() => setSuccessMessage(undefined)}
            ></button>
          </div>
        </article>
      )}
      <div class="container">
        <div class="control">
          <input
            class="input"
            type="text"
            value={blacklist.name}
            placeholder={blacklist.name}
            style={{ marginBottom: "10px" }}
            onChange={handleFormInputChange("name")}
          />
          <textarea
            class="textarea has-fixed-size"
            readonly
            rows="20"
            onChange={handleFormInputChange("content")}
            value={blacklist.content}
          />
        </div>
        <button class="button is-fullwidth is-primary" onClick={handleChange}>
          Save
        </button>
      </div>
    </>
  );
}

export default Blacklist;
