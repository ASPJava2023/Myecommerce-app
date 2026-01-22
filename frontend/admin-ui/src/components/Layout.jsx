import { Outlet, useLocation } from 'react-router-dom';
import Sidebar from './Sidebar';
import Header from './Header';

const Layout = () => {
    const location = useLocation();

    const getPageTitle = () => {
        const path = location.pathname;
        const titles = {
            '/dashboard': 'Dashboard',
            '/users': 'User Management',
            '/sellers': 'Seller Management',
            '/products': 'Product Approval',
            '/categories': 'Category Management',
            '/orders': 'Order Management',
            '/reports': 'Reports',
            '/analytics': 'Analytics',
            '/settings': 'Settings'
        };
        return titles[path] || 'Admin Panel';
    };

    return (
        <div className="admin-layout">
            <Sidebar />
            <div className="main-content">
                <Header title={getPageTitle()} />
                <div className="content">
                    <Outlet />
                </div>
            </div>
        </div>
    );
};

export default Layout;
