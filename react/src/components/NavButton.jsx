import { Link } from 'react-router-dom';
import './NavButton.css'; 

const NavButton = ({ to, label }) => {
  return (
    <Link to={to} className="nav-button">
      {label}
    </Link>
  );
};

export default NavButton;