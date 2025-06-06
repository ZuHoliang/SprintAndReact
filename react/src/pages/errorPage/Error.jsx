import { useNavigate } from "react-router-dom";

const Error = ({ message = "發生未知錯誤", status = 500 }) => {
  const navigate = useNavigate();

  return (
    <div className="p-6 text-center">
      <h1 className="text-3xl font-bold text-red-600 mb-4">錯誤 {status}</h1>
      <p className="text-lg mb-6">{message}</p>
      <button
        onClick={() => navigate("/")}
        className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition"
      >
        回首頁
      </button>
    </div>
  );
};

export default Error;
