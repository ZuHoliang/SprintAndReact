import React, { useState, useEffect, useContext } from "react";
import AnnouncementSearchForm from "../../forms/AnnouncementSearchForm";
import AnnouncementCard from "../../contexts/AnnouncementCard";
import HomeButton from "../../components/HomeButton";
import "../../forms/AnnouncementSearchForm.css";
import "./Announcements.css";
import "../../components/HomeButton.css";

const API_BASE = "http://localhost:8088/api/announcements";

const Announcements = () => {
  const [announcements, setAnnouncements] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchAnnouncements = async (query = "") => {
    try {
      setLoading(true);
      const url = query ? `${API_BASE}${query}` : API_BASE;
      const res = await fetch(url, {
        method: "GET",
        credentials: "include",
      });
      if (!res.ok) throw new Error("載入失敗");
      const data = await res.json();
      setAnnouncements(data);
    } catch (err) {
      console.error("查詢公告失敗", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAnnouncements();
  }, []);

  //搜尋
  const handleSearch = (keyword, startDate, endDate) => {
    const params = new URLSearchParams({
      keyword,
      startDate,
      endDate,
    });
    return fetchAnnouncements(`/search?${params.toString()}`);
  };

  return (
    <div className="announcement-page">
      <h2>公告頁面</h2>
      <AnnouncementSearchForm onSearch={handleSearch} />

      {loading ? (
        <p>載入中...</p>
      ) : (
        announcements.map((a) => (
          <div key={a.announcementId}>
            <AnnouncementCard announcement={a} />
          </div>
        ))
      )}
      <HomeButton />
    </div>
  );
};

export default Announcements;
