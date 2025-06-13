import React, { useState, useEffect } from "react";
import SwapReplyDialog from "./SwapReplyDialog";
import "./SwapRequestCard.css";

//顯示單筆換班通知
const SwapRequestCard = ({ request, isReceived, onReply, onCancel }) => {
  const [replyOpen, setReplyOpen] = useState(false);

  //送出同意
  const handleApprove = (message) => {
    onReply(request.shiftSwapId, true, message);
    setReplyOpen(false);
  };

  //送出拒絕
  const handleReject = (message) => {
    onReply(request.shiftSwapId, false, message);
    setReplyOpen(false);
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
          <button onClick={() => setReplyOpen(true)}>同意 / 拒絕 </button>
        </div>
      ) : (
        <div className="card-actions">
          <button onClick={() => onCancel(request.shiftSwapId)}>
            取消申請
          </button>
        </div>
      )}

      {replyOpen && (
        <SwapReplyDialog
          onApprove={handleApprove}
          onReject={handleReject}
          onClose={() => setReplyOpen(false)}
        />
      )}
    </div>
  );
};

export default SwapRequestCard;
