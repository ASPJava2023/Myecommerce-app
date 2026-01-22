# ShopSphere - E-Commerce Frontend

A modern, responsive React frontend for the ShopSphere e-commerce platform with premium UI/UX design, Razorpay payment integration, and seamless API integration.

## 🎨 Features

### Core Features
- **Authentication**: Login and registration with JWT token management
- **Product Catalog**: Browse products with search, filtering, and pagination
- **Shopping Cart**: Add/remove items, update quantities, real-time cart updates
- **Checkout**: Secure checkout with Razorpay payment integration
- **Order Management**: View order history and track order status
- **Responsive Design**: Mobile-first design with glassmorphism effects

### Design Highlights
- 🌙 **Dark Mode First**: Premium dark theme with vibrant gradients
- ✨ **Glassmorphism**: Modern glass-effect cards and components
- 🎭 **Smooth Animations**: Micro-interactions and transitions
- 📱 **Fully Responsive**: Optimized for mobile, tablet, and desktop
- 🎨 **Custom Design System**: Consistent colors, spacing, and typography

## 🚀 Getting Started

### Prerequisites
- Node.js 18+ and npm
- Backend services running (User, Product, Order services)
- API Gateway running on port 8080

### Installation

1. **Install Dependencies**
```bash
cd frontend
npm install
```

2. **Configure Environment**
Update Razorpay key in `src/pages/CheckoutPage.jsx`:
```javascript
key: 'rzp_test_YOUR_KEY_ID', // Replace with your actual Razorpay key
```

3. **Start Development Server**
```bash
npm run dev
```

The application will be available at `http://localhost:3000`

### Build for Production
```bash
npm run build
```

## 📁 Project Structure

```
frontend/
├── public/
├── src/
│   ├── components/          # Reusable components
│   │   ├── Header.jsx
│   │   ├── Footer.jsx
│   │   ├── ProductCard.jsx
│   │   ├── Loading.jsx
│   │   ├── ErrorMessage.jsx
│   │   ├── Pagination.jsx
│   │   └── ProtectedRoute.jsx
│   ├── pages/              # Page components
│   │   ├── HomePage.jsx
│   │   ├── LoginPage.jsx
│   │   ├── RegisterPage.jsx
│   │   ├── ProductsPage.jsx
│   │   ├── ProductDetailPage.jsx
│   │   ├── CartPage.jsx
│   │   ├── CheckoutPage.jsx
│   │   ├── OrdersPage.jsx
│   │   └── OrderDetailPage.jsx
│   ├── services/           # API services
│   │   ├── api.js
│   │   ├── authService.js
│   │   ├── productService.js
│   │   ├── categoryService.js
│   │   ├── cartService.js
│   │   └── orderService.js
│   ├── context/            # React context
│   │   └── AuthContext.jsx
│   ├── utils/              # Utility functions
│   │   ├── formatters.js
│   │   └── validators.js
│   ├── config/             # Configuration
│   │   └── constants.js
│   ├── App.jsx             # Main app component
│   ├── main.jsx            # Entry point
│   └── index.css           # Global styles & design system
├── index.html
├── vite.config.js
└── package.json
```

## 🔌 API Integration

The frontend connects to the backend API Gateway at `http://localhost:8080` through Vite proxy configuration.

### API Endpoints Used

**Authentication**
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout
- `GET /api/auth/me` - Get current user

**Products**
- `GET /api/products` - List products (paginated)
- `GET /api/products/{id}` - Get product details
- `GET /api/products/search` - Search products
- `GET /api/products/category/{id}` - Filter by category
- `GET /api/products/featured` - Get featured products

**Categories**
- `GET /api/categories` - List all categories

**Cart**
- `GET /api/cart` - Get user's cart
- `POST /api/cart/items` - Add item to cart
- `PUT /api/cart/items/{id}` - Update cart item quantity
- `DELETE /api/cart/items/{id}` - Remove item from cart
- `DELETE /api/cart` - Clear cart

**Orders**
- `POST /api/orders` - Create order
- `GET /api/orders` - Get user's orders (paginated)
- `GET /api/orders/{id}` - Get order details
- `PUT /api/orders/{id}/cancel` - Cancel order

## 💳 Razorpay Integration

The checkout page integrates Razorpay for secure payments:

1. User fills shipping address
2. Clicks "Proceed to Payment"
3. Razorpay modal opens for payment
4. On successful payment, order is confirmed
5. User is redirected to order details page

**Setup Razorpay:**
1. Sign up at [razorpay.com](https://razorpay.com)
2. Get your API key from the dashboard
3. Update the key in `CheckoutPage.jsx`

## 🎨 Design System

### Color Palette
- **Primary**: Purple/Blue gradient (#667eea → #764ba2)
- **Secondary**: Coral/Pink (#ec4899)
- **Success**: Green (#10b981)
- **Error**: Red (#ef4444)
- **Background**: Dark theme (#0f0f23, #1a1a2e)

### Typography
- **Primary Font**: Inter
- **Display Font**: Outfit
- **Sizes**: Responsive scale from 0.75rem to 3rem

### Components
- Glassmorphism cards with backdrop blur
- Gradient buttons with glow effects
- Smooth transitions and micro-animations
- Responsive grid layouts

## 📱 Responsive Breakpoints

- **Mobile**: < 768px
- **Tablet**: 769px - 1024px
- **Desktop**: > 1024px

## 🔒 Authentication Flow

1. User registers or logs in
2. JWT token is stored in localStorage
3. Token is sent with every API request via Axios interceptor
4. Protected routes redirect to login if not authenticated
5. Token is cleared on logout

## 🛠️ Development

### Available Scripts

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run preview` - Preview production build

### Code Style
- React functional components with hooks
- ES6+ JavaScript
- CSS modules for component styles
- Consistent naming conventions

## 🚀 Deployment

### Build
```bash
npm run build
```

The build output will be in the `dist/` directory.

### Deploy Options
- **Vercel**: Connect GitHub repo for automatic deployments
- **Netlify**: Drag and drop `dist/` folder
- **AWS S3 + CloudFront**: Upload to S3 bucket
- **Docker**: Create Dockerfile with nginx to serve static files

## 📝 Environment Variables

Create `.env` file for environment-specific configuration:

```env
VITE_API_BASE_URL=http://localhost:8080
VITE_RAZORPAY_KEY=rzp_test_YOUR_KEY_ID
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

MIT License - feel free to use this project for learning and development.

## 🙏 Acknowledgments

- Design inspiration from modern e-commerce platforms
- Icons from Unicode emoji
- Fonts from Google Fonts (Inter, Outfit)
- Payment integration by Razorpay

---

**Built with ❤️ using React, Vite, and modern web technologies**
