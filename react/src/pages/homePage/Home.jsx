import React, { useContext, useEffect, useState } from "react";
import AuthContext from "../../contexts/AuthContext.js";
import NavButton from "../../components/NavButton";
import LoginForm from "../../forms/LoginForm";
import LatestAnnouncements from "../../components/LatestAnnouncements";
import { connectSocket, disconnectSocket } from "../../utils/socket";
import "./Home.css";

const API_BASE = "http://localhost:8088/api";

const Home = () => {
  const { user, loading, logout } = useContext(AuthContext);
  const [notificationCount, setNotificationCount] = useState(0);

  useEffect(() => {
    if (!user) {
      setNotificationCount(0);
      disconnectSocket();
      return;
    }

    const checkNotifications = async () => {
      try {
        const resNoti = await fetch(`${API_BASE}/notifications`, {
          credentials: "include",
        });
        let list1 = [];
        if (resNoti.ok) {
          const data = await resNoti.json();
          if (data.status === 200) list1 = data.data;
        }

        const resSwap = await fetch(`${API_BASE}/swap/received`, {
          credentials: "include",
        });
        let list2 = [];
        if (resSwap.ok) {
          const data = await resSwap.json();
          if (data.status === 200) list2 = data.data;
        }
        setNotificationCount(list1.length + list2.length);
      } catch (err) {
        console.error("通知取得失敗", err);
      }
    };

    connectSocket(user.userId, () => {
      setNotificationCount((c) => c + 1);
    });

    checkNotifications();

    return () => {
      disconnectSocket();
    };
  }, [user]);

  // 判斷用戶權限
  const isAdmin = user?.role === 2; //role=2 是 ADMIN
  const isUser = user?.role === 1 || user?.role === 2; // role=1 是 USER，role=2 是 ADMIN

  // if (loading) {
  //   return <div className="loading">載入中...</div>;
  // }

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
                  <NavButton
                    to="/profile"
                    label="個人中心"
                    notificationCount={notificationCount}
                  />
                  <NavButton to="/schedule" label="班表" />
                </>
              )}

              {isAdmin && (
                <>
                  <NavButton to="/announcements/admin" label="公告管理" />
                  <NavButton to="/manageaccount" label="員工管理" />
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
