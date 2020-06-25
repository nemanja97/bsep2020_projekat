import React, { useEffect, useState } from "react";
import SIEMCenterNavbar from "../../shared/SIEMCenterNavbar";
import { RuleService } from "../../services/RuleService";
import RulesTable from "./RulesTable";
import SearchRules from "./SearchRules";
import { useHistory } from "react-router-dom";

var alarmStompClient = null;
var logStompClient = null;

function RulesDashboard() {
  const [rules, setRules] = useState([]);
  const [query, setQuery] = useState({
    ids: [],
    name: undefined,
    produces: undefined,
    consumes: undefined,
    page: undefined,
    size: 10,
    sort: "timestamp,desc",
  });
  const [dangerousAlarms, setDangerousAlarms] = useState([]);
  const [searchPage, setSearchPage] = useState({
    activePage: 0,
    itemsCountPerPage: 10,
    totalItemsCount: 0,
  });
  const history = useHistory();

  // ****************************************************************************************************
  // Initial data
  // ****************************************************************************************************

  useEffect(() => {
    async function fetchRules() {
      const response = await RuleService.getRules(query);
      setRules(response.data.content);
      updateSearchPageInfo(response.data);
    }
    fetchRules();
  }, []);

  // ****************************************************************************************************
  // Page info handling
  // ****************************************************************************************************

  const updateSearchPageInfo = (response) => {
    const frontendPageNumber = response.number + 1;
    setSearchPage({
      ...searchPage,
      activePage: frontendPageNumber,
      itemsCountPerPage: response.numberOfElements,
      totalItemsCount: response.totalElements,
    });
    setQuery({ ...query, page: undefined });
  };

  const handlePageChange = (selectedPage) => {
    const backendPageNumber = selectedPage - 1;
    setQuery({ ...query, page: backendPageNumber });
  };

  useEffect(() => {
    async function executeSearchQuery() {
      const response = await RuleService.getRules(query);
      setRules(response.data.content);
      updateSearchPageInfo(response.data);
    }
    if (query.page !== undefined) {
      executeSearchQuery();
    }
  }, [query.page]);

  // ****************************************************************************************************
  // Search handling
  // ****************************************************************************************************

  const handleSearchFormInputChange = (name) => (event) => {
    const val = event.target.value;
    setQuery({ ...query, [name]: val });
  };

  const handleSearchFormWidgetChange = (name) => (val) => {
    setQuery({ ...query, [name]: val });
  };

  const handleSearchSubmit = () => {
    setQuery({ ...query, page: 0 });
  };

  const handleIdsChange = (event) => {
    const val = event.target.value;
    const newIds = val.split(" ").filter((x) => x !== "");
    setQuery({ ...query, ids: newIds });
  };

  // ****************************************************************************************************
  // API handling
  // ****************************************************************************************************

  const handleDelete = async (id) => {
    await RuleService.deleteRule(id).then(() => {
      setRules((prevState) => prevState.filter((el) => el.id !== id));
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
      <div className="container">
        <div className="columns is-centered is-vcentered is-mobile">
          <div className="column has-text-centered">
            <SearchRules
              handleSearchFormInputChange={handleSearchFormInputChange}
              handleSearchFormWidgetChange={handleSearchFormWidgetChange}
              handleSearchSubmit={handleSearchSubmit}
              handleIdsChange={handleIdsChange}
              addNew={() => {
                history.push(`/siemcenter/rule`);
              }}
            />
          </div>
        </div>
      </div>
      <RulesTable
        rules={rules}
        edit={(id) => {
          history.push(`/siemcenter/rule/${id}`);
        }}
        delete={handleDelete}
        searchPage={searchPage}
        handlePageChange={handlePageChange}
      />
    </>
  );
}

export default RulesDashboard;
