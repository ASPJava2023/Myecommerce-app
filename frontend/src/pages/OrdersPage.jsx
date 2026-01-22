import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import orderService from '../services/orderService';
import { formatCurrency, formatDateTime } from '../utils/formatters';
import { ORDER_STATUS } from '../config/constants';
import Pagination from '../components/Pagination';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';
import './OrdersPage.css';

const OrdersPage = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    useEffect(() => {
        loadOrders();
    }, [currentPage]);

    const loadOrders = async () => {
        try {
            setLoading(true);
            setError(null);
            const data = await orderService.getUserOrders(currentPage);
            setOrders(data.content || []);
            setTotalPages(data.totalPages || 0);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const getStatusBadgeClass = (status) => {
        switch (status) {
            case ORDER_STATUS.DELIVERED:
                return 'badge-success';
            case ORDER_STATUS.CANCELLED:
                return 'badge-error';
            case ORDER_STATUS.PENDING:
                return 'badge-warning';
            default:
                return 'badge-info';
        }
    };

    if (loading) return <Loading />;
    if (error) return <ErrorMessage message={error} onRetry={loadOrders} />;

    return (
        <div className="orders-page">
            <div className="container">
                <h1>My Orders</h1>

                {orders.length === 0 ? (
                    <div className="no-orders glass-card">
                        <div className="no-orders-icon">📦</div>
                        <h2>No orders yet</h2>
                        <p>Start shopping to see your orders here!</p>
                        <Link to="/products" className="btn btn-primary">
                            Browse Products
                        </Link>
                    </div>
                ) : (
                    <>
                        <div className="orders-list">
                            {orders.map((order) => (
                                <div key={order.id} className="order-card glass-card">
                                    <div className="order-header">
                                        <div className="order-info">
                                            <h3>Order #{order.id}</h3>
                                            <p className="order-date">{formatDateTime(order.orderDate)}</p>
                                        </div>
                                        <span className={`badge ${getStatusBadgeClass(order.status)}`}>
                                            {order.status}
                                        </span>
                                    </div>

                                    <div className="order-items">
                                        {order.items?.slice(0, 3).map((item, index) => (
                                            <div key={index} className="order-item">
                                                <span>{item.product?.name || 'Product'}</span>
                                                <span className="order-item-qty">× {item.quantity}</span>
                                            </div>
                                        ))}
                                        {order.items?.length > 3 && (
                                            <p className="order-more">+{order.items.length - 3} more items</p>
                                        )}
                                    </div>

                                    <div className="order-footer">
                                        <div className="order-total">
                                            <span>Total:</span>
                                            <span className="total-amount">{formatCurrency(order.totalAmount)}</span>
                                        </div>
                                        <Link to={`/orders/${order.id}`} className="btn btn-sm btn-secondary">
                                            View Details
                                        </Link>
                                    </div>
                                </div>
                            ))}
                        </div>

                        <Pagination
                            currentPage={currentPage}
                            totalPages={totalPages}
                            onPageChange={setCurrentPage}
                        />
                    </>
                )}
            </div>
        </div>
    );
};

export default OrdersPage;
