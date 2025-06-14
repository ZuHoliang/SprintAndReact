import React from "react";
import "./ScheduleCell.css";

//單獨一天(用來顯示早班/晚班 和排班人員)
const ScheduleCell = ({ date, schedules, onShiftClick }) => {
  const getShiftMembers = (type) => {
    return schedules
      .filter((s) => s.shiftType === type)
      .map((s, index) => (
        <span key={index}>
          ({s.accountId}) <br /> {s.username}
          <br />
        </span>
      ));
  };

  return (
    <div className="schedule-cell">
      <div className="cell-date">{date}</div>
      <div
        className="shift morning"
        onClick={() => onShiftClick(date, "MORNING")}
      >
        早班: <br /> {getShiftMembers("MORNING") || "--"}
      </div>
      <div
        className="shift evening"
        onClick={() => onShiftClick(date, "EVENING")}
      >
        晚班: <br /> {getShiftMembers("EVENING") || "--"}
      </div>
    </div>
  );
};

export default ScheduleCell;
