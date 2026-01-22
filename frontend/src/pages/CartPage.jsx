import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import cartService from '../services/cartService';
import { formatCurrency } from '../utils/formatters';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';
import './CartPage.css';

const CartPage = () => {
    const navigate = useNavigate();
    const [cart, setCart] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [updating, setUpdating] = useState({});

    useEffect(() => {
        loadCart();
    }, []);

    const loadCart = async () => {
        try {
            setLoading(true);
            setError(null);
            const data = await cartService.getCart();
            setCart(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const handleUpdateQuantity = async (itemId, newQuantity) => {
        if (newQuantity < 1) return;

        try {
            setUpdating(prev => ({ ...prev, [itemId]: true }));
            await cartService.updateCartItem(itemId, newQuantity);
            await loadCart();
        } catch (err) {
            alert('Failed to update quantity: ' + err.message);
        } finally {
            setUpdating(prev => ({ ...prev, [itemId]: false }));
        }
    };

    const handleRemoveItem = async (itemId) => {
        if (!confirm('Remove this item from cart?')) return;

        try {
            await cartService.removeFromCart(itemId);
            await loadCart();
        } catch (err) {
            alert('Failed to remove item: ' + err.message);
        }
    };

    const handleClearCart = async () => {
        if (!confirm('Clear entire cart?')) return;

        try {
            await cartService.clearCart();
            await loadCart();
        } catch (err) {
            alert('Failed to clear cart: ' + err.message);
        }
    };

    if (loading) return <Loading />;
    if (error) return <ErrorMessage message={error} onRetry={loadCart} />;

    const items = cart?.items || [];
    const isEmpty = items.length === 0;

    return (
        <div className="cart-page">
            <div className="container">
                <div className="cart-header">
                    <h1>Shopping Cart</h1>
                    {!isEmpty && (
                        <button className="btn btn-sm btn-danger" onClick={handleClearCart}>
                            Clear Cart
                        </button>
                    )}
                </div>

                {isEmpty ? (
                    <div className="empty-cart glass-card">
                        <div className="empty-cart-icon">🛒</div>
                        <h2>Your cart is empty</h2>
                        <p>Add some products to get started!</p>
                        <Link to="/products" className="btn btn-primary">
                            Browse Products
                        </Link>
                    </div>
                ) : (
                    <div className="cart-layout">
                        {/* Cart Items */}
                        <div className="cart-items">
                            {items.map((item) => (
                                <div key={item.id} className="cart-item glass-card">
                                    <div className="cart-item-image">
                                        {item.product?.imageUrl ? (
                                            <img src={item.product.imageUrl} alt={item.product.name} />
                                        ) : (
                                            <div className="cart-item-placeholder">📦</div>
                                        )}
                                    </div>

                                    <div className="cart-item-info">
                                        <Link to={`/products/${item.product?.id}`} className="cart-item-name">
                                            {item.product?.name}
                                        </Link>
                                        <p className="cart-item-price">{formatCurrency(item.price)}</p>
                                    </div>

                                    <div className="cart-item-quantity">
                                        <button
                                            className="quantity-btn"
                                            onClick={() => handleUpdateQuantity(item.id, item.quantity - 1)}
                                            disabled={updating[item.id]}
                                        >
                                            −
                                        </button>
                                        <span className="quantity-value">{item.quantity}</span>
                                        <button
                                            className="quantity-btn"
                                            onClick={() => handleUpdateQuantity(item.id, item.quantity + 1)}
                                            disabled={updating[item.id]}
                                        >
                                            +
                                        </button>
                                    </div>

                                    <div className="cart-item-total">
                                        <p className="total-label">Total</p>
                                        <p className="total-value">{formatCurrency(item.totalPrice)}</p>
                                    </div>

                                    <button
                                        className="cart-item-remove"
                                        onClick={() => handleRemoveItem(item.id)}
                                        title="Remove item"
                                    >
                                        ✕
                                    </button>
                                </div>
                            ))}
                        </div>

                        {/* Cart Summary */}
                        <div className="cart-summary glass-card">
                            <h2>Order Summary</h2>

                            <div className="summary-row">
                                <span>Subtotal ({items.length} items)</span>
                                <span>{formatCurrency(cart.totalAmount)}</span>
                            </div>

                            <div className="summary-row">
                                <span>Shipping</span>
                                <span className="text-success">FREE</span>
                            </div>

                            <div className="summary-divider"></div>

                            <div className="summary-row summary-total">
                                <span>Total</span>
                                <span>{formatCurrency(cart.totalAmount)}</span>
                            </div>

                            <button
                                className="btn btn-lg btn-primary"
                                onClick={() => navigate('/checkout')}
                                style={{ width: '100%' }}
                            >
                                Proceed to Checkout
                            </button>

                            <Link to="/products" className="continue-shopping">
                                ← Continue Shopping
                            </Link>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default CartPage;
