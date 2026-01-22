import { Link } from 'react-router-dom';
import { formatCurrency } from '../utils/formatters';
import './ProductCard.css';

const ProductCard = ({ product, onAddToCart }) => {
    const handleAddToCart = (e) => {
        e.preventDefault();
        if (onAddToCart) {
            onAddToCart(product);
        }
    };

    return (
        <Link to={`/products/${product.id}`} className="product-card glass-card">
            <div className="product-image-container">
                {product.imageUrl ? (
                    <img src={product.imageUrl} alt={product.name} className="product-image" />
                ) : (
                    <div className="product-image-placeholder">
                        <span className="placeholder-icon">📦</span>
                    </div>
                )}
                {product.featured && (
                    <span className="product-badge">⭐ Featured</span>
                )}
            </div>

            <div className="product-info">
                <h3 className="product-name">{product.name}</h3>
                <p className="product-description">{product.description}</p>

                <div className="product-footer">
                    <div className="product-price">
                        <span className="price-current">{formatCurrency(product.price)}</span>
                        {product.originalPrice && product.originalPrice > product.price && (
                            <span className="price-original">{formatCurrency(product.originalPrice)}</span>
                        )}
                    </div>

                    {product.stockQuantity > 0 ? (
                        <button
                            className="btn btn-sm btn-primary add-to-cart-btn"
                            onClick={handleAddToCart}
                        >
                            Add to Cart
                        </button>
                    ) : (
                        <span className="out-of-stock">Out of Stock</span>
                    )}
                </div>
            </div>
        </Link>
    );
};

export default ProductCard;
