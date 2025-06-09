import React, { useState, useEffect } from "react";
import AnnouncementForm from "../../forms/AnnouncementForm";
import "./AnnouncementAdminPage.css";

const API_BASE = "http://localhost:8088/api/announcements";

const AnnouncementAdminPage = () => {
  const [announcements, setAnnouncements] = useState([]);
  const [editId, setEditId] = useState(null);
  const [editingData, setEditingData] = useState(null);
  const [isCreating, setIsCreating] = useState(false);

  const fetchAnnouncements = async () => {
    try {
      const res = await fetch(`${API_BASE}`);
      if (!res.ok) throw new Error("載入失敗");
      const data = await res.json();
      setAnnouncements(data);
    } catch (err) {
      console.error("公告載入錯誤：", err);
      alert("無法載入公告資料");
    }
  };

  useEffect(() => {
    fetchAnnouncements();
  }, []);

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
        const res = await fetch(`${API_BASE}/admin/${id}`, {
          method: "DELETE",
          credentials: "include",
        });

        if (!res.ok) throw new Error("刪除失敗");

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
      const response = await fetch(url, {
        method,
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ title: data.title, content: data.content }),
      });

      if (!response.ok) {
        throw new Error("伺服器錯誤!");
      }

      await fetchAnnouncements();

      let newItem = await response.json();

      //更新公告
      if (
        editId &&
        editingData &&
        editingData.announcementActive !== data.announcementActive
      ) {
        const activeChage = await fetch(
          `${API_BASE}/admin/${editId}/active?active=${data.announcementActive}`,
          {
            method: "PUT",
            credentials: "include",
          }
        );
        if (!activeChage.ok) throw new Error("更新公告失敗");
        newItem = await activeChage.json();
      }

      resetForm();
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
      {/* 在沒有進行新增或編輯時顯示"新增公告" */}
      {!isCreating && !editId && (
        <button onClick={() => setIsCreating(true)}>新增公告</button>
      )}

      {/* 顯示表單 */}
      {(isCreating || editId) && (
        <AnnouncementForm
          initialData={editingData || {}}
          mode={editId ? "edit" : "create"}
          onSubmit={handleFormSubmit}
        />
      )}

      {/* 公告表單 */}
      <ul className="announcement-list">
        {announcements.map((a) => (
          <li key={a.announcementId}>
            <strong>{a.title}</strong>({a.createdTime})
            <div>
              <button onClick={() => handleEdit(a)}>編輯</button>
              <button onClick={() => handleDelete(a.announcementId)}>
                刪除
              </button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default AnnouncementAdminPage;
