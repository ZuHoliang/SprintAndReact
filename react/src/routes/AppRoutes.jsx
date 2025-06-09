import { Route, Routes } from "react-router-dom";
{
  /* 首頁 */
}
import Home from "../pages/homePage/Home";
{
  /* 公告區 */
}
import Announcements from "../pages/announcementsPage/Announcements";
import AnnouncementDetailPage from "../pages/announcementsPage/AnnouncementDetailPage";
import AnnouncementAdminPage from "../pages/announcementsPage/AnnouncementAdminPage";
{
  /* 個人資料 */
}
import Profile from "../pages/profilePage/Profile";
import ManageAccount from "../pages/manageAccountPage/ManageAccount";
{
  /* 班表 */
}
import Schedule from "../pages/schedulePage/Schedule";
import ErrorPage from "../pages/errorPage/Error";

const AppRoutes = () => (
  <Routes>
    {/* 首頁 */}
    <Route path="/" element={<Home />} />
    {/* 公告區 */}
    <Route path="/announcements" element={<Announcements />} />
    <Route path="/announcements/all" element={<Announcements />} />
    <Route path="/announcement/:id" element={<AnnouncementDetailPage />} />
    <Route path="/announcements/admin" element={<AnnouncementAdminPage />} />
    {/* 個人資料 */}
    <Route path="/profile" element={<Profile />} />
    <Route path="/manageaccount" element={<ManageAccount />} />
    {/* 班表 */}
    <Route path="/schedule" element={<Schedule />} />
    <Route path="*" element={<ErrorPage status={404} message="找不到網頁" />} />
  </Routes>
);

export default AppRoutes;
