import React, { useState, useEffect } from "react";
import "./NotificationList.css";

const API_BASE = "http://localhost:8088/api";

const NotificationList = ({ onClose }) => {
  const [list, setList] = useState([]);

  const fetchList = () => {
    fetch(`${API_BASE}/notifications`, { credentials: "include" })
      .then((res = res.json()))
      .then((data) => {
        if (data.status === 200) setList(data.data);
        else alert("載入失敗");
      })
      .catch(() => alert("連線失敗"));
  };

  useEffect(() => {
    fetchList();
  }, []);

  const handleDelete = async (id) => {
    const res = await fetch(`${API_BASE}/notifications/${id}`, {
      method: "Delete",
      credentials: "include",
    });
    if (res.ok) {
      fetchList();
      onClose && onClose();
    } else {
      alert("刪除失敗");
    }
  };

  return (
    <div className="notification-list">
      <h4>通知</h4>
      {list.length === 0 ? (
        <p>目前無通知</p>
      ) : (
        list.map((n) => (
          <div key={n.notifiactionId} className="notification-item">
            <span>{n.text}</span>
            <button onClick={() => handleDelete(n.notifiactionId)}>關閉</button>
          </div>
        ))
      )}
    </div>
  );
};

export default NotificationList;
