import React from "react";
import "./Calendar.css";

const Calendar = () => {
    const days =  ['日', '一', '二', '三', '四', '五', '六'];

    return(
         <div className="calendar">
      <div className="calendar-header">
        {days.map((day) => (
          <div key={day} className="calendar-cell calendar-header-cell">
            {day}
          </div>
        ))}
      </div>
      <div className="calendar-body">
        {Array.from({ length: 35 }, (_, i) => (
          <div key={i} className="calendar-cell">
            {i < 2 ? '' : i - 1} {/* 模擬月曆起始日期偏移 */}
          </div>
        ))}
      </div>
    </div>
    );
};

export default Calendar;