import React, { useState, useEffect, useContext } from "react";
import AnnouncementSearchForm from "../../forms/AnnouncementSearchForm";
import AnnouncementCard from "../../contexts/AnnouncementCard";
import HomeButton from "../../components/HomeButton";
import AnnouncementForm from "../../forms/AnnouncementForm";
// import AnnouncementForm from "../../forms/AnnouncementForm";
// import { AuthContext } from "../../contexts/AuthContext";
import "./Announcements.css";
import { AuthContext } from "../../contexts/AuthContext";

const API_BASE = "http://localhost:8088/api/announcements";

const Announcements = () => {
  const { user } = useContext(AuthContext);
  const isAdmin = user?.role === 2;

  const [announcements, setAnnouncements] = useState([]);
  const [loading, setLoading] = useState(false);
  const [editId, setEditId] = useState(null);
  const [editingData, setEditingData] = useState(null);
  const [isCreating, setIsCreating] = useState(false);

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
    fetchAnnouncements(`/search?${params.toString()}`);
  };

  //編輯
  const handleEdit = (item) => {
    setEditId(item.announcementId);
    setEditingData(item);
    setIsCreating(false);
  };

  //刪除
  const handleDelete = async (id) => {
    if (window.confirm("缺任要刪除這則公告嗎?")) {
      try {
        const res = await fetch(`${API_BASE}/admin/${id}`, {
          method: "DELETE",
          credentials: "include",
        });
        if (!res.ok) throw new Error("刪除失敗");
        setAnnouncements((prev) => prev.filter((a) => a.announcementId !== id));
      } catch (err) {
        console.error("公告刪除失敗:", err);
        alert("公告刪除失敗!");
      }
    }
  };

  //
  const handleFormSubmit = async (data) => {
    const url = editId ? `${API_BASE}/admin/${editId}` : `${API_BASE}/admin/`;
    const method = editId ? "PUT" : "POST";

    try {
      const response = await fetch(url, {
        method,
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      });
      if (!response.ok) throw new Error("伺服器錯誤");
      const newItem = await response.json();
      if (editId) {
        setAnnouncements((prev) =>
          prev.map((a) =>
            a.announcementId === newItem.announcementId ? newItem : a
          )
        );
      } else {
        setAnnouncements((prev) => [newItem, ...prev]);
      }
      resetForm();
    } catch (error) {
      console.error("送出失敗:", error);
      alert("發生錯誤，請稍後再試");
    }
  };

  const resetForm = () => {
    setEditId(null);
    setEditingData(null);
    setIsCreating(false);
  };

  return (
    <div className="announcement-page">
      <h2>公告頁面</h2>
      <AnnouncementSearchForm onSearch={handleSearch} />
      {isAdmin && !isCreating && !editId && (
        <button onClick={() => setIsCreating(true)}>新增公告</button>
      )}
      {isAdmin && (isCreating || editId) && (
        <AnnouncementForm
          initialData={editingData || {}}
          mode={editId ? "edit" : "create"}
          onSubmit={handleFormSubmit}
        />
      )}
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
