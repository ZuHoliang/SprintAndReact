import React, { useState, useEffect } from "react";
import SwapReplyDialog from "./SwapReplyDialog";
import "./SwapRequestCard.css";

//顯示單筆換班通知
const SwapRequestCard = ({ request, isReceived, onReply, onCancel }) => {
  const [replyOpen, setReplyOpen] = useState(false);
  const [error, setError] = useState("");
  const [submiting, setSubmiting] = useState(false);

  //送出同意
  const handleApprove = async (message) => {
    setSubmiting(true);
    const result = await onReply(request.shiftSwapId, true, message);
    if (result?.moderation) {
      setError("*內容不合適");
    } else if (result?.success) {
      setReplyOpen(false);
      setError("");
    }
    setSubmiting(false);
  };

  //送出拒絕
  const handleReject = async (message) => {
    setSubmiting(true);
    const result = await onReply(request.shiftSwapId, false, message);
    if (result?.moderation) {
      setError("*內容不合適");
    } else if (result?.success) {
      setReplyOpen(false);
      setError("");
    }
    setSubmiting(false);
  };

  return (
    <div className="swap-card">
      <div className="card-content">
        <div>
          <strong>{isReceived ? "申請人" : "被申請人"} :</strong>
          {isReceived ? request.requestUsername : request.targetUsername}
        </div>
        <div>
          <strong>班別:</strong>
          {request.swapDate}
          {request.swapToShift === "MORNING" ? "早班" : "晚班"}
        </div>
        {request.swapMessage && (
          <div>
            <strong>留言:</strong>
            {request.swapMessage}
          </div>
        )}
      </div>
      {/* 是否是接收方 */}
      {isReceived ? (
        <div className="card-actions">
          <button onClick={() => setReplyOpen(true)} disabled={submiting}>同意 / 拒絕 </button>
        </div>
      ) : (
        <div className="card-actions">
          <button onClick={() => onCancel(request.shiftSwapId)} disabled={submiting}>
            取消申請
          </button>
        </div>
      )}

      {replyOpen && (
        <SwapReplyDialog
          onApprove={handleApprove}
          onReject={handleReject}
          onClose={() => {
            setReplyOpen(false);
            setError("");
          }}
          error={error}
          submitting={submiting}
        />
      )}
    </div>
  );
};

export default SwapRequestCard;
