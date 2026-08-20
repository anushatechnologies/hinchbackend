# ==============================================================================
# HINCHMART B2B MARKETPLACE - AUTOMATED API & PAYMENT TEST SUITE
# ==============================================================================

$BaseUrl = "http://localhost:8081"
$ErrorActionPreference = "Continue"

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host " HINCHMART END-TO-END API & PAYMENT GATEWAY VERIFICATION " -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host ""

$Passed = 0
$Failed = 0

function Test-Endpoint {
    param(
        [string]$Name,
        [string]$Method,
        [string]$Path,
        [hashtable]$Headers = @{},
        [string]$Body = $null,
        [int]$ExpectedStatus = 200
    )

    $Uri = "$BaseUrl$Path"
    Write-Host -NoNewline "[$Method] $Path - $Name ... "
    
    try {
        $Params = @{
            Uri = $Uri
            Method = $Method
            ContentType = "application/json"
            Headers = $Headers
        }
        if ($Body) {
            $Params["Body"] = $Body
        }

        $Response = Invoke-WebRequest @Params -UseBasicParsing
        $StatusCode = $Response.StatusCode

        if ($StatusCode -eq $ExpectedStatus -or ($ExpectedStatus -eq 200 -and ($StatusCode -eq 200 -or $StatusCode -eq 201))) {
            Write-Host "PASS ($StatusCode)" -ForegroundColor Green
            $script:Passed++
            return ($Response.Content | ConvertFrom-Json)
        } else {
            Write-Host "FAIL (Expected $ExpectedStatus, Got $StatusCode)" -ForegroundColor Red
            $script:Failed++
            return $null
        }
    } catch {
        $StatusCode = $_.Exception.Response.StatusCode.value__
        if ($StatusCode -eq $ExpectedStatus) {
            Write-Host "PASS ($StatusCode)" -ForegroundColor Green
            $script:Passed++
        } else {
            Write-Host "ERROR ($StatusCode - $($_.Exception.Message))" -ForegroundColor Red
            $script:Failed++
        }
        return $null
    }
}

# 1. Test Public Razorpay Config
$ConfigRes = Test-Endpoint -Name "Get Razorpay Config" -Method "GET" -Path "/api/payments/config" -ExpectedStatus 200

# 2. Test Public Categories & Products Catalog
$CategoriesRes = Test-Endpoint -Name "Get Categories" -Method "GET" -Path "/api/categories" -ExpectedStatus 200
$BrandsRes = Test-Endpoint -Name "Get Brands" -Method "GET" -Path "/api/brands" -ExpectedStatus 200
$ProductsRes = Test-Endpoint -Name "Get Product Catalog" -Method "GET" -Path "/api/products?page=0&size=10" -ExpectedStatus 200

# 3. Test Authentication (Buyer Login)
$BuyerLoginBody = '{"identifier":"buyer@demo.com","password":"Buyer@123"}'
$BuyerAuth = Test-Endpoint -Name "Buyer Login" -Method "POST" -Path "/api/auth/login" -Body $BuyerLoginBody -ExpectedStatus 200
$BuyerToken = $BuyerAuth.data.accessToken
$BuyerHeaders = @{ "Authorization" = "Bearer $BuyerToken" }

# 4. Test Authentication (Seller Login)
$SellerLoginBody = '{"identifier":"seller@tata.com","password":"Seller@123"}'
$SellerAuth = Test-Endpoint -Name "Seller Login" -Method "POST" -Path "/api/auth/login" -Body $SellerLoginBody -ExpectedStatus 200
$SellerToken = $SellerAuth.data.accessToken
$SellerHeaders = @{ "Authorization" = "Bearer $SellerToken" }

# 5. Test Authentication (Admin Login)
$AdminLoginBody = '{"identifier":"admin@hinchmart.com","password":"Admin@123"}'
$AdminAuth = Test-Endpoint -Name "Admin Login" -Method "POST" -Path "/api/auth/login" -Body $AdminLoginBody -ExpectedStatus 200
$AdminToken = $AdminAuth.data.accessToken
$AdminHeaders = @{ "Authorization" = "Bearer $AdminToken" }

