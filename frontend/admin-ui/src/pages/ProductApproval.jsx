import { useState, useEffect } from 'react';

const ProductApproval = () => {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        setTimeout(() => {
            setProducts([
                { id: 1, name: 'Laptop Pro 15', category: 'Electronics', price: 1299.99, seller: 'Tech Store', status: 'PENDING' },
                { id: 2, name: 'Summer Dress', category: 'Fashion', price: 49.99, seller: 'Fashion Hub', status: 'APPROVED' },
                { id: 3, name: 'Coffee Table', category: 'Furniture', price: 199.99, seller: 'Home Decor', status: 'PENDING' }
            ]);
            setLoading(false);
        }, 1000);
    }, []);

    const handleApprove = (productId) => {
        setProducts(products.map(product =>
            product.id === productId ? { ...product, status: 'APPROVED' } : product
        ));
    };

    const handleReject = (productId) => {
        if (window.confirm('Are you sure you want to reject this product?')) {
            setProducts(products.map(product =>
                product.id === productId ? { ...product, status: 'REJECTED' } : product
            ));
        }
    };

    if (loading) {
        return <div className="loading"><div className="spinner"></div></div>;
    }

    return (
        <div className="table-container">
            <div className="table-header">
                <h2>Product Approval ({products.filter(p => p.status === 'PENDING').length} Pending)</h2>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Product Name</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Seller</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {products.map(product => (
                        <tr key={product.id}>
                            <td>{product.id}</td>
                            <td>{product.name}</td>
                            <td>{product.category}</td>
                            <td>${product.price}</td>
                            <td>{product.seller}</td>
                            <td>
                                <span className={`badge ${product.status === 'APPROVED' ? 'success' :
                                        product.status === 'PENDING' ? 'warning' : 'danger'
                                    }`}>
                                    {product.status}
                                </span>
                            </td>
                            <td>
                                {product.status === 'PENDING' && (
                                    <>
                                        <button
                                            onClick={() => handleApprove(product.id)}
                                            className="btn-action btn-approve"
                                        >
                                            Approve
                                        </button>
                                        <button
                                            onClick={() => handleReject(product.id)}
                                            className="btn-action btn-reject"
                                        >
                                            Reject
                                        </button>
                                    </>
                                )}
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default ProductApproval;
