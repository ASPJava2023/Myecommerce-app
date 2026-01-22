import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import orderService from '../services/orderService';
import { formatCurrency, formatDateTime } from '../utils/formatters';
import { ORDER_STATUS } from '../config/constants';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';
import './OrderDetailPage.css';

const OrderDetailPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [order, setOrder] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [cancelling, setCancelling] = useState(false);

    useEffect(() => {
        loadOrder();
    }, [id]);

    const loadOrder = async () => {
        try {
            setLoading(true);
            setError(null);
            const data = await orderService.getOrderById(id);
            setOrder(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const handleCancelOrder = async () => {
        if (!confirm('Are you sure you want to cancel this order?')) return;

        try {
            setCancelling(true);
            await orderService.cancelOrder(id);
            await loadOrder();
            alert('Order cancelled successfully');
        } catch (err) {
            alert('Failed to cancel order: ' + err.message);
        } finally {
            setCancelling(false);
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

    const canCancelOrder = (status) => {
        return status === ORDER_STATUS.PENDING || status === ORDER_STATUS.CONFIRMED;
    };

    if (loading) return <Loading />;
    if (error) return <ErrorMessage message={error} onRetry={loadOrder} />;
    if (!order) return <ErrorMessage message="Order not found" />;

    return (
        <div className="order-detail-page">
            <div className="container">
                <button className="back-btn" onClick={() => navigate('/orders')}>
                    ← Back to Orders
                </button>

                <div className="order-detail-header">
                    <div>
                        <h1>Order #{order.id}</h1>
                        <p className="order-date">Placed on {formatDateTime(order.orderDate)}</p>
                    </div>
                    <span className={`badge ${getStatusBadgeClass(order.status)}`}>
                        {order.status}
                    </span>
                </div>

                <div className="order-detail-layout">
                    {/* Order Items */}
                    <div className="order-items-section glass-card">
                        <h2>Order Items</h2>
                        <div className="order-items-list">
                            {order.items?.map((item, index) => (
                                <div key={index} className="order-detail-item">
                                    <div className="item-image">
                                        {item.product?.imageUrl ? (
                                            <img src={item.product.imageUrl} alt={item.product.name} />
                                        ) : (
                                            <div className="item-placeholder">📦</div>
                                        )}
                                    </div>
                                    <div className="item-info">
                                        <h3>{item.product?.name || 'Product'}</h3>
                                        <p className="item-price">{formatCurrency(item.price)} × {item.quantity}</p>
                                    </div>
                                    <div className="item-total">
                                        {formatCurrency(item.totalPrice)}
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>

                    {/* Order Summary */}
                    <div className="order-summary-section">
                        <div className="glass-card">
                            <h2>Order Summary</h2>

                            <div className="summary-row">
                                <span>Subtotal</span>
                                <span>{formatCurrency(order.totalAmount)}</span>
                            </div>

                            <div className="summary-row">
                                <span>Shipping</span>
                                <span className="text-success">FREE</span>
                            </div>

                            <div className="summary-divider"></div>

                            <div className="summary-row summary-total">
                                <span>Total</span>
                                <span>{formatCurrency(order.totalAmount)}</span>
                            </div>
                        </div>

                        {/* Shipping Address */}
                        <div className="glass-card">
                            <h2>Shipping Address</h2>
                            <div className="address-info">
                                <p>{order.shippingAddress?.addressLine1}</p>
                                {order.shippingAddress?.addressLine2 && (
                                    <p>{order.shippingAddress.addressLine2}</p>
                                )}
                                <p>
                                    {order.shippingAddress?.city}, {order.shippingAddress?.state} {order.shippingAddress?.pinCode}
                                </p>
                                <p>Phone: {order.shippingAddress?.phone}</p>
                            </div>
                        </div>

                        {/* Payment Info */}
                        <div className="glass-card">
                            <h2>Payment</h2>
                            <p className="payment-method">
                                {order.paymentMethod || 'Razorpay'}
                            </p>
                            <p className="payment-status">
                                Status: <span className={order.paymentStatus === 'COMPLETED' ? 'text-success' : 'text-warning'}>
                                    {order.paymentStatus || 'PENDING'}
                                </span>
                            </p>
                        </div>

                        {/* Actions */}
                        {canCancelOrder(order.status) && (
                            <button
                                className="btn btn-danger"
                                onClick={handleCancelOrder}
                                disabled={cancelling}
                                style={{ width: '100%' }}
                            >
                                {cancelling ? 'Cancelling...' : 'Cancel Order'}
                            </button>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default OrderDetailPage;