# 5A. Test Real OTP Dispatch (Email / Mobile)
$SendOtpBody = '{"identifier":"buyer@demo.com","purpose":"LOGIN"}'
$SendOtpRes = Test-Endpoint -Name "Send Real OTP to Email/Mobile" -Method "POST" -Path "/api/auth/send-otp" -Body $SendOtpBody -ExpectedStatus 200

# 5B. Test Real OTP Verification
$GeneratedOtp = ($SendOtpRes.data -replace "OTP:\s*", "").Trim()
if ($GeneratedOtp) {
    $VerifyOtpBody = "{`"identifier`":`"buyer@demo.com`",`"otpCode`":`"$GeneratedOtp`",`"purpose`":`"LOGIN`"}"
    $VerifyOtpRes = Test-Endpoint -Name "Verify OTP & Login" -Method "POST" -Path "/api/auth/verify-otp" -Body $VerifyOtpBody -ExpectedStatus 200
}

# 6. Test User Profile
$MeRes = Test-Endpoint -Name "Get Current Profile" -Method "GET" -Path "/api/auth/me" -Headers $BuyerHeaders -ExpectedStatus 200

# 7. Test Cart Management
$ClearCart = Test-Endpoint -Name "Clear Cart" -Method "DELETE" -Path "/api/cart/clear" -Headers $BuyerHeaders -ExpectedStatus 200
$AddToCartBody = '{"productId":1,"quantity":5}'
$AddToCartRes = Test-Endpoint -Name "Add 5 Tons TMT Rebar to Cart" -Method "POST" -Path "/api/cart/items" -Headers $BuyerHeaders -Body $AddToCartBody -ExpectedStatus 201
$GetCartRes = Test-Endpoint -Name "Get Buyer Cart" -Method "GET" -Path "/api/cart" -Headers $BuyerHeaders -ExpectedStatus 200

# 8. Test Checkout Preview
$PreviewRes = Test-Endpoint -Name "Preview Checkout Breakdown" -Method "POST" -Path "/api/checkout/preview" -Headers $BuyerHeaders -Body "{}" -ExpectedStatus 200

# 9. Test Order Placement (UPI Flow)
$OrderBody = '{
  "shippingAddress": "Site #7, Metro Line 3 Corridor, Hinjewadi Phase 2, Pune",
  "billingAddress": "Plot 45, MIDC Industrial Area, Phase 2, Pune",
  "city": "Pune",
  "state": "Maharashtra",
  "pincode": "411057",
  "paymentMethod": "UPI",
  "notes": "Automated Test Order - Full API Verification"
}'
$CreateOrderRes = Test-Endpoint -Name "Place Order (UPI)" -Method "POST" -Path "/api/orders" -Headers $BuyerHeaders -Body $OrderBody -ExpectedStatus 201
$NewOrderId = $CreateOrderRes.data.id

