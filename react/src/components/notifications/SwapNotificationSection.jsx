import React, { useEffect, useState } from "react";
import useAuthFetch from "../../utils/useAuthFetch";
import SwapRequestCard from "./SwapRequestCard";

const API_BASE = "http://localhost:8088/api";

const SwapNotificationSection = ({onUpdated}) => {
  const [receivedRequests, setReceivedRequests] = useState([]);
  const [sentRequests, setSentRequests] = useState([]);
  const authFetch = useAuthFetch();

  const fetchRequest = () => {
    authFetch(`${API_BASE}/swap/received`, { credentials: "include" })
      .then((res) => res.json())
      .then((data) => {
        if (data.status === 200) setReceivedRequests(data.data);
        else alert("載入失敗");
      })
      .catch(() => alert("連線失敗"));

    authFetch(`${API_BASE}/swap/sent`, { credentials: "include" })
      .then((res) => res.json())
      .then((data) => {
        if (data.status === 200) setSentRequests(data.data);
        else alert("載入失敗");
      })
      .catch(() => alert("連線失敗"));
  };

  useEffect(() => {
    fetchRequest();
  }, []);

  const handleReject = async (id, isApprove, message) => {
    const action = isApprove ? "approve" : "reject";
    const res = await authFetch(
      `${API_BASE}/swap/${id}/${action}?message=${encodeURIComponent(message)}`,
      {
        method: "POST",
        credentials: "include",
      }
    );

    const data = await res.json();
    if (res.ok) {
      alert(isApprove ? "已同意換班" : "已拒絕換班");
      fetchRequest();
      onUpdated && onUpdated();
    return{success: true};
    }
    if (
      data.message && (data.message.includes("留言不當"))
    ){
      return{success: false, moderation: true};
    }
    alert("取消失敗");
    return{ success: false };
  };

  const handleCancel = async (id) => {
    const res = await authFetch(`${API_BASE}/swap/${id}`, {
      method: "DELETE",
      credentials: "include",
    });
    if (res.ok) {
      alert("已取消換班申請");
      fetchRequest();
      onUpdated && onUpdated();
    } else {
      alert("取消失敗");
    }
  };

  return (
    <div className="swap-notification-section">
      <h4>收到的請求</h4>
      {receivedRequests.length === 0 ? (
        <p>目前無消息</p>
      ) : (
        receivedRequests.map((req) => (
          <SwapRequestCard
            key={req.shiftSwapId}
            request={req}
            isReceived={true}
            onReply={handleReject}
          />
        ))
      )}
      <h4>送出的請求</h4>
      {sentRequests.length === 0 ? (
        <p>目前無送出的請求</p>
      ) : (
        sentRequests.map((req) => (
          <SwapRequestCard
            key={req.shiftSwapId}
            request={req}
            isReceived={false}
            onCancel={handleCancel}
          />
        ))
      )}
    </div>
  );
};

export default SwapNotificationSection;
