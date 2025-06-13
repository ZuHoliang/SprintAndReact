import React, { useState, useEffect } from "react";
import "./AnnouncementForm.css";

const AnnouncementForm = ({ initialData, onSubmit, onCancel, mode }) => {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [active, setActive] = useState(true);

  //填上原始內容
  useEffect(() => {
    const data = initialData || {};
    setTitle(data.title || "");
    setContent(data.content || "");
    setActive(
      data.announcementActive === undefined ?  true : data.announcementActive
    );
  }, [initialData])

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (title.trim().length < 3) {
      alert("標題至少需要 3 個字");
      return;
    }
    await onSubmit({
      title,
      content,
      announcementActive: active,
    });
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

      <div className="form-actions">
      <button type="submit">{mode === "create" ? "新增" : "儲存修改"}</button>
      {onCancel && (
        <button type="button" className="cancel-button" onClick={onCancel}>
          取消
          </button>
      )}
      </div>
    </form>
  );
};

export default AnnouncementForm;
