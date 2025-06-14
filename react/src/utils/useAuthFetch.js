import { useNavigate } from "react-router-dom";
import { useContext } from "react";
import { AuthContext } from "../contexts/AuthContext";

const useAuthFetch = () => {
  const navigate = useNavigate();
  const { clearUser } = useContext(AuthContext);

  return async (url, options = {}) => {
    const response = await fetch(url, options);
    if (response.status === 401) {
      if (clearUser) clearUser(); //登出
      navigate("/"); //導回首頁
      return Promise.reject({ unauthorized: true });
    }
    return response;
  };
};
export default useAuthFetch;
