import { QueryService } from "./QueryService";
import axios from "axios";

export const RuleService = {
  getRules,
  getRuleById,
  createRule,
  validateRule,
  updateRules,
  deleteRule,
};

async function getRules(query) {
  const url_query = QueryService.formQuery(query);
  return await axios.get(
    `${process.env.REACT_APP_PKI_API_URL}/v1/rules?${url_query}`
  );
}

async function getRuleById(id) {
  return await axios.get(
    `${process.env.REACT_APP_PKI_API_URL}/v1/rules?ids=${id}`
  );
}

async function createRule(rule) {
  return await axios.post(
    `${process.env.REACT_APP_PKI_API_URL}/v1/rules`,
    rule
  );
}

async function validateRule(rule) {
  return await axios.post(
    `${process.env.REACT_APP_PKI_API_URL}/v1/rules/validate`,
    rule
  );
}

async function updateRules(rule) {
  return await axios.put(`${process.env.REACT_APP_PKI_API_URL}/v1/rules`, rule);
}

async function deleteRule(id) {
  return await axios.delete(
    `${process.env.REACT_APP_PKI_API_URL}/v1/rules?id=${id}`
  );
}
