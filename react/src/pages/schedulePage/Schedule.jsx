import React, { useState, useEffect, useContext } from "react";
import AuthContext from "../../contexts/AuthContext.js";
import ScheduleCalendar from "../../components/schedule/ScheduleCalendar.jsx";
import ShiftActionDialog from "../../components/schedule/ShiftActionDialog.jsx";
import HomeButton from "../../components/HomeButton";
import useAuthFetch from "../../utils/useAuthFetch";

const API_BASE = "http://localhost:8088/api";

const Schedule = () => {
  const today = new Date();
  const [year, setYear] = useState(today.getFullYear());
  const [month, setMonth] = useState(today.getMonth() + 1);
  const [schedules, setSchedules] = useState([]);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [selectedDate, setSelectedDate] = useState("");
  const [selectedShift, setSelectedShift] = useState("");
  const [selectedMembers, setSelectedMembers] = useState([]);
  const [selfScheduled, setSelfScheduled] = useState(false);
  const { user } = useContext(AuthContext);
  const authFetch = useAuthFetch();

  useEffect(() => {
    fetchSchedules();
  }, [year, month]);

  const fetchSchedules = async () => {
    try {
      const res = await authFetch(`${API_BASE}/schedule/${year}/${month}`, {
        method: "GET",
        credentials: "include",
      });
      const data = await res.json();
      if (res.ok) {
        setSchedules(data.data);
      } else {
        alert(data.message || "載入班表失敗");
      }
    } catch {
      alert("連線失敗");
    }
  };

  //點選班表
  const handleShiftClick = (data, shiftType) => {
    setSelectedDate(data);
    setSelectedShift(shiftType);
    //目前排班的人
    const dayMembers = schedules.filter(
      (s) => s.workDate === data && s.shiftType === shiftType
    );
    //使用者是否已經排了該班次
    setSelectedMembers(dayMembers);
    setSelfScheduled(dayMembers.some((m) => m.userId === user?.userId));
    setDialogOpen(true);
  };

  const handleMonthChange = (newYear, newMonth) => {
    setYear(newYear);
    setMonth(newMonth);
  };

  const handleCloseDialog = () => {
    setDialogOpen(false);
    setSelectedMembers([]);
    setSelectedDate("");
    setSelectedShift("");
  };

  //排班
  const handleAssign = async (date, shiftType) => {
    try {
      const res = await authFetch(
        `${API_BASE}/schedule?date=${date}&shiftType=${shiftType}`,
        {
          method: "POST",
          credentials: "include",
        }
      );
      const data = await res.json();
      if (res.ok) {
        alert("排班成功");
        handleCloseDialog();
        fetchSchedules();
      } else {
        alert(data.message || "排班失敗");
      }
    } catch {
      alert("連線錯誤");
    }
  };

  //取消排班
  const handleCancel = async (date, shiftType) => {
    try {
      const res = await authFetch(
        `${API_BASE}/schedule?date=${date}&shiftType=${shiftType}`,
        {
          method: "DELETE",
          credentials: "include",
        }
      );
      const data = await res.json();
      if (res.ok) {
        alert("取消排班");
        handleCloseDialog();
        fetchSchedules();
      } else {
        alert(data.message || "取消失敗");
      }
    } catch {
      alert("連線錯誤");
    }
  };

  //申請換班
  const handleRequestSwap = async (date, shiftType, targetUserId, message) => {
    try {
      const res = await authFetch(`${API_BASE}/swap`, {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          swapDate: date,
          swapToShift: shiftType,
          targetUserId,
          swapMessage: message,
        }),
      });
      const data = await res.json();
      if (res.ok) {
        alert("已送出換班申請");
        handleCloseDialog();
      } else {
        alert(data.message || "換班申請送出失敗");
      }
    } catch {
      alert("連線錯誤");
    }
  };

  return (
    <div className="schedule-page">
      <h2>班表</h2>

      <ScheduleCalendar
        year={year}
        month={month}
        schedules={schedules}
        onShiftClick={handleShiftClick}
        onMonthChange={handleMonthChange}
      />

      <ShiftActionDialog
        open={dialogOpen}
        date={selectedDate}
        shiftType={selectedShift}
        members={selectedMembers}
        isSelfScheduled={selfScheduled}
        onClose={handleCloseDialog}
        onAssign={handleAssign}
        onCancel={handleCancel}
        onRequestSwap={handleRequestSwap}
      />

      <HomeButton />
    </div>
  );
};

export default Schedule;
