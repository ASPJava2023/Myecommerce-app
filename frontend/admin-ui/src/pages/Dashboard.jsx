import { useState, useEffect } from 'react';
import { LineChart, Line, BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

const Dashboard = () => {
    const [stats, setStats] = useState({
        totalUsers: 0,
        totalProducts: 0,
        totalOrders: 0,
        totalRevenue: 0
    });
    const [loading, setLoading] = useState(true);

    const salesData = [
        { month: 'Jan', sales: 4000, orders: 240 },
        { month: 'Feb', sales: 3000, orders: 198 },
        { month: 'Mar', sales: 5000, orders: 320 },
        { month: 'Apr', sales: 4500, orders: 280 },
        { month: 'May', sales: 6000, orders: 390 },
        { month: 'Jun', sales: 5500, orders: 350 }
    ];

    useEffect(() => {
        // Simulate API call
        setTimeout(() => {
            setStats({
                totalUsers: 1250,
                totalProducts: 450,
                totalOrders: 890,
                totalRevenue: 125000
            });
            setLoading(false);
        }, 1000);
    }, []);

    if (loading) {
        return <div className="loading"><div className="spinner"></div></div>;
    }

    return (
        <div>
            <div className="stats-grid">
                <div className="stat-card">
                    <h3>Total Users</h3>
                    <div className="value">{stats.totalUsers.toLocaleString()}</div>
                    <div className="change positive">+12% from last month</div>
                </div>
                <div className="stat-card">
                    <h3>Total Products</h3>
                    <div className="value">{stats.totalProducts.toLocaleString()}</div>
                    <div className="change positive">+8% from last month</div>
                </div>
                <div className="stat-card">
                    <h3>Total Orders</h3>
                    <div className="value">{stats.totalOrders.toLocaleString()}</div>
                    <div className="change positive">+15% from last month</div>
                </div>
                <div className="stat-card">
                    <h3>Total Revenue</h3>
                    <div className="value">${stats.totalRevenue.toLocaleString()}</div>
                    <div className="change positive">+20% from last month</div>
                </div>
            </div>

            <div className="chart-container">
                <h3>Sales Overview</h3>
                <ResponsiveContainer width="100%" height={300}>
                    <LineChart data={salesData}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="month" />
                        <YAxis />
                        <Tooltip />
                        <Legend />
                        <Line type="monotone" dataKey="sales" stroke="#4F46E5" strokeWidth={2} />
                    </LineChart>
                </ResponsiveContainer>
            </div>

            <div className="chart-container">
                <h3>Orders by Month</h3>
                <ResponsiveContainer width="100%" height={300}>
                    <BarChart data={salesData}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="month" />
                        <YAxis />
                        <Tooltip />
                        <Legend />
                        <Bar dataKey="orders" fill="#10B981" />
                    </BarChart>
                </ResponsiveContainer>
            </div>
        </div>
    );
};

export default Dashboard;