if ($NewOrderId) {
    # 10. Test Razorpay Payment Order Creation
    $PayCreateBody = "{`"orderId`":$NewOrderId,`"paymentMethod`":`"UPI`"}"
    $PayCreateRes = Test-Endpoint -Name "Create Razorpay Payment Order" -Method "POST" -Path "/api/payments/create" -Headers $BuyerHeaders -Body $PayCreateBody -ExpectedStatus 201
    $PaymentId = $PayCreateRes.data.id
    $GatewayOrderId = $PayCreateRes.data.gatewayOrderId

    # 11. Test Razorpay Signature Verification
    if ($PaymentId -and $GatewayOrderId) {
        $PayVerifyBody = "{`"paymentId`":$PaymentId,`"gatewayOrderId`":`"$GatewayOrderId`",`"gatewayPaymentId`":`"pay_test_$(Get-Random)`",`"gatewaySignature`":`"test_signature`"}"
        $PayVerifyRes = Test-Endpoint -Name "Verify Payment & Confirm Order" -Method "POST" -Path "/api/payments/verify" -Headers $BuyerHeaders -Body $PayVerifyBody -ExpectedStatus 200
    }

    # 12. Test Order Details & Status History
    $GetOrderRes = Test-Endpoint -Name "Get Order Details by ID" -Method "GET" -Path "/api/orders/$NewOrderId" -Headers $BuyerHeaders -ExpectedStatus 200

    # 13. Test Payment Details by Order ID
    $GetPaymentRes = Test-Endpoint -Name "Get Payment Details by Order ID" -Method "GET" -Path "/api/payments/order/$NewOrderId" -Headers $BuyerHeaders -ExpectedStatus 200

    # 14. Test GST Tax Invoice Generation
    $GetInvoiceRes = Test-Endpoint -Name "Get GST Tax Invoice" -Method "GET" -Path "/api/orders/$NewOrderId/invoice" -Headers $BuyerHeaders -ExpectedStatus 200

    # 15. Test Shipment Creation by Seller
    $ShipmentBody = '{
      "deliveryPartnerId": 2,
      "trackingNumber": "VRL-TEST-998811",
      "awbCode": "AWB-VRL-998811",
      "shippingLabelUrl": "https://cdn.hinchmart.com/labels/test.pdf",
      "estimatedDeliveryDate": "2026-08-25",
      "notes": "Dispatched via VRL Logistics Heavy Freight"
    }'
    $ShipmentRes = Test-Endpoint -Name "Seller Create Shipment" -Method "POST" -Path "/api/seller/orders/$NewOrderId/shipment" -Headers $SellerHeaders -Body $ShipmentBody -ExpectedStatus 201

    # 16. Test Live Order Tracking
    $TrackingRes = Test-Endpoint -Name "Get Order Tracking Checkpoints" -Method "GET" -Path "/api/orders/$NewOrderId/tracking" -Headers $BuyerHeaders -ExpectedStatus 200
}

# 17. Test Order History Lists
$BuyerOrders = Test-Endpoint -Name "Buyer Order History" -Method "GET" -Path "/api/orders?page=0&size=10" -Headers $BuyerHeaders -ExpectedStatus 200
$SellerOrders = Test-Endpoint -Name "Seller Received Orders" -Method "GET" -Path "/api/orders/seller?page=0&size=10" -Headers $SellerHeaders -ExpectedStatus 200

# 18. Test Notifications
$NotifRes = Test-Endpoint -Name "Get Notifications" -Method "GET" -Path "/api/notifications" -Headers $BuyerHeaders -ExpectedStatus 200
$UnreadCount = Test-Endpoint -Name "Get Unread Notifications" -Method "GET" -Path "/api/notifications/unread" -Headers $BuyerHeaders -ExpectedStatus 200

# 19. Test Admin Dashboard & Management
$AdminStats = Test-Endpoint -Name "Admin Dashboard Stats" -Method "GET" -Path "/api/admin/dashboard" -Headers $AdminHeaders -ExpectedStatus 200
$AdminUsers = Test-Endpoint -Name "Admin List All Users" -Method "GET" -Path "/api/admin/users" -Headers $AdminHeaders -ExpectedStatus 200
$AdminSellers = Test-Endpoint -Name "Admin List Sellers" -Method "GET" -Path "/api/admin/sellers" -Headers $AdminHeaders -ExpectedStatus 200
$AdminProducts = Test-Endpoint -Name "Admin List Products" -Method "GET" -Path "/api/admin/products" -Headers $AdminHeaders -ExpectedStatus 200

Write-Host ""
Write-Host "==========================================================" -ForegroundColor Cyan
if ($Failed -eq 0) {
    Write-Host " TEST EXECUTION SUMMARY: Passed = $Passed, Failed = $Failed (100% ALL PASSED!) " -ForegroundColor Green
} else {
    Write-Host " TEST EXECUTION SUMMARY: Passed = $Passed, Failed = $Failed " -ForegroundColor Red
}
Write-Host "==========================================================" -ForegroundColor Cyan
