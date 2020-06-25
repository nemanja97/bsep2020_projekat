import React, { useEffect, useState } from "react";
import PKINavbar from "../shared/PKINavbar";
import { useHistory } from "react-router-dom";
import { ExpirableLinksService } from "../services/ExpirableLinksService";
import ExpirableLinksTable from "./ExpirableLinksTable";

function Requests() {
  const [links, setLinks] = useState([]);
  const history = useHistory();

  useEffect(() => {
    ExpirableLinksService.getLinks().then((response) => {
      setLinks(response.data);
    });
  }, []);

  return (
    <>
      <PKINavbar />
      <ExpirableLinksTable expirableLinks={links} />
      <button
        class="button is-fullwidth is-primary"
        onClick={async () =>
          await ExpirableLinksService.createLink().then(
            ExpirableLinksService.getLinks().then((response) => {
              setLinks(response.data);
            })
          )
        }
      >
        Create new expirable link
      </button>
    </>
  );
}

export default Requests;
