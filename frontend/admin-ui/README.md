# Admin UI - E-Commerce Platform

Modern admin dashboard for managing the e-commerce platform.

## Features

- 📊 **Dashboard** - Analytics and statistics overview
- 👥 **User Management** - Manage platform users
- 🏪 **Seller Management** - Approve/reject seller applications
- 📦 **Product Approval** - Review and approve products
- 🏷️ **Category Management** - Manage product categories
- 🛒 **Order Management** - Track and update orders
- 📈 **Reports** - Generate and download reports
- 📉 **Analytics** - Advanced analytics with charts
- ⚙️ **Settings** - Platform configuration

## Tech Stack

- React 18
- Vite
- React Router DOM
- Axios
- Recharts

## Getting Started

### Install Dependencies

```bash
npm install
```

### Run Development Server

```bash
npm run dev
```

The admin UI will be available at http://localhost:3001

### Build for Production

```bash
npm run build
```

## Default Admin Credentials

- Email: admin@example.com
- Password: admin123

## API Configuration

The application connects to the backend API Gateway at `http://localhost:8080/api`

Configure the API URL in `.env`:

```
VITE_API_BASE_URL=http://localhost:8080/api
```

## Project Structure

```
src/
├── components/       # Reusable components
│   ├── Header.jsx
│   ├── Sidebar.jsx
│   └── Layout.jsx
├── pages/           # Page components
│   ├── Login.jsx
│   ├── Dashboard.jsx
│   ├── UserManagement.jsx
│   ├── SellerManagement.jsx
│   ├── ProductApproval.jsx
│   ├── CategoryManagement.jsx
│   ├── OrderManagement.jsx
│   ├── Reports.jsx
│   ├── Analytics.jsx
│   └── Settings.jsx
├── services/        # API services
│   └── authService.js
├── utils/           # Utility functions
│   └── ProtectedRoute.jsx
├── App.jsx          # Main app component
├── App.css          # Global styles
└── main.jsx         # Entry point
```

## License

MIT
