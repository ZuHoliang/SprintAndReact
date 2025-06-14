import React, { useState, useEffect } from "react";
import AuthContext from "./AuthContext.js";

const API_BASE = "http://localhost:8088/api";

const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  //檢查登入
  const checkLoginStatus = async () => {
    try {
      const response = await fetch(`${API_BASE}/auth/check`, {
        method: "GET",
        credentials: "include", //session cookie
        headers: {
          "Content-Type": "application/json",
        },
      });
      if (response.ok) {
        const data = await response.json();
        if (data.status === 200 && data) {
          setUser(data.data);
        } else {
          // 未登入也當成成功，但 user 為 null
          setUser(null);
        }
      }
    } catch (error) {
      console.error("檢查登入狀態失敗:", error);
    } finally {
      setLoading(false);
    }
  };

  //登入
  const login = async (accountId, password, rememberMe = false) => {
    try {
      // setLoading(true);
      const formData = new FormData();
      formData.append("accountId", accountId);
      formData.append("password", password);
      formData.append("rememberMe", rememberMe);

      const response = await fetch(`${API_BASE}/auth/login`, {
        method: "POST",
        credentials: "include", // session cookie
        body: formData, // FormData傳送
      });

      const data = await response.json();
      if (response.ok && data.status === 200) {
        setUser(data.data);
        return { success: true, message: data.message };
      } else {
        return {
          success: false,
          message: data.message || "登入失敗",
          errorCode: data.errorCode || null,
        };
      }
    } catch (error) {
      console.error("登入錯誤", error);
      return { success: false, message: "網路錯誤，請稍後再試" };
    } finally {
      setLoading(false);
    }
  };

  //登出
  const logout = async () => {
    try {
      const response = await fetch(`${API_BASE}/auth/logout`, {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
      });

      const data = await response.json();

      if (response.ok) {
        setUser(null);
        return { success: true, message: data.message };
      } else {
        return { success: false, message: data.message || "登出失敗" };
      }
    } catch (error) {
      console.error("登出錯誤:", error);
      return { success: false, message: "網路錯誤，請稍後再試" };
    }
  };

  //載入時檢查登入狀態
  useEffect(() => {
    checkLoginStatus();
  }, []);

  const clearUser = () => setUser(null);

  const value = {
    user,
    login,
    logout,
    clearUser,
    loading,
    checkLoginStatus,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export default AuthProvider;
