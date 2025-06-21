import React, { useState, useEffect } from "react";
import ShiftMemberList from "./ShiftMemberList";
import "./ShiftActionDialog.css";

//發送換班請求表單(彈窗)
const ShiftActionDialog = ({
  open,
  date,
  shiftType,
  members,
  isSelfScheduled,
  onClose,
  onAssign,
  onCancel,
  onRequestSwap,
  submitting,
  error,
}) => {
  const [swapTargetId, setSwapTargetId] = useState(null);
  const [message, setMessage] = useState("");

  useEffect(() => {
    if (open) {
      setSwapTargetId(null);
      setMessage("");
    }
  }, [open, date, shiftType]);

  if (!open) return null;

  const handleAssign = () => {
    onAssign(date, shiftType);
  };

  const handleCancel = () => {
    onCancel(date, shiftType);
  };

  const handleRequestSwap = () => {
    if (swapTargetId) {
      onRequestSwap(date, shiftType, swapTargetId, message);
    } else {
      alert("請則換班對象");
    }
  };

  return (
    <div className="dialog-overlay">
      <div className="dialog-box">
        <h3>
          {date} {shiftType === "MORNING" ? "早班" : "晚班"}
        </h3>
        <ShiftMemberList
          members={members}
          selectedId={swapTargetId}
          onSelect={setSwapTargetId}
        />

        <textarea
          placeholder="留言"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
        />

        {error && <div className="field-error">{error}</div>}

        <div className="dialog-actions">
          {!isSelfScheduled && members.length < 2 && (
            <button onClick={handleAssign}>排班</button>
          )}

          {isSelfScheduled && <button onClick={handleCancel}>取消排班</button>}

          {members.length > 0 && !isSelfScheduled && (
            <button onClick={handleRequestSwap} disabled={submitting}>{submitting ? "審核中..." : "發送換班請求"}</button>
          )}

          <button onClick={onClose}>關閉</button>
        </div>
      </div>
    </div>
  );
};

export default ShiftActionDialog;
