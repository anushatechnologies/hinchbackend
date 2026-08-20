# 🚀 HinchMart B2B Marketplace — Frontend Step-by-Step API Flow & Contract Documentation

> **Base URL:** `http://localhost:8081`  
> **Headers Required for Authenticated Routes:**  
> `Authorization: Bearer <accessToken>`  
> `Content-Type: application/json`

---

# 👥 USER ROLES & IDENTIFICATION GUIDE FOR FRONTEND

Frontend applications must check the `user.role` property returned in the **Login Response** (`POST /api/auth/login`), **OTP Verification** (`POST /api/auth/verify-otp`), or **Profile API** (`GET /api/auth/me`).

### 1. The 3 User Types & Permissions:

| Role String | Description & Access Level | Default Landing Page | Accessible Endpoints |
| :--- | :--- | :--- | :--- |
| **`BUYER`** | **End Customer / Business Procurement**<br>Can browse catalog, add to cart, place orders, make Razorpay payments, post custom RFQs, track deliveries, download GST invoices. | `/catalog` or `/dashboard/buyer` | `/api/cart/**`<br>`/api/orders/**`<br>`/api/payments/create`<br>`/api/payments/verify`<br>`/api/rfq/**`<br>`/api/buyer/**` |
| **`SELLER`** | **Manufacturer / Dealer / Distributor**<br>Can manage storefront, submit products for approval, view received orders, book courier shipments, submit quotes/bids on buyer RFQs. | `/seller/dashboard` | `/api/seller/**`<br>`/api/orders/seller`<br>`/api/seller/orders/**/shipment`<br>`/api/seller/rfq/**` |
| **`ADMIN`** / **`SUPER_ADMIN`** | **Platform Marketplace Operator**<br>Full supervisory control: View real-time revenue & metrics, approve/reject seller KYC, approve/reject product catalog entries, issue Razorpay refunds. | `/admin/dashboard` | `/api/admin/**`<br>`/api/payments/**/refund`<br>`/api/admin/sellers/**`<br>`/api/admin/products/**` |

---

### 2. How Frontend Identifies Roles:

#### A. From API Response:
```typescript
// Inspect: response.data.user.role
if (user.role === 'ADMIN' || user.role === 'SUPER_ADMIN') {
  router.push('/admin/dashboard');
} else if (user.role === 'SELLER') {
  router.push('/seller/dashboard');
} else {
  router.push('/catalog');
}
```

#### B. From JWT Token Payload (Decoded Client-Side):
```json
{
  "sub": "buyer@demo.com",
  "userId": 4,
  "role": "BUYER",
  "email": "buyer@demo.com",
  "phone": "9876543210",
  "status": "ACTIVE",
  "iat": 1787200000,
  "exp": 1787286400
}
```

---

# 📌 FLOW 1: Authentication & Real-Time OTP Verification

---

### Step 1 — `POST /api/auth/send-otp`
**UI Action:** User types email or 10-digit mobile number and taps *"Send OTP"*.

```json
// Request Body
{
  "identifier": "buyer@demo.com",
  "purpose": "LOGIN"
}
```

```json
// ✅ Response 200 OK — Real OTP dispatched to Email / SMS
{
  "success": true,
  "message": "OTP sent successfully to buyer@demo.com",
  "data": "OTP: 839201",
  "timestamp": "2026-08-20T16:50:00Z"
}
```

```json
// ❌ Response 400 Bad Request — Missing / Invalid Phone or Email
{
  "success": false,
  "message": "Identifier is required",
  "timestamp": "2026-08-20T16:50:00Z"
}
```

---

### Step 2 — `POST /api/auth/verify-otp`
**UI Action:** User enters the 6-digit OTP received in email / SMS and taps *"Verify & Login"*.

```json
// Request Body
{
  "identifier": "buyer@demo.com",
  "otpCode": "839201",
  "purpose": "LOGIN"
}
```

```json
// ✅ Response 200 OK — Authenticated successfully
{
  "success": true,
  "message": "OTP verified successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "7f8b9a10-2345-4cde-8f90-123456789abc",
    "tokenType": "Bearer",
    "expiresIn": 86400000,
    "user": {
      "id": 4,
      "fullName": "Rajesh Sharma",
      "email": "buyer@demo.com",
      "phone": "9876543210",
      "role": "BUYER",
      "status": "ACTIVE",
      "companyName": "Apex Infra Projects Pvt Ltd",
      "gstin": "27AAAAA0000A1Z5"
    }
  },
  "timestamp": "2026-08-20T16:50:05Z"
}
```

