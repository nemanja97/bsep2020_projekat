import axios from "axios";

export const BlacklistService = {
  getBlacklists,
  getBlacklistById,
  createBlacklist,
  updateBlacklists,
  deleteBlacklist,
};

async function getBlacklists() {
  return await axios.get(
    `${process.env.REACT_APP_PKI_API_URL}/v1/blacklists`
  );
}

async function getBlacklistById(id) {
  return await axios.get(
    `${process.env.REACT_APP_PKI_API_URL}/v1/blacklists?id=${id}`
  );
}

async function createBlacklist(blacklist) {
  return await axios.post(
    `${process.env.REACT_APP_PKI_API_URL}/v1/blacklists`,
    blacklist
  );
}

async function updateBlacklists(blacklist) {
  return await axios.put(`${process.env.REACT_APP_PKI_API_URL}/v1/blacklists`, blacklist);
}

async function deleteBlacklist(id) {
  return await axios.delete(
    `${process.env.REACT_APP_PKI_API_URL}/v1/blacklists?id=${id}`
  );
}
