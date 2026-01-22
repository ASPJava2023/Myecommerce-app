import { LineChart, Line, BarChart, Bar, PieChart, Pie, Cell, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

const Analytics = () => {
    const userGrowthData = [
        { month: 'Jan', users: 100 },
        { month: 'Feb', users: 150 },
        { month: 'Mar', users: 200 },
        { month: 'Apr', users: 280 },
        { month: 'May', users: 350 },
        { month: 'Jun', users: 450 }
    ];

    const productPerformanceData = [
        { category: 'Electronics', sales: 4500 },
        { category: 'Fashion', sales: 3200 },
        { category: 'Furniture', sales: 2100 },
        { category: 'Books', sales: 1800 }
    ];

    const revenueData = [
        { name: 'Electronics', value: 45000 },
        { name: 'Fashion', value: 32000 },
        { name: 'Furniture', value: 21000 },
        { name: 'Books', value: 18000 }
    ];

    const COLORS = ['#4F46E5', '#10B981', '#F59E0B', '#EF4444'];

    return (
        <div>
            <div className="chart-container">
                <h3>User Growth Trend</h3>
                <ResponsiveContainer width="100%" height={300}>
                    <LineChart data={userGrowthData}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="month" />
                        <YAxis />
                        <Tooltip />
                        <Legend />
                        <Line type="monotone" dataKey="users" stroke="#4F46E5" strokeWidth={2} />
                    </LineChart>
                </ResponsiveContainer>
            </div>

            <div className="chart-container">
                <h3>Product Performance by Category</h3>
                <ResponsiveContainer width="100%" height={300}>
                    <BarChart data={productPerformanceData}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="category" />
                        <YAxis />
                        <Tooltip />
                        <Legend />
                        <Bar dataKey="sales" fill="#10B981" />
                    </BarChart>
                </ResponsiveContainer>
            </div>

            <div className="chart-container">
                <h3>Revenue Distribution</h3>
                <ResponsiveContainer width="100%" height={300}>
                    <PieChart>
                        <Pie
                            data={revenueData}
                            cx="50%"
                            cy="50%"
                            labelLine={false}
                            label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                            outerRadius={100}
                            fill="#8884d8"
                            dataKey="value"
                        >
                            {revenueData.map((entry, index) => (
                                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                            ))}
                        </Pie>
                        <Tooltip />
                    </PieChart>
                </ResponsiveContainer>
            </div>
        </div>
    );
};

export default Analytics;
