import CalendarChart from "../Component/Chart/CalendarChart";
import GeoChart from "../Component/Chart/GeoChart";
import PieChart from "../Component/Chart/PieChart";
import DailyRecord from "../Component/DailyRecord";
import Top10 from "../Component/Top10";
import MonthlyChart from "../Component/Chart/MonthlyChart";
import DailyCheckById from "../Component/Chart/DailyCheckbyItem";
import TotalProduct from "../Component/Chart/TotalProduct";
import SSE from "../Component/SSE";

import "./Dashboard.css";

export default function Dashboard() {

  return (
    <section className="Dashboard">
      <div className="leftBox">
        <div className="lb-left">
          <div className="lb-left-top chartSection">
            <DailyRecord />
          </div>
          <div className="lb-left-middle chartSection">
            <Top10/>
          </div>
        </div>
        <div className="lb-right">
          <div className="lb-right-top chartSection">
            <MonthlyChart />
          </div>
          <div className="lb-right-middle chartSection">
            <DailyCheckById/>
          </div>
          <div className="lb-right-bottom chartSection">
            <TotalProduct/>
          </div>
        </div>
      </div>

      <div className="rightBox">
        <SSE />
      </div>
    </section>
  );
}
