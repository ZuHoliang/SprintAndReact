import { useState, useEffect } from "react";
import useAuthFetch from "../../utils/useAuthFetch";
import HomeButton from "../../components/HomeButton";
import ScheduleSummary from "../../components/notifications/ScheduleSummary";
import SwapNotificationSection from "../../components/notifications/SwapNotificationSection";
import NotificationList from "../../components/notifications/NotificationList";
import "./Profile.css";

const API_BASE = "http://localhost:8088/api";

const Profile = () => {
  const [user, setUser] = useState(null);
  const authFetch = useAuthFetch();
  const [mode, setMode] = useState("view"); //view=預設頁面|editName:修改名字|editPassword:修改密碼
  const [editName, setEditName] = useState("");
  const [editPassword, setEditPassword] = useState({
    password: "",
    confirmPassword: "",
  });
  
  const [refreshKey, setRefreshKey] = useState(0);
  const triggerRefresh = () => setRefreshKey((k) => k + 1);
  useEffect(() => {
    authFetch(`${API_BASE}/users/me`, { credentials: "include" })
      .then((res) => {
        if (!res.ok) throw new Error("無法取得使用者資料");
        return res.json();
      })
      .then((data) => {
        setUser(data);
        setEditName(data.username);
      })

      .catch(() => alert("無法取得使用者資料"));
  }, []);
  //密碼限限制
  const validatePassword = (password) =>
    /[a-z]/.test(password) && /[A-Z]/.test(password) && /\d/.test(password);

  //修改名稱(不再需要密碼)
  const handleUpdateName = async () => {
    //使用者名稱長度>2
    if (editName.length < 2) return alert("使用者名稱請至少2個字");
    
    try {
      const res = await authFetch(`${API_BASE}/users/me`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({ username: editName }),
      });
      if (!res.ok) throw new Error();
      alert("使用者名稱修改成功");
      setMode("view");
      setEditName("");
      setPasswordVerify("");
      const updatedUser = await res.json();
      setUser(updatedUser);
    } catch {
      alert("修改失敗");
    }
  };

  //修改密碼
  const handleUpdatePassword = async () => {
    const { password, confirmPassword } = editPassword;
    if (password !== confirmPassword) return alert("確認密碼與新密碼不一致");
    if (!validatePassword(password)) return alert("密碼請包含英文大小寫與數字");
    try {
      const res = await authFetch(`${API_BASE}/users/me`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({ username: user.username, password }),
      });
      if (!res.ok) throw new Error();
      alert("密碼修改成功");
      setMode("view");
      setEditPassword({ password: "", confirmPassword: "" });
    } catch {
      alert("修改失敗");
    }
  };

  if (!user) return <div>載入中...</div>;

  return (
    <div className="user-profile-container">
      <div className="left-panel">
        <h3>通知中心</h3>
        <NotificationList onClose={triggerRefresh} />
        <SwapNotificationSection onUpdated={triggerRefresh}/>
      </div>
      <div className="right-panel">
        <h2>個人資訊</h2>
        <p>
          <strong>使用者名稱:{user.username}</strong>
        </p>
        <p className="user-role">{user.role === 2 ? "管理者" : "一般員工"}</p>
        <div>
          <ScheduleSummary refreshKey={refreshKey} />
        </div>
        <h3>修改個人資料</h3>
        {mode === "view" && (
          <div className="button-group">
            <button onClick={() => setMode("editName")}>修改使用者名稱</button>
            <button onClick={() => setMode("editPassword")}>修改密碼</button>
          </div>
        )}

        {/* 修改名字- */}
        {mode === "editName" && (
          <div className="form-group">
            <label>新使用者名稱:</label>
            <input
              value={editName}
              onChange={(e) => setEditName(e.target.value)}
            />
           
            <div className="button-group">
              <button onClick={handleUpdateName}> 確認修改</button>
              <button onClick={() => setMode("view")}>取消修改</button>
            </div>
          </div>
        )}

        {/* 修改密碼 */}
        {mode === "editPassword" && (
          <div className="form-group">
            <label>新密碼:</label>
            <input
              type="password"
              value={editPassword.password}
              onChange={(e) =>
                setEditPassword({ ...editPassword, password: e.target.value })
              }
            />
            <label>確認密碼:</label>
            <input
              type="password"
              value={editPassword.confirmPassword}
              onChange={(e) =>
                setEditPassword({
                  ...editPassword,
                  confirmPassword: e.target.value,
                })
              }
            />

            <div className="button-group">
              <button onClick={handleUpdatePassword}>確認修改</button>
              <button onClick={() => setMode("view")}>取消修改</button>
            </div>
          </div>
        )}
      </div>
      <HomeButton />
    </div>
  );
};

export default Profile;
