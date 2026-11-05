require('dotenv').config();
const express = require('express');
const cors = require('cors');

const app = express();
const PORT = process.env.PORT || 3000;
const FAKE_STORE_URL = process.env.FAKE_STORE_API_URL || 'https://fakestoreapi.com';

app.use(cors());
app.use(express.json());

// Comprehensive Event & Request Logging Middleware
app.use((req, res, next) => {
  const timestamp = new Date().toISOString();
  const method = req.method;
  const url = req.originalUrl;
  const ip = req.ip || req.connection.remoteAddress;

  console.log(`\n📥 [${timestamp}] Incoming Event: ${method} ${url} from IP: ${ip}`);
  if (Object.keys(req.query).length > 0) {
    console.log(`   Query Params:`, JSON.stringify(req.query));
  }
  if (req.body && Object.keys(req.body).length > 0) {
    console.log(`   Request Body:`, JSON.stringify(req.body));
  }

  const start = Date.now();
  res.on('finish', () => {
    const duration = Date.now() - start;
    const status = res.statusCode;
    const emoji = status >= 400 ? '❌' : '✅';
    console.log(`${emoji} [${new Date().toISOString()}] Response: ${method} ${url} -> Status ${status} (${duration}ms)`);
  });

  next();
});

// Helper to fetch from Fake Store API
async function fetchFakeStore(endpoint) {
  const fullUrl = `${FAKE_STORE_URL}${endpoint}`;
  console.log(`🌐 [External API Call] Fetching from Fake Store: ${fullUrl}`);
  const response = await fetch(fullUrl);
  if (!response.ok) {
    throw new Error(`Failed to fetch from Fake Store API: ${response.statusText}`);
  }
  const data = await response.json();
  console.log(`📦 [External API Response] Received data from ${endpoint} (items/data count: ${Array.isArray(data) ? data.length : 'object'})`);
  return data;
}

// GET /api/products/featured - Top 5 rated products
app.get('/api/products/featured', async (req, res) => {
  try {
    const products = await fetchFakeStore('/products');
    const featured = products
      .sort((a, b) => (b.rating?.rate || 0) - (a.rating?.rate || 0))
      .slice(0, 5);
    console.log(`⭐ [Featured Products] Returning top ${featured.length} rated products.`);
    res.json(featured);
  } catch (error) {
    console.error('❌ Error fetching featured products:', error);
    res.status(500).json({ error: 'Internal Server Error' });
  }
});

// GET /api/products/categories - All categories
app.get('/api/products/categories', async (req, res) => {
  try {
    const categories = await fetchFakeStore('/products/categories');
    console.log(`🏷️ [Categories] Returning ${categories.length} categories.`);
    res.json(categories);
  } catch (error) {
    console.error('❌ Error fetching categories:', error);
    res.status(500).json({ error: 'Internal Server Error' });
  }
});

// GET /api/products - Get all products, optional query ?category=... or ?search=...
app.get('/api/products', async (req, res) => {
  try {
    const { category, search } = req.query;
    let endpoint = '/products';
    if (category) {
      endpoint = `/products/category/${encodeURIComponent(category)}`;
    }
    let products = await fetchFakeStore(endpoint);

    if (search) {
      const query = search.toLowerCase();
      products = products.filter(p =>
        p.title.toLowerCase().includes(query) ||
        p.description.toLowerCase().includes(query) ||
        p.category.toLowerCase().includes(query)
      );
      console.log(`🔍 [Search Products] Query "${search}" matched ${products.length} products.`);
    } else {
      console.log(`📋 [Products] Returning ${products.length} products (category: ${category || 'all'}).`);
    }

    res.json(products);
  } catch (error) {
    console.error('❌ Error fetching products:', error);
    res.status(500).json({ error: 'Internal Server Error' });
  }
});

// GET /api/products/:id - Product details
app.get('/api/products/:id', async (req, res) => {
  try {
    const { id } = req.params;
    console.log(`📄 [Product Details] Fetching details for product ID: ${id}`);
    const product = await fetchFakeStore(`/products/${id}`);
    if (!product || Object.keys(product).length === 0) {
      console.log(`⚠️ [Product Details] Product ID ${id} not found.`);
      return res.status(404).json({ error: 'Product not found' });
    }
    res.json(product);
  } catch (error) {
    console.error(`❌ Error fetching product ${req.params.id}:`, error);
    res.status(404).json({ error: 'Product not found' });
  }
});

// POST /api/enquiries - Submit product enquiry
app.post('/api/enquiries', (req, res) => {
  try {
    const { productId, productTitle, customerName, customerEmail, message } = req.body;

    if (productId === undefined || productId === null || !customerName || !customerEmail) {
      console.log(`⚠️ [Enquiry Submission] Validation failed: Missing required fields.`);
      return res.status(400).json({ error: 'Missing required enquiry fields' });
    }

    console.log('-------------------------------------------');
    console.log('📧 [SALES INBOX EMAIL SIMULATION]');
    console.log(`To: ${process.env.SALES_EMAIL || 'sales@example.com'}`);
    console.log(`Subject: New Enquiry for Product: ${productTitle || productId}`);
    console.log(`From: ${customerName} (${customerEmail})`);
    console.log(`Product ID: ${productId}`);
    console.log(`Message:\n${message || 'No additional message provided.'}`);
    console.log('-------------------------------------------');

    res.status(201).json({
      success: true,
      message: 'Enquiry submitted successfully. Sales inbox notified.',
      enquiryId: Date.now()
    });
  } catch (error) {
    console.error('❌ Error submitting enquiry:', error);
    res.status(500).json({ error: 'Internal Server Error' });
  }
});

app.listen(PORT, () => {
  console.log(`🚀 Product Catalogue Backend running on port ${PORT} with full event logging enabled.`);
});
