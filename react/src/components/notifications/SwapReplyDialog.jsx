import React, { useState, useEffect } from "react";
import "./SwapReplyDialog.css";

const SwapReplyDialog = ({ onApprove, onReject, onClose, error, submitting }) => {
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
        {error && <div className="field-error">{error}</div>}
        <div className="dialog-actions">
          <button onClick={() => onApprove(message)} disabled = {submitting}>{submitting? "審核中..." : "同意"}</button>
          <button onClick={() => onReject(message)} disabled = {submitting}>{submitting? "審核中..." : "拒絕"}</button>
          <button onClick={onClose} disabled={submitting}>取消</button>
        </div>
      </div>
    </div>
  );
};

export default SwapReplyDialog;
