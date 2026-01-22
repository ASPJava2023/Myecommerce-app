import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import cartService from '../services/cartService';
import orderService from '../services/orderService';
import { formatCurrency } from '../utils/formatters';
import { isRequired, isValidPhone, isValidPinCode } from '../utils/validators';
import Loading from '../components/Loading';
import ErrorMessage from '../components/ErrorMessage';
import './CheckoutPage.css';

const CheckoutPage = () => {
    const navigate = useNavigate();
    const [cart, setCart] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [processing, setProcessing] = useState(false);

    const [formData, setFormData] = useState({
        addressLine1: '',
        addressLine2: '',
        city: '',
        state: '',
        pinCode: '',
        phone: '',
    });

    const [errors, setErrors] = useState({});

    useEffect(() => {
        loadCart();
        loadRazorpayScript();
    }, []);

    const loadCart = async () => {
        try {
            setLoading(true);
            const data = await cartService.getCart();
            if (!data || !data.items || data.items.length === 0) {
                navigate('/cart');
                return;
            }
            setCart(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const loadRazorpayScript = () => {
        return new Promise((resolve) => {
            const script = document.createElement('script');
            script.src = 'https://checkout.razorpay.com/v1/checkout.js';
            script.onload = () => resolve(true);
            script.onerror = () => resolve(false);
            document.body.appendChild(script);
        });
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
        if (errors[name]) {
            setErrors(prev => ({ ...prev, [name]: '' }));
        }
    };

    const validate = () => {
        const newErrors = {};

        if (!isRequired(formData.addressLine1)) {
            newErrors.addressLine1 = 'Address is required';
        }

        if (!isRequired(formData.city)) {
            newErrors.city = 'City is required';
        }

        if (!isRequired(formData.state)) {
            newErrors.state = 'State is required';
        }

        if (!isValidPinCode(formData.pinCode)) {
            newErrors.pinCode = 'Invalid PIN code';
        }

        if (!isValidPhone(formData.phone)) {
            newErrors.phone = 'Invalid phone number';
        }

        return newErrors;
    };

    const handleRazorpayPayment = async (orderId, amount) => {
        const options = {
            key: 'rzp_test_YOUR_KEY_ID', // Replace with your Razorpay key
            amount: amount * 100, // Razorpay expects amount in paise
            currency: 'INR',
            name: 'ShopSphere',
            description: `Order #${orderId}`,
            order_id: orderId, // This should come from backend Razorpay order creation
            handler: async function (response) {
                // Payment successful
                alert('Payment successful! Order ID: ' + orderId);
                navigate(`/orders/${orderId}`);
            },
            prefill: {
                contact: formData.phone,
            },
            theme: {
                color: '#667eea',
            },
            modal: {
                ondismiss: function () {
                    setProcessing(false);
                    alert('Payment cancelled');
                }
            }
        };

        const razorpay = new window.Razorpay(options);
        razorpay.open();
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const newErrors = validate();
        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
            return;
        }

        setProcessing(true);

        try {
            // Create order
            const orderData = {
                shippingAddress: {
                    addressLine1: formData.addressLine1,
                    addressLine2: formData.addressLine2,
                    city: formData.city,
                    state: formData.state,
                    pinCode: formData.pinCode,
                    phone: formData.phone,
                },
                paymentMethod: 'RAZORPAY',
            };

            const order = await orderService.createOrder(orderData);

            // Initiate Razorpay payment
            await handleRazorpayPayment(order.id, order.totalAmount);

        } catch (err) {
            alert('Failed to create order: ' + err.message);
            setProcessing(false);
        }
    };

    if (loading) return <Loading />;
    if (error) return <ErrorMessage message={error} onRetry={loadCart} />;

    return (
        <div className="checkout-page">
            <div className="container">
                <h1>Checkout</h1>

                <div className="checkout-layout">
                    {/* Shipping Form */}
                    <div className="checkout-form-section">
                        <div className="glass-card">
                            <h2>Shipping Address</h2>

                            <form onSubmit={handleSubmit} className="checkout-form">
                                <div className="form-group">
                                    <label htmlFor="addressLine1" className="form-label">Address Line 1 *</label>
                                    <input
                                        type="text"
                                        id="addressLine1"
                                        name="addressLine1"
                                        className={`form-input ${errors.addressLine1 ? 'error' : ''}`}
                                        value={formData.addressLine1}
                                        onChange={handleChange}
                                        placeholder="Street address, P.O. box"
                                    />
                                    {errors.addressLine1 && <span className="form-error">{errors.addressLine1}</span>}
                                </div>

                                <div className="form-group">
                                    <label htmlFor="addressLine2" className="form-label">Address Line 2</label>
                                    <input
                                        type="text"
                                        id="addressLine2"
                                        name="addressLine2"
                                        className="form-input"
                                        value={formData.addressLine2}
                                        onChange={handleChange}
                                        placeholder="Apartment, suite, unit, building, floor, etc."
                                    />
                                </div>

                                <div className="form-row">
                                    <div className="form-group">
                                        <label htmlFor="city" className="form-label">City *</label>
                                        <input
                                            type="text"
                                            id="city"
                                            name="city"
                                            className={`form-input ${errors.city ? 'error' : ''}`}
                                            value={formData.city}
                                            onChange={handleChange}
                                        />
                                        {errors.city && <span className="form-error">{errors.city}</span>}
                                    </div>

                                    <div className="form-group">
                                        <label htmlFor="state" className="form-label">State *</label>
                                        <input
                                            type="text"
                                            id="state"
                                            name="state"
                                            className={`form-input ${errors.state ? 'error' : ''}`}
                                            value={formData.state}
                                            onChange={handleChange}
                                        />
                                        {errors.state && <span className="form-error">{errors.state}</span>}
                                    </div>
                                </div>

                                <div className="form-row">
                                    <div className="form-group">
                                        <label htmlFor="pinCode" className="form-label">PIN Code *</label>
                                        <input
                                            type="text"
                                            id="pinCode"
                                            name="pinCode"
                                            className={`form-input ${errors.pinCode ? 'error' : ''}`}
                                            value={formData.pinCode}
                                            onChange={handleChange}
                                            placeholder="400001"
                                        />
                                        {errors.pinCode && <span className="form-error">{errors.pinCode}</span>}
                                    </div>

                                    <div className="form-group">
                                        <label htmlFor="phone" className="form-label">Phone *</label>
                                        <input
                                            type="tel"
                                            id="phone"
                                            name="phone"
                                            className={`form-input ${errors.phone ? 'error' : ''}`}
                                            value={formData.phone}
                                            onChange={handleChange}
                                            placeholder="9876543210"
                                        />
                                        {errors.phone && <span className="form-error">{errors.phone}</span>}
                                    </div>
                                </div>

                                <button
                                    type="submit"
                                    className="btn btn-lg btn-primary"
                                    disabled={processing}
                                    style={{ width: '100%' }}
                                >
                                    {processing ? 'Processing...' : '💳 Proceed to Payment'}
                                </button>
                            </form>
                        </div>
                    </div>

                    {/* Order Summary */}
                    <div className="checkout-summary">
                        <div className="glass-card">
                            <h2>Order Summary</h2>

                            <div className="summary-items">
                                {cart.items.map((item) => (
                                    <div key={item.id} className="summary-item">
                                        <div className="summary-item-info">
                                            <span className="summary-item-name">{item.product?.name}</span>
                                            <span className="summary-item-qty">× {item.quantity}</span>
                                        </div>
                                        <span className="summary-item-price">{formatCurrency(item.totalPrice)}</span>
                                    </div>
                                ))}
                            </div>

                            <div className="summary-divider"></div>

                            <div className="summary-row">
                                <span>Subtotal</span>
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

                            <div className="payment-info">
                                <p>🔒 Secure payment powered by Razorpay</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default CheckoutPage;
