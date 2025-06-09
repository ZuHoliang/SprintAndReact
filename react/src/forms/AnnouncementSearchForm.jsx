import React, { useState, useEffect } from "react";

const AnnouncementSearchForm = ({ onSearch }) => {
  const [keyword, setKeyword] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [error, setError] = useState("");
  const [isSearching, setIsSearching] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    // 驗證：起始日期不能晚於結束日期
    if (startDate && endDate && new Date(startDate) > new Date(endDate)) {
      setError("起始日期不能晚於結束日期");
      return;
    }
    setError("");
    setIsSearching(true);

    onSearch(keyword, startDate, endDate);
  };
  const handleReset = () => {
    setKeyword("");
    setStartDate("");
    setEndDate("");
    setError("");
  };

  return (
    <form onSubmit={handleSubmit} className="announcement-search-form">
      <input
        type="text"
        placeholder="輸入標題關鍵字"
        value={keyword}
        onChange={(e) => setKeyword(e.target.value)}
      />
      <input
        type="date"
        value={startDate}
        onChange={(e) => setStartDate(e.target.value)}
      />

      <input
        type="date"
        value={endDate}
        onChange={(e) => setEndDate(e.target.value)}
      />

      <button type="submit" disabled={isSearching}>
        {isSearching ? "搜尋中..." : "搜尋"}
      </button>
      <button type="button" onClick={handleReset}>
        重設
      </button>

      {error && <p style={{ color: "red" }}>{error}</p>}
    </form>
  );
};
export default AnnouncementSearchForm;