```json
// ❌ Response 400 Bad Request — Wrong or Expired OTP
{
  "success": false,
  "message": "Invalid or expired OTP code.",
  "timestamp": "2026-08-20T16:50:05Z"
}
```

---

### Step 3 — `POST /api/auth/login` (Standard Password Login)
**UI Action:** User enters Email/Phone and Password.

```json
// Request Body
{
  "identifier": "buyer@demo.com",
  "password": "Buyer@123"
}
```

```json
// ✅ Response 200 OK
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "7f8b9a10-2345-4cde-8f90-123456789abc",
    "user": {
      "id": 4,
      "fullName": "Rajesh Sharma",
      "role": "BUYER"
    }
  }
}
```

---

### Step 4 — `POST /api/auth/refresh-token` (Silent Token Interceptor)
**UI Action:** Automatically triggered by Axios response interceptor when access token expires (`401 Unauthorized`).

```json
// Request Body
{
  "refreshToken": "7f8b9a10-2345-4cde-8f90-123456789abc"
}
```

```json
// ✅ Response 200 OK — New JWT Access Token issued
{
  "success": true,
  "message": "Token refreshed successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.new_token_payload...",
    "refreshToken": "7f8b9a10-2345-4cde-8f90-123456789abc",
    "tokenType": "Bearer"
  }
}
```

---

# 📌 FLOW 2: Product Catalog & B2B Bulk Pricing Engine

---

### Step 1 — `GET /api/products` (Search & Filtering)
**UI Action:** User opens catalog, filters by Category, Brand, Price Range, or types search query.

```http
GET /api/products?query=TMT&categoryId=1&minPrice=50000&maxPrice=70000&page=0&size=10&sort=sellingPrice,asc
```

```json
// ✅ Response 200 OK
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 1,
        "productName": "TATA Tiscon 550D TMT Bar (12mm)",
        "slug": "tata-tiscon-550d-tmt-bar-12mm",
        "categoryName": "Steel & Structural Materials",
        "brandName": "Tata Tiscon",
        "sellingPrice": 61500.00,
        "mrp": 65000.00,
        "unit": "Ton",
        "moq": 1,
        "stock": 500,
        "gstRate": 18.00,
        "imageUrl": "https://cdn.hinchmart.com/products/tmt12.jpg"
      }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "size": 10,
    "number": 0
  }
}
```

---

### Step 2 — `GET /api/products/:id` (Product Detail & Bulk Tier Discount Table)
**UI Action:** User opens product page. Render the `bulkPrices` array as a tiered discount pricing table.

```http
GET /api/products/1
```

```json
// ✅ Response 200 OK
{
  "success": true,
  "data": {
    "id": 1,
    "productName": "TATA Tiscon 550D TMT Bar (12mm)",
    "description": "High-strength Fe-550D rebar for seismic-resistant construction.",
    "hsnCode": "72142090",
    "moq": 1,
    "stock": 500,
    "unit": "Ton",
    "sellingPrice": 61500.00,
    "gstRate": 18.00,
    "bulkPrices": [
      { "id": 1, "minQuantity": 1,  "maxQuantity": 4,  "pricePerUnit": 61500.00, "label": "1 - 4 Tons (Base)" },
      { "id": 2, "minQuantity": 5,  "maxQuantity": 9,  "pricePerUnit": 60800.00, "label": "5 - 9 Tons (Save ₹700/Ton)" },
      { "id": 3, "minQuantity": 10, "maxQuantity": 50, "pricePerUnit": 59900.00, "label": "10+ Tons (Save ₹1,600/Ton)" }
    ],
    "seller": {
      "id": 5,
      "sellerName": "Anand Verma",
      "companyName": "Tata Steel Distribution Hub Pvt Ltd",
      "city": "Pune",
      "state": "Maharashtra"
    }
  }
}
```

---

# 📌 FLOW 3: Shopping Cart Management

---

### Step 1 — `POST /api/cart/items` (Add to Cart with Auto Bulk Pricing)
**UI Action:** User selects quantity (e.g. 5 Tons) and taps *"Add to Cart"*.

```json
// Request Body
{
  "productId": 1,
  "quantity": 5
}
```

