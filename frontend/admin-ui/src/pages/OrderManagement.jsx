import { useState, useEffect } from 'react';

const OrderManagement = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        setTimeout(() => {
            setOrders([
                { id: 1, orderNumber: 'ORD-001', customer: 'John Doe', total: 299.99, status: 'PENDING', date: '2024-01-20' },
                { id: 2, orderNumber: 'ORD-002', customer: 'Jane Smith', total: 149.99, status: 'SHIPPED', date: '2024-01-21' },
                { id: 3, orderNumber: 'ORD-003', customer: 'Bob Johnson', total: 499.99, status: 'DELIVERED', date: '2024-01-22' }
            ]);
            setLoading(false);
        }, 1000);
    }, []);

    const handleStatusUpdate = (orderId, newStatus) => {
        setOrders(orders.map(order =>
            order.id === orderId ? { ...order, status: newStatus } : order
        ));
    };

    if (loading) {
        return <div className="loading"><div className="spinner"></div></div>;
    }

    return (
        <div className="table-container">
            <div className="table-header">
                <h2>Order Management ({orders.length})</h2>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>Order #</th>
                        <th>Customer</th>
                        <th>Total</th>
                        <th>Status</th>
                        <th>Date</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {orders.map(order => (
                        <tr key={order.id}>
                            <td>{order.orderNumber}</td>
                            <td>{order.customer}</td>
                            <td>${order.total}</td>
                            <td>
                                <span className={`badge ${order.status === 'DELIVERED' ? 'success' :
                                        order.status === 'SHIPPED' ? 'info' :
                                            order.status === 'PENDING' ? 'warning' : 'danger'
                                    }`}>
                                    {order.status}
                                </span>
                            </td>
                            <td>{order.date}</td>
                            <td>
                                <select
                                    value={order.status}
                                    onChange={(e) => handleStatusUpdate(order.id, e.target.value)}
                                    style={{ padding: '0.375rem', borderRadius: '4px', border: '1px solid #D1D5DB' }}
                                >
                                    <option value="PENDING">Pending</option>
                                    <option value="PROCESSING">Processing</option>
                                    <option value="SHIPPED">Shipped</option>
                                    <option value="DELIVERED">Delivered</option>
                                    <option value="CANCELLED">Cancelled</option>
                                </select>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default OrderManagement;
