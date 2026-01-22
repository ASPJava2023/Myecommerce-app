import { useState } from 'react';

const Reports = () => {
    const [reportType, setReportType] = useState('sales');
    const [dateRange, setDateRange] = useState({ start: '', end: '' });

    const handleGenerateReport = () => {
        alert(`Generating ${reportType} report from ${dateRange.start} to ${dateRange.end}`);
        // In real implementation, this would call API and download CSV/Excel
    };

    return (
        <div>
            <div className="table-container">
                <div className="table-header">
                    <h2>Generate Reports</h2>
                </div>
                <div style={{ padding: '2rem' }}>
                    <div className="form-group">
                        <label>Report Type</label>
                        <select
                            value={reportType}
                            onChange={(e) => setReportType(e.target.value)}
                            style={{ width: '100%', padding: '0.75rem', borderRadius: '6px', border: '1px solid #D1D5DB' }}
                        >
                            <option value="sales">Sales Report</option>
                            <option value="users">User Report</option>
                            <option value="products">Product Report</option>
                            <option value="orders">Order Report</option>
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Start Date</label>
                        <input
                            type="date"
                            value={dateRange.start}
                            onChange={(e) => setDateRange({ ...dateRange, start: e.target.value })}
                        />
                    </div>

                    <div className="form-group">
                        <label>End Date</label>
                        <input
                            type="date"
                            value={dateRange.end}
                            onChange={(e) => setDateRange({ ...dateRange, end: e.target.value })}
                        />
                    </div>

                    <button onClick={handleGenerateReport} className="btn-primary">
                        Generate Report
                    </button>
                </div>
            </div>

            <div className="table-container" style={{ marginTop: '2rem' }}>
                <div className="table-header">
                    <h2>Recent Reports</h2>
                </div>
                <table>
                    <thead>
                        <tr>
                            <th>Report Name</th>
                            <th>Type</th>
                            <th>Generated Date</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>Sales Report - January 2024</td>
                            <td><span className="badge info">Sales</span></td>
                            <td>2024-02-01</td>
                            <td>
                                <button className="btn-action btn-approve">Download</button>
                            </td>
                        </tr>
                        <tr>
                            <td>User Report - Q4 2023</td>
                            <td><span className="badge info">Users</span></td>
                            <td>2024-01-15</td>
                            <td>
                                <button className="btn-action btn-approve">Download</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default Reports;
