import React from "react";
import "./MonthSwitcher.css";

//切換月份元件
const MonthSwitcher = ({ year, month, onChange }) => {
  //上個月
  const handlePrev = () => {
    const prev = new Date(year, month - 2);
    onChange(prev.getFullYear(), prev.getMonth() + 1);
  };

  //下個月
  const handleNext = () => {
    const next = new Date(year, month);
    onChange(next.getFullYear(), next.getMonth() + 1);
  };

  return (
    <div className="month-switcher">
      <button onClick={handlePrev}>上個月</button>
      <span>
        {year}年{month}月
      </span>
      <button onClick={handleNext}>下個月</button>
    </div>
  );
};

export default MonthSwitcher;
