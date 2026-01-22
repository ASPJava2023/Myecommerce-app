import { useState, useEffect } from 'react';

const UserManagement = () => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        // Simulate API call
        setTimeout(() => {
            setUsers([
                { id: 1, name: 'John Doe', email: 'john@example.com', role: 'USER', status: 'ACTIVE', createdAt: '2024-01-15' },
                { id: 2, name: 'Jane Smith', email: 'jane@example.com', role: 'USER', status: 'ACTIVE', createdAt: '2024-01-20' },
                { id: 3, name: 'Bob Johnson', email: 'bob@example.com', role: 'USER', status: 'INACTIVE', createdAt: '2024-02-01' }
            ]);
            setLoading(false);
        }, 1000);
    }, []);

    const handleStatusToggle = (userId) => {
        setUsers(users.map(user =>
            user.id === userId
                ? { ...user, status: user.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE' }
                : user
        ));
    };

    const handleDelete = (userId) => {
        if (window.confirm('Are you sure you want to delete this user?')) {
            setUsers(users.filter(user => user.id !== userId));
        }
    };

    const filteredUsers = users.filter(user =>
        user.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.email.toLowerCase().includes(searchTerm.toLowerCase())
    );

    if (loading) {
        return <div className="loading"><div className="spinner"></div></div>;
    }

    return (
        <div className="table-container">
            <div className="table-header">
                <h2>All Users ({users.length})</h2>
                <input
                    type="text"
                    placeholder="Search users..."
                    className="search-box"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
            </div>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th>Status</th>
                        <th>Created At</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {filteredUsers.map(user => (
                        <tr key={user.id}>
                            <td>{user.id}</td>
                            <td>{user.name}</td>
                            <td>{user.email}</td>
                            <td><span className="badge info">{user.role}</span></td>
                            <td>
                                <span className={`badge ${user.status === 'ACTIVE' ? 'success' : 'warning'}`}>
                                    {user.status}
                                </span>
                            </td>
                            <td>{user.createdAt}</td>
                            <td>
                                <button
                                    onClick={() => handleStatusToggle(user.id)}
                                    className="btn-action btn-edit"
                                >
                                    Toggle Status
                                </button>
                                <button
                                    onClick={() => handleDelete(user.id)}
                                    className="btn-action btn-delete"
                                >
                                    Delete
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default UserManagement;
