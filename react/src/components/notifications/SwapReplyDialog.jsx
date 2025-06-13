import React, { useState, useEffect } from "react";
// import "./SwapReplyDialog.css"

const SwapReplyDialog = ({ onApprove, onReject, onClose }) => {
  const [message, setMessage] = useState("");

  return (
    <div className="dialog-overlay">
      <div className="dialog-box">
        <h3>換班請求</h3>
        <textarea
          placeholder="可輸入回覆訊息"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
        />
        <div className="dialog-actions">
          <button onClick={() => onApprove(message)}>同意</button>
          <button onClick={() => onReject(message)}>拒絕</button>
          <button onClick={onClose}>取消</button>
        </div>
      </div>
    </div>
  );
};

export default SwapReplyDialog;
