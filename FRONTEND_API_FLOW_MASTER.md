# 🚀 HinchMart B2B Marketplace — Complete API Flow Master Guide

> **Base URL:** `http://localhost:8081`  
> **Auth Header:** `Authorization: Bearer <accessToken>` *(required for all protected routes)*  
> **Content-Type:** `application/json`

---

# 👤 PART 1: BUYER (USER) APIS & FLOWS

---

## 🟢 FLOW 1.1: Buyer Authentication & Real-Time OTP Login

### Step 1 — `POST /api/auth/send-otp`
*Buyer enters Email or 10-digit Phone and taps "Send OTP"*

```json
// Request
{
  "identifier": "buyer@demo.com",
  "purpose": "LOGIN"
}

// ✅ Response 200 OK — Real OTP dispatched to Email & SMS
{
  "success": true,
  "message": "OTP sent successfully to buyer@demo.com",
  "data": "OTP: 839201"                                      ← 6-digit random code
}

// ❌ Response 400 Bad Request
{
  "success": false,
  "message": "Identifier is required"
}
```

### Step 2 — `POST /api/auth/verify-otp`
*Buyer enters the 6-digit OTP received in email/phone and taps "Verify & Login"*

```json
// Request
{
  "identifier": "buyer@demo.com",
  "otpCode": "839201",
  "purpose": "LOGIN"
}

// ✅ Response 200 OK — JWT session tokens issued
{
  "success": true,
  "message": "OTP verified successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "7f8b9a10-2345-4cde-8f90-123456789abc",
    "tokenType": "Bearer",
    "user": {
      "id": 4,
      "fullName": "Rajesh Sharma",
      "email": "buyer@demo.com",
      "phone": "9876543210",
      "role": "BUYER",                                       ← Identify as BUYER
      "companyName": "Apex Infra Projects Pvt Ltd"
    }
  }
}

// ❌ Response 400 Bad Request — Wrong or expired OTP
{
  "success": false,
  "message": "Invalid or expired OTP code."
}
```

### Step 3 — `POST /api/auth/login` (Standard Password Login Alternative)
*Buyer enters Email/Phone and Password*

```json
// Request
{
  "identifier": "buyer@demo.com",
  "password": "Buyer@123"
}

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

## 🟢 FLOW 1.2: Catalog Browsing & Bulk Tier Pricing

### Step 1 — `GET /api/products`
*Buyer searches or filters product catalog*

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
    "totalPages": 1
  }
}
```

