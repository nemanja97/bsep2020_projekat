import axios from "axios";

export const ExpirableLinksService = {
  getLinks,
  createLink,
  isValidLink,
};

async function getLinks() {
  return await axios.get(
    `${process.env.REACT_APP_API_URL}/v1/expirableLinks/all`
  );
}

async function createLink() {
  console.log("create");
  return await axios.post(
    `${process.env.REACT_APP_API_URL}/v1/expirableLinks`
  );
}

async function isValidLink(link) {
  return await axios.get(
    `${process.env.REACT_APP_API_URL}/v1/expirableLinks/isValid?link=${link}`
  );
}
