import { useNavigate } from 'react-router-dom';
import authService from '../services/authService';

const Header = ({ title }) => {
    const navigate = useNavigate();
    const user = authService.getCurrentUser();

    const handleLogout = () => {
        authService.logout();
        navigate('/login');
    };

    return (
        <div className="header">
            <h1>{title}</h1>
            <div className="header-actions">
                <div className="user-info">
                    <span>Welcome, {user?.firstName || 'Admin'}</span>
                </div>
                <button onClick={handleLogout} className="btn-logout">
                    Logout
                </button>
            </div>
        </div>
    );
};

export default Header;
