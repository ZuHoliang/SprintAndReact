import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import "./LatestAnnouncements.css";

const API_BASE = "http://localhost:8088/api";

const LatestAnnouncements = () => {
  const [announcements, setAnnouncements] = useState([]);
  const [loading, setLoading] = useState(false); //** */

  useEffect(() => {
    const fetchLatest = async () => {
      try {
        setLoading(true);
        const res = await fetch(`${API_BASE}/announcements/latest`, {
          method: "GET",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
        });
        if (res.ok) {
          const data = await res.json();
          setAnnouncements(data.slice(0, 5));
        } else {
          console.error("公告載入失敗");
        }
      } catch (err) {
        console.error("錯誤:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchLatest();
  }, []);

  return (
    <div className="announcement-panel">
      <div className="panel-header">
        <Link to="/announcements" className="announcement-title-button">
          最新公告
        </Link>
      </div>
      <div className="panel-body">
        {loading ? (
          <p>載入中...</p>
        ) : announcements.length === 0 ? (
          <p>暫無公告</p>
        ) : (
          <ul className="announcement-list">
            {announcements.map((item) => (
              <li key={item.id}>
                <strong>{item.title}</strong>
                <br />
                <small>{new Date(item.createTime).toLocaleDateString}</small>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
};

export default LatestAnnouncements;
