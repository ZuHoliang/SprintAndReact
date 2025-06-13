import React from "react";
import ScheduleCell from "./ScheduleCell";
import MonthSwitcher from "./MonthSwitcher";
import { formatDate } from "../../utils/formatDate";
import "./ScheduleCalendar.css";

//整體班表
const ScheduleCalendar = ({
  year,
  month,
  schedules,
  onShiftClick,
  onMonthChange,
}) => {
  const getDaysInMonth = (year, month) => {
    const date = new Date(year, month - 1);
    const days = [];
    while (date.getMonth() === month - 1) {
      days.push(new Date(date));
      date.setDate(date.getDate() + 1);
    }
    return days;
  };

  const days = getDaysInMonth(year, month);

  return (
    <div className="schedule-calendar">
      <MonthSwitcher year={year} month={month} onChange={onMonthChange} />
      <div className="calendar-grid">
        {days.map((date) => {
          const formatted = formatDate(date);
          const daySchedules = schedules.filter(
            (s) => s.workDate === formatted
          );
          return (
            <ScheduleCell
              key={formatted}
              date={formatted}
              schedules={daySchedules}
              onShiftClick={onShiftClick}
            />
          );
        })}
      </div>
    </div>
  );
};

export default ScheduleCalendar;
