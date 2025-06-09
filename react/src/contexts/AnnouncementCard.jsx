//公告摘要
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./AnnouncementCard.css";

const AnnouncementCard = ({ announcement }) => {
  const navigate = useNavigate();
  const handleClick = () => {
    navigate(`/announcement/${announcement.announcementId}`);
  };
  return (
    <div className="announcement-card" onClick={handleClick}>
      <h3>{announcement.title} </h3>
      <p>{announcement.content.slice(0, 20)}...</p>
      <p>
        <small>
          發布時間：{new Date(announcement.createdTime).toLocaleString()}
        </small>
      </p>
    </div>
  );
};

export default AnnouncementCard;
