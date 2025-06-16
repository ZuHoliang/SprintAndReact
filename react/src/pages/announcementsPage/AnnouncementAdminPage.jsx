import React, { useState, useEffect } from "react";
import {
  connectAnnouncementSocket,
  disconnectAnnouncementSocket,
} from "../../utils/socket";
import AnnouncementForm from "../../forms/AnnouncementForm";
import AnnouncementSearchForm from "../../forms/AnnouncementSearchForm";
import HomeButton from "../../components/HomeButton";
import useAuthFetch from "../../utils/useAuthFetch";
import "./AnnouncementAdminPage.css";
import "../../forms/AnnouncementSearchForm.css";
import "../../forms/AnnouncementForm.css";

const API_BASE = "http://localhost:8088/api/announcements";

const AnnouncementAdminPage = () => {
  const [announcements, setAnnouncements] = useState([]);
  const [editId, setEditId] = useState(null);
  const [editingData, setEditingData] = useState(null);
  const [isCreating, setIsCreating] = useState(false);
  const [page, setpage] = useState(0);
  const authFetch = useAuthFetch();

  const fetchAnnouncements = async (p = 0, query = "") => {
    try {
      const url = query
        ? `${API_BASE}${query}&page=${p}`
        : `${API_BASE}/page?page=${p}`;
      const res = await authFetch(url, {
        method: "GET",
        credentials: "include",
      });
      if (!res.ok) throw new Error("載入失敗");
      const data = await res.json();
      setAnnouncements(data);
      setpage(p);
    } catch (err) {
      console.error("公告載入錯誤：", err);
      alert("無法載入公告資料");
    }
  };

  useEffect(() => {
    fetchAnnouncements(0);
    connectAnnouncementSocket((data) => {
      if (Array.isArray(data)) {
        setAnnouncements(data);
        setpage(0);
      }
    });
    return () => disconnectAnnouncementSocket();
  }, []);

  // 搜尋公告
  const handleSearch = (keyword, startDate, endDate) => {
    const params = new URLSearchParams({ keyword, startDate, endDate });
    return fetchAnnouncements(0, `/search?${params.toString()}`);
  };

  //編輯公告
  const handleEdit = (item) => {
    setEditId(item.announcementId);
    setEditingData(item);
    setIsCreating(false);
  };

  //刪除公告
  const handleDelete = async (id) => {
    if (window.confirm("確定要刪除這則公告嗎?")) {
      try {
        const res = await authFetch(`${API_BASE}/admin/${id}`, {
          method: "DELETE",
          credentials: "include",
        });

        if (!res.ok) throw new Error("刪除失敗");
        alert("公告刪除成功");
        await fetchAnnouncements();
      } catch (err) {
        console.error("刪除公告失敗：", err);
        alert("刪除公告時發生錯誤");
      }
    }
  };

  //新增或編輯送出
  const handleFormSubmit = async (data) => {
    const url = editId ? `${API_BASE}/admin/${editId}` : `${API_BASE}/admin`;
    const method = editId ? "PUT" : "POST";

    try {
      const response = await authFetch(url, {
        method,
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ title: data.title, content: data.content }),
      });

      if (!response.ok) {
        throw new Error("伺服器錯誤!");
      }

      let newItem = await response.json();

      //更新公告
      if (
        editId &&
        editingData &&
        editingData.announcementActive !== data.announcementActive
      ) {
        const activeChage = await authFetch(
          `${API_BASE}/admin/${editId}/active?active=${data.announcementActive}`,
          {
            method: "PUT",
            credentials: "include",
          }
        );
        if (!activeChage.ok) throw new Error("更新公告失敗");
        newItem = await activeChage.json();
      }

      alert(`公告已成功${editId ? "編輯" : "新增"}`);

      resetForm();
      await fetchAnnouncements();
    } catch (error) {
      console.error("送出失敗：", error);
      alert("發生錯誤，請稍後再試");
    }
  };

  //重製表單
  const resetForm = () => {
    setEditId(null);
    setEditingData(null);
    setIsCreating(false);
  };

  //表單畫面
  return (
    <div className="admin-page">
      <h1>公告管理</h1>
      <AnnouncementSearchForm onSearch={handleSearch} />
      {/* 在沒有進行新增或編輯時顯示"新增公告" */}
      {!isCreating && !editId && (
        <div className="control-bar">
          <button onClick={() => setIsCreating(true)}>新增公告</button>
          {/* 換頁 */}
          <div className="page-controls">
            <button
              disabled={page === 0}
              onClick={() => fetchAnnouncements(page - 1)}
            >
              上一頁
            </button>
            <span>第 {page + 1} 頁</span>
            <button
              disabled={announcements.length < 10}
              onClick={() => fetchAnnouncements(page + 1)}
            >
              下一頁
            </button>
          </div>
        </div>
      )}

      {/* 顯示表單 */}
      {(isCreating || editId) && (
        <AnnouncementForm
          initialData={editingData || {}}
          mode={editId ? "edit" : "create"}
          onSubmit={handleFormSubmit}
          onCancel={resetForm}
        />
      )}

      {/* 公告表單 */}
      <ul className="announcement-list">
        {announcements.map((a) => (
          <li key={a.announcementId}>
            <strong>{a.title}</strong>(
            {new Date(a.createdTime).toLocaleString("zh-TW")})
            <div>
              <button onClick={() => handleEdit(a)}>編輯</button>
              <button onClick={() => handleDelete(a.announcementId)}>
                刪除
              </button>
            </div>
          </li>
        ))}
      </ul>

      <HomeButton />
    </div>
  );
};

export default AnnouncementAdminPage;