### Step 2 — `GET /api/products/:id`
*Buyer opens Product Details page. Render the `bulkPrices` array as a tiered discount pricing table and `pincodeInventories` as serviceable delivery zones.*

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
    "sku": "TATA-TISCON-550D-12MM",
    "moq": 1,
    "stock": 25,
    "unit": "Ton",
    "sellingPrice": 61500.00,
    "gstRate": 18.00,
    "bulkPrices": [
      { "minQuantity": 1,  "maxQuantity": 4,  "pricePerUnit": 61500.00, "label": "1 - 4 Tons (Base)" },
      { "minQuantity": 5,  "maxQuantity": 9,  "pricePerUnit": 60800.00, "label": "5 - 9 Tons (Save ₹700/Ton)" },
      { "minQuantity": 10, "maxQuantity": 50, "pricePerUnit": 59900.00, "label": "10+ Tons (Save ₹1,600/Ton)" }
    ],
    "pincodeInventories": [
      {
        "id": 1,
        "pincode": "410218",
        "warehouseName": "Kalamboli Yard - Bay 4",
        "city": "Navi Mumbai",
        "state": "Maharashtra",
        "quantity": 15,
        "availableQuantity": 15,
        "deliveryDays": 2,
        "serviceable": true
      },
      {
        "id": 2,
        "pincode": "411057",
        "warehouseName": "Pune Hinjewadi Hub",
        "city": "Pune",
        "state": "Maharashtra",
        "quantity": 10,
        "availableQuantity": 10,
        "deliveryDays": 1,
        "serviceable": true
      }
    ]
  }
}
```

### Step 3 — `GET /api/inventory/check-availability`
*Buyer enters delivery pincode (e.g. 411057) to check stock availability and estimated delivery timeline. (Requires Bearer token, any role).*

```http
GET /api/inventory/check-availability?skuOrId=TATA-TISCON-550D-12MM&pincode=411057&quantity=5
Authorization: Bearer <accessToken>
```

```json
// ✅ Response 200 OK
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "productId": 1,
    "productSku": "TATA-TISCON-550D-12MM",
    "productName": "TATA Tiscon 550D TMT Bar (12mm)",
    "pincode": "411057",
    "warehouseName": "Pune Hinjewadi Hub",
    "city": "Pune",
    "state": "Maharashtra",
    "quantity": 10,
    "availableQuantity": 10,
    "reservedQuantity": 0,
    "deliveryDays": 1,
    "serviceable": true,
    "active": true
  },
  "timestamp": "2026-08-22T02:13:28.123"
}
```

### Step 4 — `GET /api/inventory/search`
*Search all inventory across warehouses by any combination of filters: pincode, category, subcategory, brand, keyword, and stock status. (Requires Bearer token, any role).*

```http
GET /api/inventory/search?pincode=411057&categoryId=1&inStockOnly=true
Authorization: Bearer <accessToken>
```

```json
// ✅ Response 200 OK
{
  "success": true,
  "message": "Operation successful",
  "data": [
    {
      "id": 2,
      "productId": 1,
      "productSku": "TATA-TISCON-550D-12MM",
      "productName": "TATA Tiscon 550D TMT Bar (12mm)",
      "sellerId": 5,
      "sellerName": "Anand Verma",
      "pincode": "411057",
      "warehouseName": "Pune Hinjewadi Hub",
      "city": "Pune",
      "state": "Maharashtra",
      "quantity": 10,
      "availableQuantity": 10,
      "deliveryDays": 1,
      "serviceable": true
    }
  ],
  "timestamp": "2026-08-22T02:13:28.123"
}
```

---

## 🟢 FLOW 1.3: Cart & Real-Time Bulk Pricing Engine

### Step 1 — `POST /api/cart/items`
*Buyer selects quantity (e.g. 5 Tons) and taps "Add to Cart"*

```json
// Request
{
  "productId": 1,
  "quantity": 5
}

// ✅ Response 201 Created — Automatically applied 5-9 Ton Tier: ₹60,800/Ton
{
  "success": true,
  "message": "Item added to cart",
  "data": {
    "id": 201,
    "items": [
      {
        "productId": 1,
        "productName": "TATA Tiscon 550D TMT Bar (12mm)",
        "quantity": 5,
        "unitPrice": 60800.00,                               ← Bulk tier price applied
        "gstRate": 18.00,
        "subtotal": 304000.00
      }
    ],
    "subtotal": 304000.00,
    "gstTotal": 54720.00,
    "grandTotal": 358720.00
  }
}

