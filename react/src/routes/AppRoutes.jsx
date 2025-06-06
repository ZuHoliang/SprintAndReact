import { Route, Routes } from "react-router-dom";
import Home from "../pages/homePage/Home";
import Announcements from "../pages/announcementsPage/Announcements";
import Profile from "../pages/profilePage/Profile";
import ErrorPage from "../pages/errorPage/Error";

const AppRoutes = () => (
  <Routes>
    <Route path="/" element={<Home />} />
    <Route path="/announcements" element={<Announcements />} />
    <Route path="/profile" element={<Profile />} />

    <Route path="*" element={<ErrorPage status={404} message="找不到網頁" />} />
  </Routes>
);

export default AppRoutes;
