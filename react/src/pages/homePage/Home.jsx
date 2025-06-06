import React, { useContext, useEffect, useState } from "react";
import { AuthContext } from "../../contexts/AuthContext";
import NavButton from "../../components/NavButton";
import LoginForm from "../../components/LoginForm";
import LatestAnnouncements from "../../components/LatestAnnouncements";
import "./Home.css";

const API_BASE = "http://localhost:8088/api";

const Home = () => {
  const { user, loading, logout } = useContext(AuthContext);

  // 判斷用戶權限
  const isAdmin = user?.role === 2; //role=2 是 ADMIN
  const isUser = user?.role === 1 || user?.role === 2; // role=1 是 USER，role=2 是 ADMIN

  if (loading) {
    return <div className="loading">載入中...</div>;
  }

  return (
    <div className="page-container">
      <h1 className="page-title">員工管理系統首頁</h1>

      {/* 左側公告區，*/}
      <div className="content-row">
        <div className="page-left-panel">
          <LatestAnnouncements />
        </div>

        {/* 右側使用者登入 */}
        <div className="right-panel">
          {!user ? (
            <>
              <h2 className="page-title">員工登入</h2>
              <LoginForm />
            </>
          ) : (
            <>
              <h2 className="page-title">歡迎 {user.username || user.name}!</h2>
              {(isUser || isAdmin) && (
                <>
                  <NavButton to="/profile" label="個人資料" />
                  <NavButton to="/schedule" label="班表" />
                </>
              )}

              {isAdmin && (
                <>
                  <NavButton to="/announcements" label="編輯公告" />
                  <NavButton to="/manageAccount" label="管理員工" />
                </>
              )}

              <button onClick={logout} className="logout-button">
                登出
              </button>
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default Home;
