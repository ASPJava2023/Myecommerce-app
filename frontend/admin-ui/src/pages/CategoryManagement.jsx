import { useState, useEffect } from 'react';

const CategoryManagement = () => {
    const [categories, setCategories] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [newCategory, setNewCategory] = useState({ name: '', description: '' });
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        setTimeout(() => {
            setCategories([
                { id: 1, name: 'Electronics', description: 'Electronic devices and gadgets', productCount: 150 },
                { id: 2, name: 'Fashion', description: 'Clothing and accessories', productCount: 200 },
                { id: 3, name: 'Furniture', description: 'Home and office furniture', productCount: 80 }
            ]);
            setLoading(false);
        }, 1000);
    }, []);

    const handleAddCategory = () => {
        if (newCategory.name) {
            setCategories([...categories, {
                id: categories.length + 1,
                ...newCategory,
                productCount: 0
            }]);
            setNewCategory({ name: '', description: '' });
            setShowModal(false);
        }
    };

    const handleDelete = (categoryId) => {
        if (window.confirm('Are you sure you want to delete this category?')) {
            setCategories(categories.filter(cat => cat.id !== categoryId));
        }
    };

    if (loading) {
        return <div className="loading"><div className="spinner"></div></div>;
    }

    return (
        <div>
            <div className="table-container">
                <div className="table-header">
                    <h2>Categories ({categories.length})</h2>
                    <button onClick={() => setShowModal(true)} className="btn-primary">
                        Add Category
                    </button>
                </div>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Description</th>
                            <th>Products</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {categories.map(category => (
                            <tr key={category.id}>
                                <td>{category.id}</td>
                                <td>{category.name}</td>
                                <td>{category.description}</td>
                                <td>{category.productCount}</td>
                                <td>
                                    <button className="btn-action btn-edit">Edit</button>
                                    <button
                                        onClick={() => handleDelete(category.id)}
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

            {showModal && (
                <div className="modal-overlay">
                    <div className="modal">
                        <h2>Add New Category</h2>
                        <div className="form-group">
                            <label>Category Name</label>
                            <input
                                type="text"
                                value={newCategory.name}
                                onChange={(e) => setNewCategory({ ...newCategory, name: e.target.value })}
                                placeholder="Enter category name"
                            />
                        </div>
                        <div className="form-group">
                            <label>Description</label>
                            <input
                                type="text"
                                value={newCategory.description}
                                onChange={(e) => setNewCategory({ ...newCategory, description: e.target.value })}
                                placeholder="Enter description"
                            />
                        </div>
                        <div className="modal-actions">
                            <button onClick={() => setShowModal(false)} className="btn-secondary">
                                Cancel
                            </button>
                            <button onClick={handleAddCategory} className="btn-primary">
                                Add Category
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default CategoryManagement;