// ❌ Response 400 Bad Request — Below MOQ or out of stock
{
  "success": false,
  "message": "Minimum Order Quantity for TATA Tiscon is 1"
}
```

### Step 2 — `GET /api/cart`
*Buyer opens cart view*

```http
GET /api/cart
```

```json
// ✅ Response 200 OK
{
  "success": true,
  "data": {
    "items": [
      {
        "id": 301,
        "productId": 1,
        "productName": "TATA Tiscon 550D TMT Bar (12mm)",
        "quantity": 5,
        "unitPrice": 60800.00,
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

## 🟢 FLOW 1.4: Checkout & Order Placement

### Step 1 — `POST /api/checkout/preview`
*Buyer navigates to Checkout. App requests tax and delivery calculations.*

```json
// Request
{}

// ✅ Response 200 OK
{
  "success": true,
  "data": {
    "subtotal": 304000.00,
    "gstAmount": 54720.00,
    "deliveryCharge": 2500.00,
    "totalAmount": 361220.00,
    "cgst": 27360.00,
    "sgst": 27360.00,
    "igst": 0.00
  }
}
```

### Step 2 — `POST /api/orders`
*Buyer enters delivery address, selects payment method (`UPI`), and taps "Place Order"*

```json
// Request
{
  "shippingAddress": "Site #7, Metro Line 3 Corridor, Hinjewadi Phase 2, Pune",
  "billingAddress": "Plot 45, MIDC Industrial Area, Phase 2, Pune",
  "city": "Pune",
  "state": "Maharashtra",
  "pincode": "411057",
  "paymentMethod": "UPI",
  "notes": "Gate #3 entry. Unloading crane available."
}

// ✅ Response 201 Created — Order created with status PLACED
{
  "success": true,
  "message": "Order placed successfully",
  "data": {
    "id": 115,                                               ← Save order ID for payment
    "orderNumber": "ORD-2026-0820-015",
    "totalAmount": 361220.00,
    "paymentMethod": "UPI",
    "paymentStatus": "PENDING",                              ← Awaiting payment
    "orderStatus": "PLACED"                                  ← Order placed
  }
}
```

---

## 🟢 FLOW 1.5: Razorpay Payment & Cryptographic Verification

### Step 1 — `POST /api/payments/create`
*Buyer taps "Proceed to Pay"*

```json
// Request
{
  "orderId": 115,
  "paymentMethod": "UPI"
}

// ✅ Response 201 Created — Pass gatewayOrderId to Razorpay Checkout Modal
{
  "success": true,
  "message": "Razorpay payment order initiated successfully",
  "data": {
    "id": 415,
    "orderId": 115,
    "orderNumber": "ORD-2026-0820-015",
    "amount": 361220.00,
    "currency": "INR",
    "amountInPaise": 36122000,
    "gatewayOrderId": "order_Rzp_20260820_015",              ← Pass to Razorpay JS
    "razorpayKeyId": "rzp_live_TO6q7NUVnPM6bA",
    "companyName": "HinchMart"
  }
}
```

### Step 2 — `POST /api/payments/verify`
*Razorpay modal completes payment and returns signature to frontend*

```json
// Request
{
  "paymentId": 415,
  "gatewayOrderId": "order_Rzp_20260820_015",
  "gatewayPaymentId": "pay_O5h72g89sA22",
  "gatewaySignature": "9f82ab7c31d8e..."
}

// ✅ Response 200 OK — Order moved to PAID & CONFIRMED + Auto GST Invoice!
{
  "success": true,
  "message": "Payment signature verified successfully",
  "data": {
    "id": 415,
    "orderId": 115,
    "amount": 361220.00,
    "paymentStatus": "SUCCESS"                               ← Payment successful!
  }
}

// ❌ Response 400 Bad Request — Tampered signature
{
  "success": false,
  "message": "Payment verification failed: Invalid Razorpay gateway signature."
}
```

---

## 🟢 FLOW 1.6: Buyer Order Tracking & GST Tax Invoice

### Step 1 — `GET /api/orders/:id/tracking`
*Buyer app polls tracking status or renders live timeline checkpoints*

```http
GET /api/orders/115/tracking
```

```json
// ✅ Response 200 OK
{
  "success": true,
  "data": {
    "orderId": 115,
    "carrierName": "VRL Logistics Heavy Freight",
    "trackingNumber": "VRL-2026-998811",
    "currentStatus": "IN_TRANSIT",                           ← Live milestone
    "estimatedDelivery": "2026-08-25",
    "checkpoints": [
      { "status": "MANIFESTED", "location": "Pune Yard",      "timestamp": "2026-08-20T17:00:00Z", "description": "Consignment booked." },
      { "status": "PICKED_UP",  "location": "Gate 2 Bay",     "timestamp": "2026-08-20T18:30:00Z", "description": "Loaded onto Flatbed Trailer." },
      { "status": "IN_TRANSIT", "location": "Pune Highway",   "timestamp": "2026-08-20T20:15:00Z", "description": "En route to Hinjewadi site." }
    ]
  }
}
```

### Step 2 — `GET /api/orders/:id/invoice`
*Buyer taps "Download Tax Invoice"*

```http
GET /api/orders/115/invoice
```

```json
// ✅ Response 200 OK — Full B2B GST Tax Invoice
{
  "success": true,
  "data": {
    "invoiceNumber": "INV-2026-000115",
    "orderNumber": "ORD-2026-0820-015",
    "sellerGstin": "27AAACT2727Q1ZW",
    "buyerGstin": "27AAAAA0000A1Z5",
    "taxableValue": 304000.00,
    "cgstAmount": 27360.00,
    "sgstAmount": 27360.00,
    "grandTotal": 361220.00,
    "paymentStatus": "PAID"
  }
}
```

---

## 🟢 FLOW 1.7: Buyer RFQ (Request For Quotation) Flow

### Step 1 — `POST /api/rfq`
*Buyer needs 50 Tons of structural steel and creates a custom quotation request*

```json
// Request
{
  "title": "50 Tons Fe-550D TMT Rebar for Commercial Mall Project",
  "categoryId": 1,
  "quantity": 50,
  "unit": "Ton",
  "targetPrice": 59000.00,
  "deliveryLocation": "Hinjewadi Phase 2, Pune",
  "requiredByDate": "2026-09-10"
}

// ✅ Response 201 Created
{
  "success": true,
  "message": "RFQ created successfully",
  "data": {
    "id": 601,
    "rfqNumber": "RFQ-2026-0820-001",
    "status": "OPEN"
  }
}
```

### Step 2 — `GET /api/buyer/rfq/:rfqId/quotes`
*Buyer views bids submitted by different sellers*

```http
GET /api/buyer/rfq/601/quotes
```

```json
// ✅ Response 200 OK
{
  "success": true,
  "data": [
    {
      "id": 901,
      "sellerCompanyName": "Tata Steel Distribution Hub Pvt Ltd",
      "pricePerUnit": 58500.00,
      "totalPrice": 2925000.00,
      "leadTimeDays": 5,
      "status": "SUBMITTED"
    }
  ]
}
```

### Step 3 — `POST /api/buyer/rfq/quotes/:id/accept`
*Buyer accepts winning bid and converts it directly into an order*

```json
// Request
{}

// ✅ Response 200 OK
{
  "success": true,
  "message": "Quotation accepted successfully. Order initialized.",
  "data": {
    "quoteId": 901,
    "status": "ACCEPTED",
    "convertedOrderId": 116
  }
}
```

---

# 🏭 PART 2: SELLER (USER) APIS & FLOWS

---

## 🟠 FLOW 2.1: Seller Received Orders & Courier Dispatch

### Step 1 — `GET /api/orders/seller`
*Seller checks list of new orders received*

```http
GET /api/orders/seller?page=0&size=10
```

```json
// ✅ Response 200 OK
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 115,
        "orderNumber": "ORD-2026-0820-015",
        "buyerName": "Rajesh Sharma",
        "totalAmount": 361220.00,
        "paymentStatus": "PAID",
        "orderStatus": "CONFIRMED"                            ← Ready to pack and ship
      }
    ]
  }
}
```

### Step 2 — `POST /api/seller/orders/:id/shipment`
*Seller books courier (VRL Logistics / Delhivery) and generates AWB tracking number*

```json
// Request
{
  "deliveryPartnerId": 2,
  "trackingNumber": "VRL-2026-998811",
  "awbCode": "AWB-VRL-998811",
  "shippingLabelUrl": "https://cdn.hinchmart.com/labels/vrl_998811.pdf",
  "estimatedDeliveryDate": "2026-08-25"
}

// ✅ Response 201 Created — Order moves to READY_TO_SHIP
{
  "success": true,
  "message": "Shipment booked successfully",
  "data": {
    "id": 701,
    "orderId": 115,
    "trackingNumber": "VRL-2026-998811",
    "status": "MANIFESTED"                                   ← Carrier booked
  }
}
```

### Step 3 — `PATCH /api/seller/shipments/:id/status`
*Seller / Carrier updates courier transit progress*

```json
// Request
{
  "status": "IN_TRANSIT",
  "location": "Pune Highway Toll Plaza Checkpoint",
  "notes": "En route to Pune Site delivery"
}

// ✅ Response 200 OK — Order status auto-syncs to SHIPPED
{
  "success": true,
  "message": "Shipment status updated to IN_TRANSIT",
  "data": {
    "id": 701,
    "status": "IN_TRANSIT",
    "orderStatus": "SHIPPED"                                 ← Order now SHIPPED
  }
}
```

---

## 🟠 FLOW 2.2: Seller Multi-Pincode Inventory & Catalog Management

### Step 1 — `POST /api/seller/inventory/pincode`
*Seller adds or updates available inventory and warehouse delivery SLA for a specific SKU and Pincode. Total product and central inventory stock are automatically synchronized.*

```http
POST /api/seller/inventory/pincode
Authorization: Bearer <sellerToken>
Content-Type: application/json
```

```json
// Request
{
  "sku": "JSW-NEO-550D-16MM",
  "pincode": "411057",
  "warehouseName": "Pune Hinjewadi Hub",
  "city": "Pune",
  "state": "Maharashtra",
  "quantity": 40,
  "deliveryDays": 1,
  "minOrderQuantity": 2
}

// ✅ Response 200 OK
{
  "success": true,
  "message": "Pincode inventory saved and total stock synchronized",
  "data": {
    "id": 5,
    "productId": 4,
    "productSku": "JSW-NEO-550D-16MM",
    "productName": "JSW Neosteel 550D TMT Bar (16mm)",
    "sellerId": 5,
    "sellerName": "Anand Verma",
    "pincode": "411057",
    "warehouseName": "Pune Hinjewadi Hub",
    "city": "Pune",
    "state": "Maharashtra",
    "quantity": 40,
    "reservedQuantity": 0,
    "availableQuantity": 40,
    "deliveryDays": 1,
    "minOrderQuantity": 2,
    "active": true,
    "serviceable": true,
    "updatedAt": "2026-08-22T02:13:28.123"
  },
  "timestamp": "2026-08-22T02:13:28.123"
}
```

### Step 2 — `POST /api/seller/inventory/pincode/bulk`
*Seller bulk-updates stock across multiple pincodes or SKUs.*

```http
POST /api/seller/inventory/pincode/bulk
Authorization: Bearer <sellerToken>
Content-Type: application/json
```

```json
// Request
[
  {
    "sku": "JSW-NEO-550D-16MM",
    "pincode": "411057",
    "warehouseName": "Pune Hinjewadi Hub",
    "city": "Pune",
    "quantity": 40,
    "deliveryDays": 1
  },
  {
    "sku": "JSW-NEO-550D-16MM",
    "pincode": "400001",
    "warehouseName": "Mumbai South Logistics Yard",
    "city": "Mumbai",
    "quantity": 30,
    "deliveryDays": 2
  }
]

// ✅ Response 200 OK
{
  "success": true,
  "message": "Bulk pincode inventories updated successfully",
  "data": [ ... ],
  "timestamp": "2026-08-22T02:13:28.123"
}
```

### Step 3 — `GET /api/seller/inventory/pincode/sku/:sku`
*Seller views all warehouse locations and stock allocations for a SKU.*

```http
GET /api/seller/inventory/pincode/sku/JSW-NEO-550D-16MM
Authorization: Bearer <sellerToken>
```

```json
// ✅ Response 200 OK
{
  "success": true,
  "data": [
    {
      "id": 5,
      "pincode": "411057",
      "warehouseName": "Pune Hinjewadi Hub",
      "quantity": 40,
      "availableQuantity": 40,
      "deliveryDays": 1
    },
    {
      "id": 6,
      "pincode": "400001",
      "warehouseName": "Mumbai South Logistics Yard",
      "quantity": 30,
      "availableQuantity": 30,
      "deliveryDays": 2
    }
  ]
}
```

### Step 4 — `POST /api/seller/products`
*Seller submits a new product catalog item for Admin approval.*

```json
// Request
{
  "productName": "UltraTech Super Cement (50kg Bag)",
  "categoryId": 2,
  "subcategoryId": 4,
  "brandId": 2,
  "sku": "ULTRA-SUPER-50KG",
  "description": "High performance blended cement for high strength concrete.",
  "hsnCode": "252329",
  "moq": 50,
  "stock": 2000,
  "unit": "BAG",
  "mrp": 420.00,
  "sellingPrice": 385.00,
  "gstRate": 28.00,
  "bulkPrices": [
    { "minQuantity": 50,  "maxQuantity": 199, "pricePerUnit": 385.00 },
    { "minQuantity": 200, "maxQuantity": 999, "pricePerUnit": 370.00 },
    { "minQuantity": 1000, "maxQuantity": 5000, "pricePerUnit": 355.00 }
  ]
}

// ✅ Response 201 Created — Status PENDING until Admin approves
{
  "success": true,
  "message": "Product submitted for approval",
  "data": {
    "id": 4,
    "productName": "UltraTech Super Cement (50kg Bag)",
    "approvalStatus": "PENDING",                             ← Awaiting Admin approval
    "isActive": false
  }
}
```

---

## 🟠 FLOW 2.3: Seller RFQ Bidding

### Step 1 — `GET /api/seller/rfq/open`
*Seller browses open RFQs from buyers*

```http
GET /api/seller/rfq/open
```

```json
// ✅ Response 200 OK
{
  "success": true,
  "data": [
    {
      "id": 601,
      "title": "50 Tons Fe-550D TMT Rebar for Commercial Mall Project",
      "quantity": 50,
      "unit": "Ton",
      "targetPrice": 59000.00,
      "requiredByDate": "2026-09-10"
    }
  ]
}
```

### Step 2 — `POST /api/seller/rfq/:rfqId/quotes`
*Seller submits bid / quotation*

```json
// Request
{
  "pricePerUnit": 58500.00,
  "leadTimeDays": 5,
  "validUntil": "2026-08-30",
  "comments": "Mill Test Certificate included."
}

// ✅ Response 201 Created
{
  "success": true,
  "message": "Quotation submitted successfully",
  "data": {
    "id": 901,
    "rfqId": 601,
    "pricePerUnit": 58500.00,
    "totalPrice": 2925000.00,
    "status": "SUBMITTED"
  }
}
```

---

# 🛡️ PART 3: ADMIN APIS & FLOWS (ADMIN ONLY)

---

## 🔴 FLOW 3.1: Admin Dashboard & Platform Statistics

### Step 1 — `GET /api/admin/dashboard`
*Admin opens the Control Center to monitor platform-wide metrics*

```http
GET /api/admin/dashboard
Authorization: Bearer <admin_token>
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

## 🔴 FLOW 3.2: Admin Seller KYC Approval Workflow

### Step 1 — `GET /api/admin/sellers?status=PENDING`
*Admin lists new sellers awaiting verification*

```http
GET /api/admin/sellers?status=PENDING
Authorization: Bearer <admin_token>
```

```json
// ✅ Response 200 OK
{
  "success": true,
  "data": [
    {
      "id": 5,
      "companyName": "Tata Steel Distribution Hub Pvt Ltd",
      "gstin": "27AAACT2727Q1ZW",
      "pan": "AAACT2727Q",
      "status": "PENDING"
    }
  ]
}
```

### Step 2 — `PATCH /api/admin/sellers/:id/approve`
*Admin verifies documents and approves the seller*

```http
PATCH /api/admin/sellers/5/approve
Authorization: Bearer <admin_token>
```

```json
// ✅ Response 200 OK — Seller is approved to list products & bid on RFQs
{
  "success": true,
  "message": "Seller approved successfully",
  "data": {
    "id": 5,
    "companyName": "Tata Steel Distribution Hub Pvt Ltd",
    "status": "APPROVED",                                    ← Seller is APPROVED
    "verifiedAt": "2026-08-20T17:30:00Z"
  }
}
```

### Step 2 (Alternative) — `PATCH /api/admin/sellers/:id/reject`
*Admin rejects seller with a reason*

```json
// Request Body: PATCH /api/admin/sellers/5/reject
{
  "rejectionReason": "Uploaded GSTIN certificate is blurry and unreadable."
}

// ✅ Response 200 OK
{
  "success": true,
  "message": "Seller rejected",
  "data": {
    "id": 5,
    "status": "REJECTED"
  }
}
```

---

## 🔴 FLOW 3.3: Admin Product Catalog Moderation

### Step 1 — `GET /api/admin/products?status=PENDING`
*Admin reviews submitted products*

```http
GET /api/admin/products?status=PENDING
Authorization: Bearer <admin_token>
```

```json
// ✅ Response 200 OK
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 4,
        "productName": "UltraTech Super Cement (50kg Bag)",
        "approvalStatus": "PENDING",
        "sellingPrice": 385.00
      }
    ]
  }
}
```

### Step 2 — `POST /api/admin/products`
*Admin directly creates a new Master SKU catalog entry with automatic central and pincode inventory population.*

```http
POST /api/admin/products
Authorization: Bearer <admin_token>
Content-Type: application/json
```

```json
// Request
{
  "productName": "JSW Neosteel 550D TMT Bar (16mm)",
  "categoryId": 1,
  "subcategoryId": 1,
  "brandId": 1,
  "sku": "JSW-NEO-550D-16MM",
  "hsnCode": "72142090",
  "gstRate": 18.00,
  "moq": 2,
  "unit": "TON",
  "mrp": 66000.00,
  "sellingPrice": 62000.00,
  "stock": 50,
  "deliveryDays": 2,
  "description": "High ductility earthquake-resistant Fe 550D grade steel rebars.",
  "specifications": "{\"Grade\":\"Fe 550D\",\"Diameter\":\"16mm\"}"
}