```json
// ✅ Response 201 Created — Applied Bulk Tier 5-9 Tons: ₹60,800/Ton
{
  "success": true,
  "message": "Item added to cart",
  "data": {
    "id": 201,
    "buyerId": 4,
    "items": [
      {
        "id": 301,
        "productId": 1,
        "productName": "TATA Tiscon 550D TMT Bar (12mm)",
        "quantity": 5,
        "unitPrice": 60800.00,
        "gstRate": 18.00,
        "subtotal": 304000.00
      }
    ],
    "totalItems": 1,
    "subtotal": 304000.00,
    "gstTotal": 54720.00,
    "grandTotal": 358720.00
  }
}
```

```json
// ❌ Response 400 Bad Request — Quantity below MOQ or Exceeds Stock
{
  "success": false,
  "message": "Minimum Order Quantity for TATA Tiscon is 1",
  "timestamp": "2026-08-20T16:50:10Z"
}
```

---

### Step 2 — `GET /api/cart` (View Shopping Cart)
**UI Action:** User clicks on cart icon.

```http
GET /api/cart
```

```json
// ✅ Response 200 OK
{
  "success": true,
  "data": {
    "id": 201,
    "buyerId": 4,
    "items": [
      {
        "id": 301,
        "productId": 1,
        "productName": "TATA Tiscon 550D TMT Bar (12mm)",
        "quantity": 5,
        "unitPrice": 60800.00,
        "gstRate": 18.00,
        "subtotal": 304000.00
      }
    ],
    "subtotal": 304000.00,
    "gstTotal": 54720.00,
    "grandTotal": 358720.00
  }
}
```

---

# 📌 FLOW 4: Checkout & Order Placement

---

### Step 1 — `POST /api/checkout/preview` (Financial Breakdown Preview)
**UI Action:** User enters checkout screen. App requests delivery charge and GST tax breakdown.

```json
// Request Body
{}
```

```json
// ✅ Response 200 OK
{
  "success": true,
  "data": {
    "itemCount": 1,
    "subtotal": 304000.00,
    "gstAmount": 54720.00,
    "deliveryCharge": 2500.00,
    "totalAmount": 361220.00,
    "isIntraState": true,
    "cgst": 27360.00,
    "sgst": 27360.00,
    "igst": 0.00
  }
}
```

---

### Step 2 — `POST /api/orders` (Place Order)
**UI Action:** User enters shipping address, selects payment method (e.g. `UPI`), and clicks *"Place Order"*.

```json
// Request Body
{
  "shippingAddress": "Site #7, Metro Line 3 Corridor, Hinjewadi Phase 2, Pune",
  "billingAddress": "Plot 45, MIDC Industrial Area, Phase 2, Pune",
  "city": "Pune",
  "state": "Maharashtra",
  "pincode": "411057",
  "paymentMethod": "UPI",
  "notes": "Gate #3 entry. Unloading crane available."
}
```

```json
// ✅ Response 201 Created — Order created in DB with status PLACED
{
  "success": true,
  "message": "Order placed successfully",
  "data": {
    "id": 115,
    "orderNumber": "ORD-2026-0820-015",
    "buyerId": 4,
    "subtotal": 304000.00,
    "gstAmount": 54720.00,
    "deliveryCharge": 2500.00,
    "totalAmount": 361220.00,
    "paymentMethod": "UPI",
    "paymentStatus": "PENDING",
    "orderStatus": "PLACED",
    "createdAt": "2026-08-20T16:50:30Z"
  }
}
```

---

# 📌 FLOW 5: Razorpay Payment Gateway & Verification

---

### Step 1 — `GET /api/payments/config` (Fetch Public Key)
**UI Action:** Called on app init to load Razorpay Public Key ID.

```http
GET /api/payments/config
```

```json
// ✅ Response 200 OK
{
  "success": true,
  "data": {
    "keyId": "rzp_live_TO6q7NUVnPM6bA",
    "currency": "INR",
    "companyName": "HinchMart"
  }
}
```

---

### Step 2 — `POST /api/payments/create` (Initiate Razorpay Gateway Order)
**UI Action:** User clicks *"Proceed to Pay"*.

```json
// Request Body
{
  "orderId": 115,
  "paymentMethod": "UPI"
}
```

```json
// ✅ Response 201 Created — Pass gatewayOrderId to Razorpay Checkout JS
{
  "success": true,
  "message": "Razorpay payment order initiated successfully",
  "data": {
    "id": 415,
    "paymentNumber": "PAY-20260820-015",
    "orderId": 115,
    "orderNumber": "ORD-2026-0820-015",
    "amount": 361220.00,
    "currency": "INR",
    "amountInPaise": 36122000,
    "paymentMethod": "UPI",
    "paymentStatus": "PENDING",
    "gatewayOrderId": "order_Rzp_20260820_015",
    "razorpayKeyId": "rzp_live_TO6q7NUVnPM6bA",
    "buyerEmail": "buyer@demo.com",
    "buyerPhone": "9876543210",
    "companyName": "HinchMart"
  }
}
```

