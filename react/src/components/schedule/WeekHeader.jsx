import React from "react";
import "./WeekHeader.css";

const WeekHeader = () => {
  const days = [
    "星期一",
    "星期二",
    "星期三",
    "星期四",
    "星期五",
    "星期六",
    "星期日",
  ];
  return (
    <div className="week-header">
      {days.map((d) => (
        <div key={d} className="week-day">
          {d}
        </div>
      ))}
    </div>
  );
};

export default WeekHeader;
