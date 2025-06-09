import React, { useState, useEffect, useContext } from "react";
import AnnouncementSearchForm from "../../forms/AnnouncementSearchForm";
import AnnouncementCard from "../../contexts/AnnouncementCard";
import HomeButton from "../../components/HomeButton";
import "./Announcements.css";
import { AuthContext } from "../../contexts/AuthContext";

const API_BASE = "http://localhost:8088/api/announcements";

const Announcements = () => {
  const { user } = useContext(AuthContext);
  const isAdmin = user?.role === 2;

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
            {isAdmin && (
              <div style={{ marginBottom: "1rem" }}>
                <button onClick={() => handleEdit(a)}>編輯</button>
                <button onClick={() => handleDelete(a.announcementId)}>
                  刪除
                </button>
              </div>
            )}
          </div>
        ))
      )}
      <HomeButton />
    </div>
  );
};

export default Announcements;
