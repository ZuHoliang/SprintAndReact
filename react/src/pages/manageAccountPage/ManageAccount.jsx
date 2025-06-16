import { useEffect, useState } from "react";
import HomeButton from "../../components/HomeButton";
import useAuthFetch from "../../utils/useAuthFetch";
import "./ManageAccount.css";

const API_BASE = "http://localhost:8088/api";

const ManageAccount = () => {
  const [users, setUser] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [searchId, setSearchId] = useState(""); //搜尋
  const [selectedUser, setSelectedUser] = useState(null); //編輯模式
  //編輯狀態預設
  const [editData, setEditData] = useState({
    password: "",
    confirmPassword: "",
  });
  //新增時預設
  const [newUser, setNewUser] = useState({
    username: "",
    password: "",
    confirmPassword: "",
    isAdmin: false, //true = admin(2)
    active: true,
  });

  const authFetch = useAuthFetch();

  useEffect(() => {
    fetchUsers();
    // 清空欄位
    setNewUser({
      username: "",
      password: "",
      confirmPassword: "",
      isAdmin: false,
      active: true,
    });
    setEditData({
      password: "",
      confirmPassword: "",
      role: 1,
      active: true,
    });
  }, []);

  const fetchUsers = async () => {
    try {
      setLoading(true);
      const res = await authFetch(`${API_BASE}/admin/users`, {
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

  //使用者名稱限制
  const validateUsername = (username) => username.length >= 2;
  //密碼限制
  const validatePassword = (password) =>
    /[A-Z]/.test(password) &&
    /[a-z]/.test(password) &&
    /\d/.test(password) &&
    password.length > 3;

  const handleSearchEdit = () => {
    const found = users.find((u) => u.accountId === searchId.trim());
    if (found) {
      setSelectedUser(found);
      setEditData({
        password: "",
        confirmPassword: "",
        role: found.role,
        active: found.active,
      });
    } else {
      alert("找不到該員工");
    }
    setSearchId("");
  };

  const handleUpdate = async () => {
    if (!selectedUser) return;
    // 如果密碼欄位有填，才做驗證
    if (editData.password.trim()) {
      if (!validatePassword(editData.password)) {
        alert("密碼格式錯誤，需包含大小寫與數字，且長度需大於3字");
        return;
      }
      if (editData.password !== editData.confirmPassword) {
        alert("確認密碼與新密碼不一致");
        return;
      }
    }

    try {
      const updates = [];
      if (editData.password.trim()) {
        //修改密碼
        updates.push(
          authFetch(
            `${API_BASE}/admin/users/${selectedUser.accountId}/password`,
            {
              method: "PUT",
              headers: { "Content-Type": "application/json" },
              credentials: "include",
              body: JSON.stringify({ password: editData.password }),
            }
          )
        );
      }
      //修改權限
      updates.push(
        authFetch(`${API_BASE}/admin/users/${selectedUser.accountId}/role`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
          body: JSON.stringify({ role: editData.role }),
        })
      );
      //修改在職狀態
      updates.push(
        authFetch(`${API_BASE}/admin/users/${selectedUser.accountId}/active`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
          body: JSON.stringify({ active: editData.active }),
        })
      );
      await Promise.all(updates);
      alert("修改成功");
      await fetchUsers();
      setSelectedUser(null);
    } catch (err) {
      alert("更新失敗" + err.message);
    }
  };
  const handleCreate = async () => {
    if (!validateUsername(newUser.username)) {
      alert("使用者名稱需大於2個字");
      return;
    }
    if (!validatePassword(newUser.password)) {
      alert("密碼需包含大小寫與數字，且長度需大於3字");
      return;
    }
    if (newUser.password !== newUser.confirmPassword) {
      alert("確認密碼與密碼不一致");
      return;
    }
    try {
      const res = await authFetch(`${API_BASE}/admin/users`, {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          username: newUser.username,
          password: newUser.password,
          role: newUser.isAdmin ? 2 : 1,
          active: newUser.active,
        }),
      });
      if (!res.ok) throw new Error("新增失敗");
      const createdUser = await res.json();
      alert(`新增成功，權限: ${createdUser.role === 2 ? "管理者" : "員工"}`);
      await fetchUsers();
      setNewUser({
        username: "",
        password: "",
        confirmPassword: "",
        isAdmin: false,
        active: true,
      });
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
          <div className="search-box">
            <label>搜尋員工:</label>
            <input
              type="text"
              placeholder="輸入員工編號"
              value={searchId}
              onChange={(e) => setSearchId(e.target.value)}
            />
            <button onClick={handleSearchEdit}>編輯</button>
          </div>
          {loading ? (
            <p>載入中...</p>
          ) : error ? (
            <p className="error">{error}</p>
          ) : (
            <table>
              <thead>
                <tr>
                  <th>員工編號</th>
                  <th>使用者名稱</th>
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
                          setEditData({
                            password: "",
                            confirmPassword: "",
                            role: user.role,
                            active: user.active,
                          });
                          setNewUser({
                            username: "",
                            password: "",
                            confirmPassword: "",
                            isAdmin: false,
                            active: true,
                          });
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
              <button
                onClick={() => {
                  setSelectedUser(null);
                  setNewUser({
                    username: "",
                    password: "",
                    confirmPassword: "",
                    isAdmin: false,
                    active: true,
                  });
                  setEditData({
                    password: "",
                    confirmPassword: "",
                    role: 1,
                    active: true,
                  });
                }}
              >
                新增員工
              </button>
            )}
          </div>

          {selectedUser ? (
            <>
              <div>
                <label>員工編號：{selectedUser.accountId}</label>
              </div>

              <div>
                <label>使用者名稱：{selectedUser.username}</label>
              </div>
              <div>
                <label>新密碼：</label>
                <input
                  type="password"
                  name="password"
                  autoComplete="new-password"
                  value={editData.password}
                  onChange={(e) =>
                    setEditData({ ...editData, password: e.target.value })
                  }
                />
              </div>

              <div>
                <label>確認密碼：</label>
                <input
                  type="password"
                  value={editData.confirmPassword}
                  onChange={(e) =>
                    setEditData({
                      ...editData,
                      confirmPassword: e.target.value,
                    })
                  }
                />
              </div>

              <div>
                <label>
                  <input
                    type="checkbox"
                    checked={editData.role === 2}
                    onChange={(e) => {
                      setEditData({
                        ...editData,
                        role: e.target.checked ? 2 : 1,
                      });
                    }}
                  />
                  管理者
                </label>
              </div>
              <div>
                <label>
                  <input
                    type="checkbox"
                    checked={editData.active}
                    onChange={(e) => {
                      setEditData({ ...editData, active: e.target.checked });
                    }}
                  />
                  在職
                </label>
              </div>
              <button onClick={handleUpdate}>確認修改</button>
            </>
          ) : (
            <>
              <div>
                <label>使用者名稱：</label>
                <input
                  type="text"
                  name="username"
                  autoComplete="off"
                  value={newUser.username}
                  onChange={(e) =>
                    setNewUser({ ...newUser, username: e.target.value })
                  }
                />
              </div>

              <div>
                <label>密碼：</label>
                <input
                  type="password"
                  name="password"
                  autoComplete="new-password"
                  value={newUser.password}
                  onChange={(e) =>
                    setNewUser({ ...newUser, password: e.target.value })
                  }
                />
              </div>

              <div>
                <label>確認密碼：</label>
                <input
                  type="password"
                  name="confirmPassword"
                  autoComplete="new-password"
                  value={newUser.confirmPassword}
                  onChange={(e) =>
                    setNewUser({ ...newUser, confirmPassword: e.target.value })
                  }
                />
              </div>

              <div>
                <label>
                  <input
                    type="checkbox"
                    checked={newUser.isAdmin}
                    onChange={(e) =>
                      setNewUser({ ...newUser, isAdmin: e.target.checked })
                    }
                  />
                  管理者
                </label>
              </div>
              <div>
                <label>
                  <input
                    type="checkbox"
                    checked={newUser.active}
                    onChange={(e) =>
                      setNewUser({ ...newUser, active: e.target.checked })
                    }
                  />
                  在職
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
