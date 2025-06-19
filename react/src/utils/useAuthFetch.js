import { useNavigate } from "react-router-dom";
import { useContext, useCallback } from "react";
import AuthContext from "../contexts/AuthContext.js";

const useAuthFetch = () => {
  const navigate = useNavigate();
  const { clearUser } = useContext(AuthContext);

  return useCallback(
    async (url, options = {}) => {
      const headers = {
        Accept: "application/json",
        ...(options.headers || {}),
      };
      const response = await fetch(url, { ...options, headers });
      if (response.status === 401) {
        if (clearUser) clearUser(); //登出
        navigate("/"); //導回首頁
        return Promise.reject({ unauthorized: true });
      }
      return response;
    },
    [clearUser, navigate]
  );
};
export default useAuthFetch;
