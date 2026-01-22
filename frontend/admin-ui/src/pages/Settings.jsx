const Settings = () => {
    return (
        <div className="table-container">
            <div className="table-header">
                <h2>Platform Settings</h2>
            </div>
            <div style={{ padding: '2rem' }}>
                <h3 style={{ marginBottom: '1rem' }}>General Settings</h3>

                <div className="form-group">
                    <label>Platform Name</label>
                    <input type="text" defaultValue="E-Commerce Platform" />
                </div>

                <div className="form-group">
                    <label>Support Email</label>
                    <input type="email" defaultValue="support@ecommerce.com" />
                </div>

                <div className="form-group">
                    <label>Currency</label>
                    <select style={{ width: '100%', padding: '0.75rem', borderRadius: '6px', border: '1px solid #D1D5DB' }}>
                        <option value="USD">USD - US Dollar</option>
                        <option value="EUR">EUR - Euro</option>
                        <option value="GBP">GBP - British Pound</option>
                        <option value="INR">INR - Indian Rupee</option>
                    </select>
                </div>

                <div className="form-group">
                    <label>
                        <input type="checkbox" defaultChecked /> Enable user registration
                    </label>
                </div>

                <div className="form-group">
                    <label>
                        <input type="checkbox" defaultChecked /> Enable seller registration
                    </label>
                </div>

                <div className="form-group">
                    <label>
                        <input type="checkbox" defaultChecked /> Require product approval
                    </label>
                </div>

                <button className="btn-primary">Save Settings</button>
            </div>
        </div>
    );
};

export default Settings;
