import { useState, useEffect } from 'react';

const SellerManagement = () => {
    const [sellers, setSellers] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        setTimeout(() => {
            setSellers([
                { id: 1, name: 'Tech Store', email: 'tech@store.com', status: 'PENDING', businessName: 'Tech Solutions Ltd', createdAt: '2024-01-10' },
                { id: 2, name: 'Fashion Hub', email: 'fashion@hub.com', status: 'APPROVED', businessName: 'Fashion Hub Inc', createdAt: '2024-01-15' },
                { id: 3, name: 'Home Decor', email: 'home@decor.com', status: 'PENDING', businessName: 'Home Decor Co', createdAt: '2024-02-01' }
            ]);
            setLoading(false);
        }, 1000);
    }, []);

    const handleApprove = (sellerId) => {
        setSellers(sellers.map(seller =>
            seller.id === sellerId ? { ...seller, status: 'APPROVED' } : seller
        ));
    };

    const handleReject = (sellerId) => {
        if (window.confirm('Are you sure you want to reject this seller?')) {
            setSellers(sellers.map(seller =>
                seller.id === sellerId ? { ...seller, status: 'REJECTED' } : seller
            ));
        }
    };

    if (loading) {
        return <div className="loading"><div className="spinner"></div></div>;
    }

    return (
        <div className="table-container">
            <div className="table-header">
                <h2>Seller Management ({sellers.length})</h2>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Seller Name</th>
                        <th>Business Name</th>
                        <th>Email</th>
                        <th>Status</th>
                        <th>Created At</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {sellers.map(seller => (
                        <tr key={seller.id}>
                            <td>{seller.id}</td>
                            <td>{seller.name}</td>
                            <td>{seller.businessName}</td>
                            <td>{seller.email}</td>
                            <td>
                                <span className={`badge ${seller.status === 'APPROVED' ? 'success' :
                                        seller.status === 'PENDING' ? 'warning' : 'danger'
                                    }`}>
                                    {seller.status}
                                </span>
                            </td>
                            <td>{seller.createdAt}</td>
                            <td>
                                {seller.status === 'PENDING' && (
                                    <>
                                        <button
                                            onClick={() => handleApprove(seller.id)}
                                            className="btn-action btn-approve"
                                        >
                                            Approve
                                        </button>
                                        <button
                                            onClick={() => handleReject(seller.id)}
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

export default SellerManagement;
