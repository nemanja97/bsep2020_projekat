import { QueryService } from "./QueryService";
import axios from "axios"

export const RuleService = {
    getRules,
};

async function getRules(query) {
  const url_query = QueryService.formQuery(query);
  return await axios.get(
    `${process.env.REACT_APP_PKI_API_URL}/v1/rules?${url_query}`
  );
}