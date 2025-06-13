import React, { useState, useEffect } from "react";
import "./ShiftMemberList.css";

//顯示目前班表上排班的人
const ShiftMemberList = ({ members, selectedId, onSelect }) => {
  return (
    <div className="member-list">
      <p>目前排班人員:</p>
      {members.length === 0 ? (
        <p>無</p>
      ) : (
        members.map((member) => (
          <label key={member.userId} className="member-item">
            <input
              type="radio"
              name="swapTarget"
              value={member.userId}
              checked={selectedId === member.userId}
              onChange={() => onSelect(member.userId)}
            />
            {member.accountId}({member.username})
          </label>
        ))
      )}
    </div>
  );
};

export default ShiftMemberList;
