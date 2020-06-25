import axios from "axios";
import { QueryService } from "./QueryService";

export const AlarmService = {
  getAlarms,
};

async function getAlarms(query) {
  const url_query = QueryService.formQuery(query);
  return await axios.get(
    `${process.env.REACT_APP_API_URL}/v1/alarms?${url_query}`
  );
}