---

### Step 3 — `POST /api/payments/verify` (Cryptographic HMAC Verification & Confirm Order)
**UI Action:** Razorpay Checkout popup finishes and triggers `handler(response)` with `razorpay_payment_id` and `razorpay_signature`.

```json
// Request Body
{
  "paymentId": 415,
  "gatewayOrderId": "order_Rzp_20260820_015",
  "gatewayPaymentId": "pay_O5h72g89sA22",
  "gatewaySignature": "9f82ab7c31d8e..."
}
```

```json
// ✅ Response 200 OK — Order transitioned to PAID & CONFIRMED. GST Invoice generated!
{
  "success": true,
  "message": "Payment signature verified successfully",
  "data": {
    "id": 415,
    "paymentNumber": "PAY-20260820-015",
    "orderId": 115,
    "amount": 361220.00,
    "paymentMethod": "UPI",
    "paymentStatus": "SUCCESS",
    "gatewayPaymentId": "pay_O5h72g89sA22",
    "transactions": [
      {
        "id": 501,
        "transactionType": "PAYMENT",
        "amount": 361220.00,
        "status": "SUCCESS",
        "gatewayReference": "pay_O5h72g89sA22",
        "createdAt": "2026-08-20T16:51:00Z"
      }
    ]
  }
}
```

```json
// ❌ Response 400 Bad Request — Signature Mismatch / Tampered Request
{
  "success": false,
  "message": "Payment verification failed: Invalid Razorpay gateway signature.",
  "timestamp": "2026-08-20T16:51:00Z"
}
```

---

# 📌 FLOW 6: Order Fulfillment, Dispatch & Live Courier Tracking

---

### Step 1 — `POST /api/seller/orders/:id/shipment` (Seller Books Courier Shipment)
**UI Action:** Seller opens order in Seller Portal, inputs AWB/Tracking code, and taps *"Dispatch Order"*.

```json
// Request Body
{
  "deliveryPartnerId": 2,
  "trackingNumber": "VRL-2026-998811",
  "awbCode": "AWB-VRL-998811",
  "shippingLabelUrl": "https://cdn.hinchmart.com/labels/vrl_998811.pdf",
  "estimatedDeliveryDate": "2026-08-25",
  "notes": "Loaded on Flatbed Trailer #MH-12-AB-9988."
}
```

```json
// ✅ Response 201 Created — Order status automatically moves to READY_TO_SHIP
{
  "success": true,
  "message": "Shipment booked successfully",
  "data": {
    "id": 701,
    "orderId": 115,
    "deliveryPartnerName": "VRL Logistics Heavy Freight",
    "trackingNumber": "VRL-2026-998811",
    "awbCode": "AWB-VRL-998811",
    "status": "MANIFESTED",
    "estimatedDeliveryDate": "2026-08-25",
    "shippingLabelUrl": "https://cdn.hinchmart.com/labels/vrl_998811.pdf"
  }
}
```

---

### Step 2 — `PATCH /api/seller/shipments/:id/status` (Update Courier Milestone)
**UI Action:** Carrier / Seller updates shipment progress.

```json
// Request Body
{
  "status": "IN_TRANSIT",
  "location": "Pune Highway Toll Plaza Checkpoint",
  "notes": "En route to Pune Site delivery"
}
```

```json
// ✅ Response 200 OK — Order status auto-syncs to SHIPPED
{
  "success": true,
  "message": "Shipment status updated to IN_TRANSIT",
  "data": {
    "id": 701,
    "orderId": 115,
    "status": "IN_TRANSIT",
    "orderStatus": "SHIPPED"
  }
}
```

---

### Step 3 — `GET /api/orders/:id/tracking` (Live Courier Tracking Timeline)
**UI Action:** Customer App polls tracking status or renders live timeline.

```http
GET /api/orders/115/tracking
```

