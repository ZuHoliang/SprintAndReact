import React, { useState, useEffect } from "react";
import "./AnnouncementForm.css";

const AnnouncementForm = (props) => {
  // 【修改】確保 initialData 不會是 null 或 undefined
  const initialData = props.initialData ?? {};
  const [title, setTitle] = useState(initialData.title || "");
  const [content, setContent] = useState(initialData.content || "");
  const [active, setActive] = useState(initialData.announcementActive ?? true);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (title.trim().length < 3) {
      alert("標題至少需要 3 個字");
      return;
    }
    onSubmit({ title, content, announcementActive: active });
  };
  return (
    <form className="announcement-form" onSubmit={handleSubmit}>
      <h2>{mode === "create" ? "新增公告" : "編輯公告"}</h2>
      <label>標題</label>
      <input
        type="text"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        required
      />

      <label>內容</label>
      <textarea
        value={content}
        onChange={(e) => setContent(e.target.value)}
        required
      />

      <label>
        <input
          type="checkbox"
          checked={active}
          onChange={(e) => setActive(e.target.checked)}
        />
        顯示公告
      </label>

      <button type="submit">{mode === "create" ? "新增" : "儲存修改"}</button>
    </form>
  );
};

export default AnnouncementForm;
