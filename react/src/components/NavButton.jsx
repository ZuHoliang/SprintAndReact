import { Link } from "react-router-dom";
import "./NavButton.css";

const NavButton = ({ to, label, notificationCount = 0 }) => {
  const hasNotification = notificationCount > 0;
  const className = `nav-button${hasNotification ? " has-notification" : ""}`;
  return (
    <Link
      to={to}
      className={className}
      data-count={hasNotification ? notificationCount : undefined}
    >
      {label}
    </Link>
  );
};

export default NavButton;
