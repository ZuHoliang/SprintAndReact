import { useEffect, useState } from "react";
import HomeButton from "../../components/HomeButton";
import "./ManageAccount.css";

const API_BASE = "http://localhost:8088/api";

const ManageAccount = () => {
  const [users, setUser] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [selectedUser, setSelectedUser] = useState(null); //編輯模式
  const [passwordInput, setPasswordInput] = useState("");
  const [newUser, setNewUser] = useState({
    accountId: "",
    password: "",
    role: 1,
    active: true,
  });

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      setLoading(true);
      const res = await fetch(`${API_BASE}/admin/users`, {
        method: "GET",
        credentials: "include",
      });
      if (!res.ok) {
        throw new Error("取得員工資料失敗");
      }

      const data = await res.json();
      setUser(data);
    } catch (err) {
      setError("無法取得員工資料");
    } finally {
      setLoading(false);
    }
  };
  const handleUpdate = async (field, value) => {
    if (!selectedUser) return;
    let endpoint = "";
    const body = {};
    if (field === "password") {
      endpoint = `/admin/users/${selectedUser.accountId}/password`;
      body.password = value;
    } else if (field === "role") {
      endpoint = `/admin/users/${selectedUser.accountId}/role`;
      body.role = value;
    } else if (field === "active") {
      endpoint = `/admin/users/${selectedUser.accountId}/active`;
    }
    try {
      const res = await fetch(`${API_BASE}${endpoint}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify(body),
      });
      if (!res.ok) {
        const errorData = await res.json();
        throw new Error(errorData.message || "更新失敗");
      }
      const data = await res.json();
      setSelectedUser(data); // 假設後端回傳的是更新後的該使用者物件

      await fetchUsers(); // 成功後重新載入使用者列表
    } catch (err) {
      alert("更新失敗" + err.message);
    }
  };
  const handleCreate = async () => {
    if (!newUser.username || !newUser.password) {
      alert("帳號與密碼不可為空");
      return;
    }
    try {
      const res = await fetch(`${API_BASE}/admin/users`, {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          username: newUser.username,
          password: newUser.password,
          role: newUser.role ? 2 : 1,
          active: newUser.active,
        }),
      });
      if (!res.ok) throw new Error("新增失敗");
      fetchUsers();
      setNewUser({ username: "", password: "", role: false, active: true });
    } catch (err) {
      alert("新增失敗: " + err.message);
    }
  };

  return (
    <div className="admin-user-page">
      <h1>員工管理</h1>
      <div className="admin-user-layout">
        <div className="user-list">
          <h2>員工列表</h2>
          {loading ? (
            <p>載入中...</p>
          ) : error ? (
            <p className="error">{error}</p>
          ) : (
            <table>
              <thead>
                <tr>
                  <th>員工編號</th>
                  <th>員工姓名</th>
                  <th>權限</th>
                  <th>狀態</th>
                  <th>編輯員工</th>
                </tr>
              </thead>
              <tbody>
                {users.map((user) => (
                  <tr key={user.accountId}>
                    <td>{user.accountId}</td>
                    <td>{user.username}</td>
                    <td>{user.role === 2 ? "管理者" : "員工"}</td>
                    <td>{user.active ? "在職" : "離職"}</td>
                    <td>
                      <button
                        onClick={() => {
                          setSelectedUser(user);
                          setPasswordInput("");
                        }}
                      >
                        編輯
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
        <div className="user-form">
          <div className="form-header">
            <h2>{selectedUser ? "編輯員工" : "新增員工"}</h2>
            {selectedUser && (
              <button onClick={() => setSelectedUser(null)}>新增員工</button>
            )}
          </div>

          {selectedUser ? (
            <>
              <div>
                <label>帳號：{selectedUser.accountId}</label>
              </div>
              <div>
                <label>新密碼：</label>
                <input
                  type="password"
                  value={passwordInput}
                  onChange={(e) => setPasswordInput(e.target.value)}
                />
              </div>
              <div>
                <label>
                  <input
                    type="checkbox"
                    checked={selectedUser.role === 2}
                    onChange={(e) =>
                      handleUpdate("role", e.target.checked ? 2 : 1)
                    }
                  />
                  管理者
                </label>
              </div>
              <div>
                <label>
                  <input
                    type="checkbox"
                    checked={selectedUser.active}
                    onChange={(e) => handleUpdate("active", e.target.checked)}
                  />
                  啟用
                </label>
              </div>
              <button
                onClick={() => {
                  if (passwordInput.trim()) {
                    handleUpdate("password", passwordInput);
                  }
                }}
              >
                確認修改
              </button>
            </>
          ) : (
            <>
              <div>
                <label>帳號：</label>
                <input type="text" disabled placeholder="尚未實作" />
              </div>
              <div>
                <label>密碼：</label>
                <input type="password" disabled placeholder="尚未實作" />
              </div>
              <div>
                <label>
                  <input type="checkbox" disabled /> 管理者
                </label>
              </div>
              <div>
                <label>
                  <input type="checkbox" disabled /> 啟用
                </label>
              </div>
              <button onClick={handleCreate}>確認新增</button>
            </>
          )}
        </div>
      </div>
      <HomeButton />
    </div>
  );
};

export default ManageAccount;
