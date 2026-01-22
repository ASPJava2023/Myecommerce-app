import { NavLink } from 'react-router-dom';

const Sidebar = () => {
    return (
        <div className="sidebar">
            <div className="sidebar-header">
                <h2>Admin Panel</h2>
            </div>
            <nav className="sidebar-nav">
                <NavLink to="/dashboard" className="nav-item">
                    📊 Dashboard
                </NavLink>
                <NavLink to="/users" className="nav-item">
                    👥 User Management
                </NavLink>
                <NavLink to="/sellers" className="nav-item">
                    🏪 Seller Management
                </NavLink>
                <NavLink to="/products" className="nav-item">
                    📦 Product Approval
                </NavLink>
                <NavLink to="/categories" className="nav-item">
                    🏷️ Categories
                </NavLink>
                <NavLink to="/orders" className="nav-item">
                    🛒 Orders
                </NavLink>
                <NavLink to="/reports" className="nav-item">
                    📈 Reports
                </NavLink>
                <NavLink to="/analytics" className="nav-item">
                    📉 Analytics
                </NavLink>
                <NavLink to="/settings" className="nav-item">
                    ⚙️ Settings
                </NavLink>
            </nav>
        </div>
    );
};

export default Sidebar;
