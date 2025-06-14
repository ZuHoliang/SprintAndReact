import React, { useEffect, useState } from "react";
import { formatDate } from "../../utils/formatDate";
import WeekHeader from "../schedule/WeekHeader";
import "./ScheduleSummary.css";

const API_BASE = "http://localhost:8088/api";

const ScheduleSummary = ({ refreshKey }) => {
  const today = new Date();
  const [schedules, setSchedules] = useState([]);
  const [year] = useState(today.getFullYear());
  const [month] = useState(today.getMonth() + 1);

  useEffect(() => {
    fetch(`${API_BASE}/schedule/me/month`, {
      method: "GET",
      credentials: "include",
    })
      .then((res) => res.json())
      .then((data) => {
        if (data.status === 200) {
          setSchedules(data.data);
        } else {
          alert(data.message || "載入失敗");
        }
      })
      .catch(() => alert("連線錯誤"));
  }, [year, month, refreshKey]);

  //計算當月日期
  const getDaysInMonth = (year, month) => {
    const days = [];
    const date = new Date(year, month - 1, 1);
    while (date.getMonth() === month - 1) {
      days.push(new Date(date));
      date.setDate(date.getDate() + 1);
    }
    return days;
  };

  //判斷未來日期
  const isFuture = (date) => {
    const now = new Date();
    now.setHours(0, 0, 0, 0);
    return new Date(date) >= now;
  };

  //顯示未上班的標記
  const renderShiftText = (date, type) => {
    const match = schedules.find(
      (s) => s.workDate === date && s.shiftType === type
    );
    if (!match) return null;
    const future = isFuture(date);
    const shiftClass = `${type.toLowerCase()} shift ${
      future ? "future" : "past"
    }`;
    return (
      <div className={shiftClass}>{type === "MORNING" ? "早班" : "晚班"}</div>
    );
  };

  const days = getDaysInMonth(year, month);

  //取得日期偏移
  const startOffset = (() => {
    const first = new Date(year, month - 1, 1);
    return (first.getDay() + 6) % 7;
  })();
  //在第一天前面塞空值
  const leading = Array(startOffset).fill(null);
  const lastDate = days[days.length - 1];
  const endOffset = (() => {
    const lastDayIndex = (lastDate.getDay() + 6) % 7;
    return (7 - lastDayIndex - 1) % 7;
  })();

  //最後一天
  const trailing = Array(endOffset).fill(null);
  const calendarDates = [...leading, ...days, ...trailing];

  return (
    <div className="schedule-summary">
      <h3>我的排班</h3>
      <WeekHeader />
      <div className="summary-grid">
        {calendarDates.map((d, idx) => {
          if (!d) return <div key={`empty-${idx}`} className="empty-cell" />;
          const dateStr = formatDate(d);
          return (
            <div
              key={dateStr}
              className={`summary-cell ${
                isFuture(dateStr) ? "future-cell" : ""
              }`}
            >
              <div className="date-label">{d.getDate()}</div>
              {renderShiftText(dateStr, "MORNING")}
              {renderShiftText(dateStr, "EVENING")}
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default ScheduleSummary;
