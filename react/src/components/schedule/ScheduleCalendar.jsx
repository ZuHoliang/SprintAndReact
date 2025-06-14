import React from "react";
import ScheduleCell from "./ScheduleCell";
import MonthSwitcher from "./MonthSwitcher";
import { formatDate } from "../../utils/formatDate";
import WeekHeader from "./WeekHeader";
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
  const startOffset = (() => {
    const first = new Date(year, month - 1, 1);
    // convert JS Sunday=0 index to Monday=0
    return (first.getDay() + 6) % 7;
  })();
  const leading = Array(startOffset).fill(null);
  const lastDate = days[days.length - 1];
  const endOffset = (() => {
    const lastDayIndex = (lastDate.getDay() + 6) % 7;
    return (7 - lastDayIndex - 1) % 7;
  })();
  const trailing = Array(endOffset).fill(null);
  const calendarDates = [...leading, ...days, ...trailing];

  return (
    <div className="schedule-calendar">
      <MonthSwitcher year={year} month={month} onChange={onMonthChange} />
      <WeekHeader />
      <div className="calendar-grid">
        {calendarDates.map((date, idx) => {
          if (!date) {
            return <div key={`empty-${idx}`} className="empty-cell" />;
          }
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