```json
// ✅ Response 200 OK
{
  "success": true,
  "data": {
    "orderId": 115,
    "orderNumber": "ORD-2026-0820-015",
    "carrierName": "VRL Logistics Heavy Freight",
    "trackingNumber": "VRL-2026-998811",
    "currentStatus": "IN_TRANSIT",
    "estimatedDelivery": "2026-08-25",
    "checkpoints": [
      {
        "status": "MANIFESTED",
        "location": "TATA Steel Distribution Hub, Pune Yard",
        "timestamp": "2026-08-20T17:00:00Z",
        "description": "Consignment booked and AWB generated."
      },
      {
        "status": "PICKED_UP",
        "location": "Gate 2 Outward Bay, MIDC Pune",
        "timestamp": "2026-08-20T18:30:00Z",
        "description": "Loaded onto Flatbed Commercial Trailer #MH-12-AB-9988."
      },
      {
        "status": "IN_TRANSIT",
        "location": "Pune Highway Toll Plaza Checkpoint",
        "timestamp": "2026-08-20T20:15:00Z",
        "description": "Vehicle in transit towards Hinjewadi Phase 2 site."
      }
    ]
  }
}
```

---

### Step 4 — `GET /api/orders/:id/invoice` (Auto-Generated GST Tax Invoice)
**UI Action:** Buyer or Seller clicks *"Download Tax Invoice"*.

```http
GET /api/orders/115/invoice
```

```json
// ✅ Response 200 OK
{
  "success": true,
  "data": {
    "id": 801,
    "invoiceNumber": "INV-2026-000115",
    "orderId": 115,
    "orderNumber": "ORD-2026-0820-015",
    "sellerName": "Anand Verma",
    "sellerCompanyName": "Tata Steel Distribution Hub Pvt Ltd",
    "sellerGstin": "27AAACT2727Q1ZW",
    "buyerName": "Rajesh Sharma",
    "buyerCompanyName": "Apex Infra Projects Pvt Ltd",
    "buyerGstin": "27AAAAA0000A1Z5",
    "isIntraState": true,
    "taxableValue": 304000.00,
    "cgstAmount": 27360.00,
    "sgstAmount": 27360.00,
    "igstAmount": 0.00,
    "totalGst": 54720.00,
    "deliveryCharge": 2500.00,
    "grandTotal": 361220.00,
    "paymentStatus": "PAID",
    "items": [
      {
        "productName": "TATA Tiscon 550D TMT Bar (12mm)",
        "hsnCode": "72142090",
        "quantity": 5,
        "unit": "Ton",
        "unitPrice": 60800.00,
        "taxableValue": 304000.00,
        "gstPercentage": 18.00,
        "gstAmount": 54720.00,
        "totalAmount": 358720.00
      }
    ]
  }
}
```

---

# 📌 FLOW 7: B2B RFQ (Request For Quotation) & Bidding Flow

---

### Step 1 — `POST /api/rfq` (Buyer Posts Custom Bulk RFQ)
**UI Action:** Buyer needs 50 Tons of structural steel and posts a custom quotation request.

```json
// Request Body
{
  "title": "50 Tons Fe-550D TMT Rebar for Commercial Mall Project",
  "categoryId": 1,
  "quantity": 50,
  "unit": "Ton",
  "targetPrice": 59000.00,
  "deliveryLocation": "Hinjewadi Phase 2, Pune",
  "requiredByDate": "2026-09-10",
  "specifications": "Fe-550D grade with test certificates from BIS/NABL lab."
}
```

```json
// ✅ Response 201 Created
{
  "success": true,
  "message": "RFQ created successfully",
  "data": {
    "id": 601,
    "rfqNumber": "RFQ-2026-0820-001",
    "status": "OPEN",
    "buyerId": 4,
    "totalQuotesReceived": 0,
    "createdAt": "2026-08-20T17:15:00Z"
  }
}
```

---

### Step 2 — `POST /api/seller/rfq/:rfqId/quotes` (Seller Submits Bid / Quote)
**UI Action:** Seller views open RFQs, enters unit price & lead time, and clicks *"Submit Bid"*.

```json
// Request Body
{
  "pricePerUnit": 58500.00,
  "leadTimeDays": 5,
  "validUntil": "2026-08-30",
  "comments": "Can deliver in 2 trailer consignments with Mill Test Certificate included."
}
```

```json
// ✅ Response 201 Created
{
  "success": true,
  "message": "Quotation submitted successfully",
  "data": {
    "id": 901,
    "rfqId": 601,
    "sellerId": 5,
    "sellerCompanyName": "Tata Steel Distribution Hub Pvt Ltd",
    "pricePerUnit": 58500.00,
    "totalPrice": 2925000.00,
    "status": "SUBMITTED"
  }
}
```

---

