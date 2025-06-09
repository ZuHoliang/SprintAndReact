import React, { useContext } from "react";
import { AuthContext } from "../../contexts/AuthContext";
import NavButton from "../../components/NavButton";
import Calendar from "../../components/Calendar";
import HomeButton from "../../components/HomeButton";

const Schedule = () => {
  const { user } = useContext(AuthContext);
  const isAdmin = user?.role === 2;

  return (
    <div className="schedule-container">
      <h2 className="schedule-title"> 班表</h2>
      <p className="schedule-description">班表內容</p>

      {/* 識別權限 */}
      {isAdmin && (
        <div className="">
          <NavButton to="/schedule" label="調整班表" />
          <Calendar />
        </div>
      )}
      <HomeButton />
    </div>
  );
};

export default Schedule;
