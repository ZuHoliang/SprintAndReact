import React from "react";
import { Link } from "react-router-dom";
import "./HomeButton.css";

const HomeButton = () => {
  return (
    <Link to="/" className="home-button">
      回首頁
    </Link>
  );
};

export default HomeButton;