### Step 3 — `POST /api/buyer/rfq/quotes/:id/accept` (Buyer Accepts Winning Quote)
**UI Action:** Buyer compares bids from different sellers and accepts the best quote.

```json
// Request Body
{}
```

```json
// ✅ Response 200 OK — Winning quote converted into confirmed order
{
  "success": true,
  "message": "Quotation accepted successfully. Order initialized.",
  "data": {
    "quoteId": 901,
    "rfqId": 601,
    "status": "ACCEPTED",
    "convertedOrderId": 116
  }
}
```

---

# 📌 FLOW 8: In-App Notifications & Badges

---

### Step 1 — `GET /api/notifications/unread` (Badge Counter)
**UI Action:** Polled by header navigation bar to show red unread badge dot.

```http
GET /api/notifications/unread
```

```json
// ✅ Response 200 OK
{
  "success": true,
  "data": [
    {
      "id": 1001,
      "title": "Payment Successful!",
      "message": "Your payment of ₹361,220.00 for order ORD-2026-0820-015 was processed successfully.",
      "type": "PAYMENT_SUCCESS",
      "referenceId": 115,
      "referenceType": "ORDER",
      "read": false,
      "createdAt": "2026-08-20T16:51:00Z"
    }
  ]
}
```

---

### Step 2 — `PATCH /api/notifications/read-all` (Clear All Badges)
**UI Action:** User clicks *"Mark All as Read"*.

```json
// ✅ Response 200 OK
{
  "success": true,
  "message": "All notifications marked as read",
  "data": null
}
```

---

# 📌 FLOW 9: Admin Dashboard & Marketplace Moderation (ADMIN Only)

---

### Step 1 — `GET /api/admin/dashboard` (Real-Time Aggregated Statistics)
**UI Action:** Admin loads the Admin Control Center home dashboard.

```http
GET /api/admin/dashboard
```

```json
// ✅ Response 200 OK
{
  "success": true,
  "data": {
    "totalBuyers": 142,
    "totalSellers": 28,
    "pendingSellers": 3,
    "activeProducts": 89,
    "pendingProducts": 5,
    "openRfqs": 12,
    "todayOrders": 8,
    "totalRevenue": 4820000.00
  }
}
```

---

### Step 2 — `GET /api/admin/sellers` & `PATCH /api/admin/sellers/:id/approve` (Seller KYC Verification)
**UI Action:** Admin inspects seller's uploaded GSTIN, PAN, and business documents, then clicks *"Approve Seller"*.

```http
GET /api/admin/sellers?status=PENDING
```

```json
// Request Body for Approval: PATCH /api/admin/sellers/5/approve
{}
```

```json
// ✅ Response 200 OK — Seller status updated to APPROVED. Seller can now list products & bid on RFQs.
{
  "success": true,
  "message": "Seller approved successfully",
  "data": {
    "id": 5,
    "companyName": "Tata Steel Distribution Hub Pvt Ltd",
    "status": "APPROVED",
    "verifiedAt": "2026-08-20T17:30:00Z"
  }
}
```

---

### Step 3 — `GET /api/admin/products` & `PATCH /api/admin/products/:id/approve` (Product Catalog Moderation)
**UI Action:** Admin checks submitted product specifications and bulk pricing tiers, then clicks *"Approve Product"*.

```json
// Request Body: PATCH /api/admin/products/1/approve
{}
```

```json
// ✅ Response 200 OK — Product becomes visible in public search and category listings
{
  "success": true,
  "message": "Product approved successfully",
  "data": {
    "id": 1,
    "productName": "TATA Tiscon 550D TMT Bar (12mm)",
    "approvalStatus": "APPROVED",
    "isActive": true
  }
}
```

---

### Step 4 — `POST /api/payments/:id/refund` (Admin Trigger Razorpay Payment Refund)
**UI Action:** Admin approves cancellation/refund request and triggers instant refund via Razorpay API.

```json
// Request Body
{
  "amount": 361220.00,
  "reason": "Customer cancelled order before trailer dispatch."
}
```

```json
// ✅ Response 200 OK — Refund processed with Razorpay Gateway
{
  "success": true,
  "message": "Refund processed successfully via Razorpay",
  "data": {
    "id": 601,
    "refundNumber": "REF-2026-0820-001",
    "paymentId": 415,
    "orderId": 115,
    "amount": 361220.00,
    "refundStatus": "PROCESSED",
    "gatewayRefundId": "rfnd_O5h82x9A1b2c"
  }
}
```

