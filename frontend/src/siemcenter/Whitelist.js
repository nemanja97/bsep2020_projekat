import React, { useEffect, useState } from "react";
import SIEMCenterNavbar from "./SIEMCenterNavbar";
import { useParams } from "react-router-dom";
import { WhitelistService } from "../services/WhitelistService";

function Whitelist() {
  const [whitelist, setWhitelist] = useState({
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
    async function fetchWhitelist() {
      const response = await WhitelistService.getWhitelistById(params.id);
      const whitelist = {
        id: response.data.id,
        name: response.data.name,
        content: response.data.content.join("\n"),
      };
      setWhitelist(whitelist);
    }
    if (params.id) fetchWhitelist();
  }, []);

  // ****************************************************************************************************
  // API handling
  // ****************************************************************************************************

  const handleChange = async () => {
    const whitelistDTO = {
      id: whitelist.id,
      name: whitelist.name,
      content: whitelist.content.split("\n"),
    };
    if (params.id) {
      await WhitelistService.updateWhitelists(whitelistDTO)
        .then((res) => setSuccessMessage("Whitelist updated."))
        .catch(function (error) {
          if (error.response) {
            setSuccessMessage("Whitelist update failed.");
          }
        });
    } else {
      await WhitelistService.createWhitelist(whitelistDTO)
        .then((res) => setSuccessMessage("Whitelist created."))
        .catch(function (error) {
          if (error.response) {
            setSuccessMessage("Whitelist creation failed.");
          }
        });
    }
  };

  // ****************************************************************************************************
  // Value change handling
  // ****************************************************************************************************

  const handleFormInputChange = (name) => (event) => {
    const val = event.target.value;
    setWhitelist({ ...whitelist, [name]: val });
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
            value={whitelist.name}
            placeholder={whitelist.name}
            style={{ marginBottom: "10px" }}
            onChange={handleFormInputChange("name")}
          />
          <textarea
            class="textarea has-fixed-size"
            readonly
            rows="20"
            onChange={handleFormInputChange("content")}
            value={whitelist.content}
          />
        </div>
        <button class="button is-fullwidth is-primary" onClick={handleChange}>
          Save
        </button>
      </div>
    </>
  );
}

export default Whitelist;
