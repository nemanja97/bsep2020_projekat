import axios from "axios";

export const WhitelistService = {
  getWhitelists,
  getWhitelistById,
  createWhitelist,
  updateWhitelists,
  deleteWhitelist,
};

async function getWhitelists() {
  return await axios.get(
    `${process.env.REACT_APP_API_URL}/v1/whitelists`
  );
}

async function getWhitelistById(id) {
  return await axios.get(
    `${process.env.REACT_APP_API_URL}/v1/whitelists?id=${id}`
  );
}

async function createWhitelist(whitelist) {
  return await axios.post(
    `${process.env.REACT_APP_API_URL}/v1/whitelists`,
    whitelist
  );
}

async function updateWhitelists(whitelist) {
  return await axios.put(`${process.env.REACT_APP_API_URL}/v1/whitelists`, whitelist);
}

async function deleteWhitelist(id) {
  return await axios.delete(
    `${process.env.REACT_APP_API_URL}/v1/whitelists?id=${id}`
  );
}
