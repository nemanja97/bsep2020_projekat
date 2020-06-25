import axios from "axios";
import { QueryService } from "./QueryService";

export const ReportService = {
    getData,
};

async function getData(query, selectedReport) {
    let response = {};
    switch (selectedReport) {
        case "Monthly alarms count by type":
            response = await getAlarmsMonthlyCount(query);
            break;
        case "Weekly alarms count by type":
            response = await getAlarmsWeeklyCount(query);
            break;
        case "Daily alarms count by type":
            response = await getAlarmsDailyCount(query);
            break;
        case "Alarms count by type":
            response = await getAlarmsTotalCountByType(query);
            break;
        case "Alarms count by hostname":
            response = await getAlarmsTotalCountByHostname(query);
            break;
        case "Monthly logs count by type":
            response = await getLogsMonthlyCount(query);
            break;
        case "Weekly logs count by type":
            response = await getLogsWeeklyCount(query);
            break;
        case "Daily logs count by type":
            response = await getLogsDailyCount(query);
            break;
        case "Logs count by type":
            response = await getLogsTotalCountByType(query);
            break;
        case "Logs count by hostname":
            response = await getLogsTotalCountByHostname(query);
            break;
        default:
            response = await getLogsTotalCountByType(query);
    }
    return response.data;
}

async function getAlarmsMonthlyCount(query) {
    const url_query = QueryService.formQuery(query);
    return await axios.get(
        `${process.env.REACT_APP_API_URL}/v1/reports/alarms/monthly/type?${url_query}`
    );
}

async function getAlarmsWeeklyCount(query) {
    const url_query = QueryService.formQuery(query);
    return await axios.get(
        `${process.env.REACT_APP_API_URL}/v1/reports/alarms/weekly/type?${url_query}`
    );
}

async function getAlarmsDailyCount(query) {
    const url_query = QueryService.formQuery(query);
    return await axios.get(
        `${process.env.REACT_APP_API_URL}/v1/reports/alarms/daily/type?${url_query}`
    );
}

async function getAlarmsTotalCountByType(query) {
    const url_query = QueryService.formQuery(query);
    return await axios.get(
        `${process.env.REACT_APP_API_URL}/v1/reports/alarms/total/type?${url_query}`
    );
}

async function getAlarmsTotalCountByHostname(query) {
    const url_query = QueryService.formQuery(query);
    return await axios.get(
        `${process.env.REACT_APP_API_URL}/v1/reports/alarms/total/hostname?${url_query}`
    );
}

async function getLogsMonthlyCount(query) {
    const url_query = QueryService.formQuery(query);
    return await axios.get(
        `${process.env.REACT_APP_API_URL}/v1/reports/logs/monthly/type?${url_query}`
    );
}

async function getLogsWeeklyCount(query) {
    const url_query = QueryService.formQuery(query);
    return await axios.get(
        `${process.env.REACT_APP_API_URL}/v1/reports/logs/weekly/type?${url_query}`
    );
}

async function getLogsDailyCount(query) {
    const url_query = QueryService.formQuery(query);
    return await axios.get(
        `${process.env.REACT_APP_API_URL}/v1/reports/logs/daily/type?${url_query}`
    );
}

async function getLogsTotalCountByType(query) {
    const url_query = QueryService.formQuery(query);
    return await axios.get(
        `${process.env.REACT_APP_API_URL}/v1/reports/logs/total/type?${url_query}`
    );
}

async function getLogsTotalCountByHostname(query) {
    const url_query = QueryService.formQuery(query);
    return await axios.get(
        `${process.env.REACT_APP_API_URL}/v1/reports/logs/total/hostname?${url_query}`
    );
}
