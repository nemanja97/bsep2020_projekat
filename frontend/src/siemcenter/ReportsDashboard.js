import React, { useState } from "react";
import Moment from "moment";
import momentLocalizer from "react-widgets-moment";
import SIEMCenterNavbar from "./SIEMCenterNavbar";
import { ReportService } from "../services/ReportService";
import { DateTimePicker, DropdownList } from "react-widgets";
import { PieChart, Pie, Tooltip, Cell } from "recharts";
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Legend } from "recharts";
import { BarChart, Bar, Brush } from "recharts";
import { Colors, Colors2 } from "./Enums";

Moment.locale("en");
momentLocalizer();

function ReportsDashboard() {
  const reportType = [
    "Monthly alarms count by type",
    "Weekly alarms count by type",
    "Daily alarms count by type",
    "Alarms count by type",
    "Alarms count by hostname",
    "Monthly logs count by type",
    "Weekly logs count by type",
    "Daily logs count by type",
    "Logs count by type",
    "Logs count by hostname",
  ]

  const [selectedReport, setSelectedReport] = useState("Alarms count by type");
  const [data, setData] = useState({});
  const [displayGraph, setDisplayGraph] = useState(false);
  const [query, setQuery] = useState({
    startDate: undefined,
    endDate: undefined
  });
  const [legendInfo, setLegendInfo] = useState("alarms")

  const getRandomColor = (index) => {
    if(index >= Colors.length){
      let randomColorIndex = Math.floor(Math.random() * Colors.length);
      return Colors2[randomColorIndex];
    }else{
      return Colors[index];
    }
  }

  const handleChange = (name) => (val) => {
    setQuery({ ...query, [name]: val });
  }

  const handleReportTypeChange = (val) => {
    setSelectedReport(val);
    setDisplayGraph(false);
  }

  async function handleSubmit() {
    const data = await ReportService.getData(query, selectedReport);
    if(selectedReport.toLowerCase().includes("alarms")){
      setLegendInfo("alarms");
    }else{
      setLegendInfo("logs");
    }
    setData(data);
    setDisplayGraph(true);
  }

  return (
    <>
      <SIEMCenterNavbar />
      <div className="columns is-centered" style={{marginTop:"1%", marginLeft:"3%", marginBottom:"3%"}}>
        <div className="column is-narrow">
          <div className="control">
            <DateTimePicker placeholder="Start date" onChange={handleChange("startDate")} />
          </div>
        </div>
        <div className="column is-narrow">
          <div className="control">
            <DateTimePicker placeholder="End date" onChange={handleChange("endDate")} />
          </div>
        </div>
        <div className="column" style={{maxWidth:"300px"}}>
          <div className="control">
            <DropdownList data={reportType} defaultValue={"Alarms count by type"} placeholder="Select report to display" onChange={handleReportTypeChange} />
          </div>
        </div>
        <div className="column is-narrow">
          <div className="control">
            <button className="button is-primary" onClick={handleSubmit}>Submit</button>
          </div>
        </div>
      </div>
      <div className="columns is-centered">
        <div className="column is-narrow">
          {(selectedReport === "Monthly alarms count by type" || selectedReport === "Monthly logs count by type") &&
           displayGraph === true && (
            <LineChart width={1200} height={600} data={data}>
              <XAxis dataKey="date" />
              <YAxis />
              <CartesianGrid strokeDasharray="3 3" />
              <Tooltip />
              <Legend />
              <Line name={"System " + legendInfo} type="monotone" dataKey="sysCount" stroke="#003f5c" strokeWidth={4} />
              <Line name={"Simulated " + legendInfo} type="monotone" dataKey="simCount" stroke="#ffa600" strokeWidth={4} />
            </LineChart>
          )}
          {(selectedReport === "Weekly alarms count by type" || selectedReport === "Weekly logs count by type") &&
           displayGraph === true && (
            <BarChart width={1200} height={600} data={data}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="date" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Brush dataKey='date' height={30} stroke="#8884d8" />
              <Bar name={"System " + legendInfo} dataKey="sysCount" fill="#003f5c" />
              <Bar name={"Simulated " + legendInfo} dataKey="simCount" fill="#ffa600" />
            </BarChart>
          )}
          {(selectedReport === "Daily alarms count by type" || selectedReport === "Daily logs count by type") &&
           displayGraph === true && (
            <BarChart width={1200} height={600} data={data}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="date" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Brush dataKey='date' height={30} stroke="#8884d8" />
              <Bar name={"System " + legendInfo} dataKey="sysCount" fill="#003f5c" />
              <Bar name={"Simulated " + legendInfo} dataKey="simCount" fill="#ffa600" />
            </BarChart>
          )}
          {(selectedReport === "Alarms count by type" || selectedReport === "Logs count by type") &&
           displayGraph === true && (
            <PieChart width={1200} height={600}>
              <Pie data={data} dataKey={"count"} nameKey={"type"} label>
                <Cell fill="#003f5c" />
                <Cell fill="#ffa600" />
              </Pie>
              <Tooltip />
              <Legend />
            </PieChart>
          )}
          {(selectedReport === "Alarms count by hostname" || selectedReport === "Logs count by hostname") &&
           displayGraph === true && (
            <PieChart width={1200} height={600}>
              <Pie data={data} dataKey={"count"} nameKey={"hostname"} label >
                {data.map((x, index) => <Cell fill={getRandomColor(index)}/>)}
              </Pie>
              <Legend />
              <Tooltip />
            </PieChart>
          )}
        </div>
      </div>
    </>
  )
}

export default ReportsDashboard;