// ✅ Response 201 Created
{
  "success": true,
  "message": "SKU item created successfully by Admin",
  "data": {
    "id": 4,
    "productName": "JSW Neosteel 550D TMT Bar (16mm)",
    "sku": "JSW-NEO-550D-16MM",
    "approvalStatus": "APPROVED",
    "stock": 50,
    "sellingPrice": 62000.00
  },
  "timestamp": "2026-08-22T02:13:28.123"
}
```

### Step 3 — `PATCH /api/admin/products/:id/approve`
*Admin approves product to appear in public search and catalog*

```http
PATCH /api/admin/products/4/approve
Authorization: Bearer <admin_token>
```

```json
// ✅ Response 200 OK — Product is now live in public marketplace
{
  "success": true,
  "message": "Product approved successfully",
  "data": {
    "id": 4,
    "productName": "UltraTech Super Cement (50kg Bag)",
    "approvalStatus": "APPROVED",                            ← Live in marketplace
    "isActive": true
  }
}
```

---

## 🔴 FLOW 3.4: Admin Razorpay Payment Refund

### Step 1 — `POST /api/payments/:id/refund`
*Admin processes a refund via Razorpay API*

```json
// Request Body
{
  "amount": 361220.00,
  "reason": "Customer cancelled order before trailer dispatch."
}

