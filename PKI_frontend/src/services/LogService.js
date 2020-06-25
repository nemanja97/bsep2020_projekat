import axios from "axios";
import { QueryService } from "./QueryService";

export const LogService = {
  getLogs,
};

async function getLogs(query) {
  const url_query = QueryService.formQuery(query);
  console.log(url_query)
  return await axios.get(
    `${process.env.REACT_APP_PKI_API_URL}/v1/logs?${url_query}`
  );
}