// ✅ Response 200 OK — Refund completed directly on Razorpay
{
  "success": true,
  "message": "Refund processed successfully via Razorpay",
  "data": {
    "id": 601,
    "refundNumber": "REF-2026-0820-001",
    "paymentId": 415,
    "orderId": 115,
    "amount": 361220.00,
    "refundStatus": "PROCESSED",                             ← Refund successful
    "gatewayRefundId": "rfnd_O5h82x9A1b2c"
  }
}
```

---

# ⚠️ PART 4: STANDARD API RESPONSE & ERROR HANDLING

All API endpoints return standard, predictable JSON envelopes with ISO-8601 formatted timestamps:

### ✅ 1. Standard Success Response Envelope
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... },
  "timestamp": "2026-08-22T02:13:28.123"
}
```

### ❌ 2. Standard 401 Unauthorized Response (Missing, Expired, or Invalid Token)
```json
{
  "success": false,
  "message": "Authentication failed: Token is missing, expired, or invalid. Please provide a valid Bearer token.",
  "error": {
    "code": "UNAUTHORIZED",
    "message": "Authentication failed: Token is missing, expired, or invalid. Please provide a valid Bearer token.",
    "details": null
  },
  "timestamp": "2026-08-22T02:13:28.123"
}
```

### ❌ 3. Standard 403 Forbidden Response (Insufficient Role Permissions)
```json
{
  "success": false,
  "message": "Access denied: You do not have permission or the required role to perform this action.",
  "error": {
    "code": "ACCESS_DENIED",
    "message": "Access denied: You do not have permission or the required role to perform this action.",
    "details": null
  },
  "timestamp": "2026-08-22T02:13:28.123"
}
```

### ❌ 4. Standard 400 Validation Error Response
```json
{
  "success": false,
  "message": "Request validation failed",
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Request validation failed",
    "details": {
      "pincode": "Delivery pincode must be a 6-digit number",
      "quantity": "Quantity must be at least 1"
    }
  },
  "timestamp": "2026-08-22T02:13:28.123"
}
```

### ❌ 5. Standard 404 Not Found Response
```json
{
  "success": false,
  "message": "Product not found with SKU: JSW-INVALID-SKU",
  "error": {
    "code": "NOT_FOUND",
    "message": "Product not found with SKU: JSW-INVALID-SKU",
    "details": null
  },
  "timestamp": "2026-08-22T02:13:28.123"
}
```
