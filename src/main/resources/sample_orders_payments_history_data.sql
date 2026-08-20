-- ==============================================================================
-- HINCHMART B2B MARKETPLACE - SAMPLE SEED DATA SCRIPT (MySQL 8.0+ Compatible)
-- Tables: orders, order_items, order_status_history, payments, 
--         payment_transactions, refunds, shipments, shipment_tracking, 
--         invoices, invoice_items, activity_logs, notifications
-- ==============================================================================

USE hinchmart_db;

-- Temporarily disable foreign key checks to ensure clean insertion
SET FOREIGN_KEY_CHECKS = 0;

-- ------------------------------------------------------------------------------
-- 1. ORDERS TABLE (`orders`)
-- ------------------------------------------------------------------------------
INSERT INTO `orders` (
    `id`, `order_number`, `buyer_id`, `shipping_address`, `billing_address`, 
    `city`, `state`, `pincode`, `subtotal`, `gst_amount`, `delivery_charge`, 
    `total_amount`, `payment_method`, `payment_status`, `order_status`, 
    `notes`, `created_at`, `updated_at`
) VALUES 
-- Order 1: Delivered & Paid (Bulk 10 Tons TMT Rebar Order)
(
    101, 'ORD-2026-0810-001', 4, 
    'Site #7, Metro Line 3 Corridor, Hinjewadi Phase 2, Pune, Maharashtra - 411057', 
    'Plot 45, MIDC Industrial Area, Phase 2, Pune, Maharashtra - 411057', 
    'Pune', 'Maharashtra', '411057', 
    599000.00, 107820.00, 4500.00, 711320.00, 
    'NEFT_RTGS', 'PAID', 'DELIVERED', 
    'Heavy commercial trailer access required at Gate 3. Unloading crane available on site.', 
    '2026-08-10 10:30:00', '2026-08-13 16:45:00'
),
-- Order 2: In Transit / Shipped & Paid (200 Bags PPC Cement)
(
    102, 'ORD-2026-0815-002', 4, 
    'Site #7, Metro Line 3 Corridor, Hinjewadi Phase 2, Pune, Maharashtra - 411057', 
    'Plot 45, MIDC Industrial Area, Phase 2, Pune, Maharashtra - 411057', 
    'Pune', 'Maharashtra', '411057', 
    73000.00, 20440.00, 2000.00, 95440.00, 
    'UPI', 'PAID', 'SHIPPED', 
    'Dispatch with waterproof tarpaulin cover. Deliver during daytime 09:00 - 18:00.', 
    '2026-08-15 11:15:00', '2026-08-17 09:00:00'
),
-- Order 3: Out For Delivery & Paid (500m HDPE Pipes)
(
    103, 'ORD-2026-0817-003', 4, 
    'Site #7, Metro Line 3 Corridor, Hinjewadi Phase 2, Pune, Maharashtra - 411057', 
    'Plot 45, MIDC Industrial Area, Phase 2, Pune, Maharashtra - 411057', 
    'Pune', 'Maharashtra', '411057', 
    56000.00, 10080.00, 1500.00, 67580.00, 
    'CREDIT_CARD', 'PAID', 'OUT_FOR_DELIVERY', 
    'Coil packaging with batch inspection tags attached.', 
    '2026-08-17 14:20:00', '2026-08-19 08:30:00'
),
-- Order 4: Confirmed & Paid (4 Tons TMT Rebars - Warehouse Processing)
(
    104, 'ORD-2026-0818-004', 4, 
    'Site #7, Metro Line 3 Corridor, Hinjewadi Phase 2, Pune, Maharashtra - 411057', 
    'Plot 45, MIDC Industrial Area, Phase 2, Pune, Maharashtra - 411057', 
    'Pune', 'Maharashtra', '411057', 
    246000.00, 44280.00, 3000.00, 293280.00, 
    'NET_BANKING', 'PAID', 'CONFIRMED', 
    'Quality test certificates (MTC) required along with delivery challan.', 
    '2026-08-18 09:45:00', '2026-08-18 10:00:00'
),
-- Order 5: Cancelled & Refunded (100 Bags Cement)
(
    105, 'ORD-2026-0812-005', 4, 
    'Site #7, Metro Line 3 Corridor, Hinjewadi Phase 2, Pune, Maharashtra - 411057', 
    'Plot 45, MIDC Industrial Area, Phase 2, Pune, Maharashtra - 411057', 
    'Pune', 'Maharashtra', '411057', 
    38000.00, 10640.00, 1200.00, 49840.00, 
    'UPI', 'REFUNDED', 'CANCELLED', 
    'Buyer requested cancellation prior to dispatch due to site schedule revision.', 
    '2026-08-12 16:00:00', '2026-08-13 11:30:00'
),
-- Order 6: Placed & Pending Payment (20 Tons TMT Rebars - RTGS Verification)
(
    106, 'ORD-2026-0819-006', 4, 
    'Site #7, Metro Line 3 Corridor, Hinjewadi Phase 2, Pune, Maharashtra - 411057', 
    'Plot 45, MIDC Industrial Area, Phase 2, Pune, Maharashtra - 411057', 
    'Pune', 'Maharashtra', '411057', 
    1170000.00, 210600.00, 6000.00, 1386600.00, 
    'NEFT_RTGS', 'PENDING', 'PLACED', 
    'Bulk procurement of 20 Tons TMT Rebars for Stage 2 casting.', 
    '2026-08-19 15:30:00', '2026-08-19 15:30:00'
)
AS new_order
ON DUPLICATE KEY UPDATE 
    `order_number` = new_order.`order_number`,
    `total_amount` = new_order.`total_amount`,
    `payment_status` = new_order.`payment_status`,
    `order_status` = new_order.`order_status`;

-- ------------------------------------------------------------------------------
-- 2. ORDER ITEMS TABLE (`order_items`)
-- ------------------------------------------------------------------------------
INSERT INTO `order_items` (
    `id`, `order_id`, `product_id`, `seller_id`, `product_name`, `sku`, 
    `unit_price`, `quantity`, `unit`, `gst_percentage`, `gst_amount`, `total_price`
) VALUES 
-- Items for Order 101 (10 Tons TATA Tiscon 550D)
(
    201, 101, 1, 5, 'TATA Tiscon 550D TMT Bar', 'TATA-TISCON-550D-12MM', 
    59900.00, 10, 'TON', 18.00, 107820.00, 706820.00
),
-- Items for Order 102 (200 Bags UltraTech Super PPC)
(
    202, 102, 2, 5, 'UltraTech Super Cement (PPC 50kg Bag)', 'ULTRATECH-SUPER-PPC-50KG', 
    365.00, 200, 'BAG', 28.00, 20440.00, 93440.00
),
-- Items for Order 103 (500 Meters Astral HDPE Pipe)
(
    203, 103, 3, 5, 'Astral Taurus PE 100 HDPE Pipe 63mm PN 10', 'ASTRAL-HDPE-63MM-PN10', 
    112.00, 500, 'METER', 18.00, 10080.00, 66080.00
),
-- Items for Order 104 (4 Tons TATA Tiscon 550D)
(
    204, 104, 1, 5, 'TATA Tiscon 550D TMT Bar', 'TATA-TISCON-550D-12MM', 
    61500.00, 4, 'TON', 18.00, 44280.00, 290280.00
),
-- Items for Order 105 (100 Bags UltraTech Cement - Cancelled)
(
    205, 105, 2, 5, 'UltraTech Super Cement (PPC 50kg Bag)', 'ULTRATECH-SUPER-PPC-50KG', 
    380.00, 100, 'BAG', 28.00, 10640.00, 48640.00
),
-- Items for Order 106 (20 Tons TATA Tiscon 550D)
(
    206, 106, 1, 5, 'TATA Tiscon 550D TMT Bar', 'TATA-TISCON-550D-12MM', 
    58500.00, 20, 'TON', 18.00, 210600.00, 1380600.00
)
AS new_item
ON DUPLICATE KEY UPDATE 
    `product_name` = new_item.`product_name`,
    `unit_price` = new_item.`unit_price`,
    `quantity` = new_item.`quantity`,
    `total_price` = new_item.`total_price`;

-- ------------------------------------------------------------------------------
-- 3. ORDER STATUS HISTORY TABLE (`order_status_history`)
-- ------------------------------------------------------------------------------
INSERT INTO `order_status_history` (
    `id`, `order_id`, `status`, `notes`, `changed_by_user_id`, `created_at`
) VALUES 
-- Order 101 Full Lifecycle (Placed -> Confirmed -> Processing -> Ready to Ship -> Shipped -> Out for Delivery -> Delivered)
(301, 101, 'PLACED', 'Order placed successfully by Buyer via B2B Portal', 4, '2026-08-10 10:30:00'),
(302, 101, 'CONFIRMED', 'RTGS Payment confirmed & stock allocated by Tata Steel Distribution Hub', 5, '2026-08-10 11:45:00'),
(303, 101, 'PROCESSING', 'Material weighed & loaded at Kalamboli Steel Yard Bay 4', 5, '2026-08-11 09:15:00'),
(304, 101, 'READY_TO_SHIP', 'Dispatched manifest created. Quality inspection & MTC attached', 5, '2026-08-11 15:30:00'),
(305, 101, 'SHIPPED', 'Consignment handed over to VRL Logistics Heavy Freight', 5, '2026-08-12 08:00:00'),
(306, 101, 'OUT_FOR_DELIVERY', 'Vehicle MH-46-AR-8821 out for delivery to Hinjewadi Site #7', 2, '2026-08-13 08:30:00'),
(307, 101, 'DELIVERED', 'Consignment received and digital POD signed by Site Engineer', 4, '2026-08-13 16:45:00'),

-- Order 102 Lifecycle (Shipped Stage)
(308, 102, 'PLACED', 'Order placed and paid via Razorpay Instant UPI', 4, '2026-08-15 11:15:00'),
(309, 102, 'CONFIRMED', 'Order confirmed by seller warehouse', 5, '2026-08-15 11:30:00'),
(310, 102, 'PROCESSING', '200 Bags palletized with moisture-resistant wrap', 5, '2026-08-16 10:00:00'),
(311, 102, 'SHIPPED', 'Dispatched with Delhivery B2B Freight (AWB: DEL-883921092)', 5, '2026-08-17 09:00:00'),

-- Order 103 Lifecycle (Out For Delivery)
(312, 103, 'PLACED', 'Order placed online with Corporate Card', 4, '2026-08-17 14:20:00'),
(313, 103, 'CONFIRMED', 'Stock confirmed at Panvel Logistics Center', 5, '2026-08-17 14:35:00'),
(314, 103, 'SHIPPED', 'In transit with Rivigo Express Surface (AWB: RIV-5519827)', 5, '2026-08-18 11:00:00'),
(315, 103, 'OUT_FOR_DELIVERY', 'Arrived at Pune Hub. Dispatched on delivery vehicle', 2, '2026-08-19 08:30:00'),

-- Order 104 Lifecycle (Confirmed)
(316, 104, 'PLACED', 'Order initiated by buyer', 4, '2026-08-18 09:45:00'),
(317, 104, 'CONFIRMED', 'Net banking payment verified. Ready for warehouse processing', 2, '2026-08-18 10:00:00'),

-- Order 105 Lifecycle (Cancelled & Refunded)
(318, 105, 'PLACED', 'Order placed by buyer', 4, '2026-08-12 16:00:00'),
(319, 105, 'CONFIRMED', 'Payment verified', 2, '2026-08-12 16:05:00'),
(320, 105, 'CANCELLED', 'Order cancelled upon buyer request. Refund initiated', 4, '2026-08-13 11:30:00'),

-- Order 106 Lifecycle (Placed)
(321, 106, 'PLACED', 'Order placed. Awaiting RTGS transfer remittance slip', 4, '2026-08-19 15:30:00')
AS new_history
ON DUPLICATE KEY UPDATE 
    `status` = new_history.`status`,
    `notes` = new_history.`notes`;

-- ------------------------------------------------------------------------------
-- 4. PAYMENTS TABLE (`payments`)
-- ------------------------------------------------------------------------------
INSERT INTO `payments` (
    `id`, `payment_number`, `order_id`, `buyer_id`, `amount`, `currency`, 
    `payment_method`, `payment_status`, `gateway_order_id`, `gateway_payment_id`, 
    `gateway_signature`, `error_code`, `error_description`, `created_at`, `updated_at`
) VALUES 
-- Payment for Order 101 (NEFT / RTGS)
(
    401, 'PAY-20260810-001', 101, 4, 711320.00, 'INR', 
    'NEFT_RTGS', 'PAID', 'rtgs_utr_hdfc_9981245012', 'HDFCN26222891001', 
    'SIG-VERIFIED-HDFC-RTGS-711320', NULL, NULL, 
    '2026-08-10 10:35:00', '2026-08-10 11:45:00'
),
-- Payment for Order 102 (Razorpay UPI)
(
    402, 'PAY-20260815-002', 102, 4, 95440.00, 'INR', 
    'UPI', 'PAID', 'order_Rzp_20260815_002', 'pay_Rzp_991823901A', 
    'e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855', NULL, NULL, 
    '2026-08-15 11:15:00', '2026-08-15 11:20:00'
),
-- Payment for Order 103 (Corporate Credit Card)
(
    403, 'PAY-20260817-003', 103, 4, 67580.00, 'INR', 
    'CREDIT_CARD', 'PAID', 'order_Rzp_20260817_003', 'pay_Rzp_881928472B', 
    'c2f35e9821a00e56114b72944b20912f7a04a3952a65d07c0a6b7d5a864d4b12', NULL, NULL, 
    '2026-08-17 14:20:00', '2026-08-17 14:22:00'
),
-- Payment for Order 104 (Net Banking)
(
    404, 'PAY-20260818-004', 104, 4, 293280.00, 'INR', 
    'NET_BANKING', 'PAID', 'order_Rzp_20260818_004', 'pay_Rzp_772819034C', 
    '9b71d224bd62f3785d96d46ad3ea3d73319bfbc2890caadae2dff72519673ca72', NULL, NULL, 
    '2026-08-18 09:45:00', '2026-08-18 09:50:00'
),
-- Payment for Order 105 (Refunded Payment)
(
    405, 'PAY-20260812-005', 105, 4, 49840.00, 'INR', 
    'UPI', 'REFUNDED', 'order_Rzp_20260812_005', 'pay_Rzp_661928374D', 
    '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', NULL, 'Order cancelled by buyer before dispatch', 
    '2026-08-12 16:00:00', '2026-08-13 11:40:00'
),
-- Payment for Order 106 (Pending Payment)
(
    406, 'PAY-20260819-006', 106, 4, 1386600.00, 'INR', 
    'NEFT_RTGS', 'PENDING', 'rtgs_pending_ord_106', NULL, 
    NULL, NULL, NULL, 
    '2026-08-19 15:30:00', '2026-08-19 15:30:00'
)
AS new_payment
ON DUPLICATE KEY UPDATE 
    `payment_number` = new_payment.`payment_number`,
    `amount` = new_payment.`amount`,
    `payment_status` = new_payment.`payment_status`;

-- ------------------------------------------------------------------------------
-- 5. PAYMENT TRANSACTIONS TABLE (`payment_transactions`)
-- ------------------------------------------------------------------------------
INSERT INTO `payment_transactions` (
    `id`, `payment_id`, `transaction_type`, `amount`, `status`, 
    `gateway_reference`, `response_payload`, `created_at`
) VALUES 
-- RTGS Settlement Transaction
(
    501, 401, 'PAYMENT', 711320.00, 'SUCCESS', 
    'HDFCN26222891001', 
    '{"bank":"HDFC Bank","utr":"HDFCN26222891001","remitter":"Apex Infra Projects Pvt Ltd","beneficiary":"Tata Steel Distribution Hub","timestamp":"2026-08-10T11:45:00"}', 
    '2026-08-10 11:45:00'
),
-- UPI Settlement Transaction
(
    502, 402, 'PAYMENT', 95440.00, 'SUCCESS', 
    'pay_Rzp_991823901A', 
    '{"gateway":"Razorpay","vpa":"apexinfra@okhdfcbank","mode":"UPI","status":"captured","fee":0.00,"tax":0.00}', 
    '2026-08-15 11:20:00'
),
-- Corporate Credit Card Transaction
(
    503, 403, 'PAYMENT', 67580.00, 'SUCCESS', 
    'pay_Rzp_881928472B', 
    '{"gateway":"Razorpay","card_network":"Visa Business","card_last4":"4412","status":"captured"}', 
    '2026-08-17 14:22:00'
),
-- Net Banking Transaction
(
    504, 404, 'PAYMENT', 293280.00, 'SUCCESS', 
    'pay_Rzp_772819034C', 
    '{"gateway":"Razorpay","bank":"ICICI Bank Corporate","status":"captured"}', 
    '2026-08-18 09:50:00'
),
-- Order 105 Payment & Subsequent Refund Transactions
(
    505, 405, 'PAYMENT', 49840.00, 'SUCCESS', 
    'pay_Rzp_661928374D', 
    '{"gateway":"Razorpay","vpa":"apexinfra@okhdfcbank","mode":"UPI","status":"captured"}', 
    '2026-08-12 16:02:00'
),
(
    506, 405, 'REFUND', 49840.00, 'SUCCESS', 
    'rfnd_Rzp_551928374R', 
    '{"gateway":"Razorpay","refund_id":"rfnd_Rzp_551928374R","amount":49840.00,"speed":"normal","status":"processed"}', 
    '2026-08-13 11:40:00'
)
AS new_trx
ON DUPLICATE KEY UPDATE 
    `status` = new_trx.`status`,
    `gateway_reference` = new_trx.`gateway_reference`;

-- ------------------------------------------------------------------------------
-- 6. REFUNDS TABLE (`refunds`)
-- ------------------------------------------------------------------------------
INSERT INTO `refunds` (
    `id`, `refund_number`, `payment_id`, `order_id`, `amount`, `reason`, 
    `refund_status`, `gateway_refund_id`, `created_at`, `updated_at`
) VALUES 
(
    601, 'RFND-20260813-001', 405, 105, 49840.00, 
    'Order cancellation requested by client before material loading.', 
    'PROCESSED', 'rfnd_Rzp_551928374R', 
    '2026-08-13 11:35:00', '2026-08-13 11:40:00'
)
AS new_refund
ON DUPLICATE KEY UPDATE 
    `refund_status` = new_refund.`refund_status`,
    `gateway_refund_id` = new_refund.`gateway_refund_id`;

-- ------------------------------------------------------------------------------
-- 7. SHIPMENTS TABLE (`shipments`)
-- ------------------------------------------------------------------------------
INSERT INTO `shipments` (
    `id`, `shipment_number`, `order_id`, `seller_id`, `delivery_partner_id`, 
    `tracking_number`, `awb_code`, `shipping_label_url`, `status`, 
    `estimated_delivery_date`, `actual_delivery_date`, `shipping_address`, 
    `notes`, `created_at`, `updated_at`
) VALUES 
-- Shipment for Order 101 (Delivered via VRL Logistics)
(
    701, 'SHP-20260811-001', 101, 5, 2, 
    'VRL-PUN-2026-88910', 'AWB-VRL-88910', 
    'https://cdn.hinchmart.com/labels/SHP-20260811-001.pdf', 'DELIVERED', 
    '2026-08-13', '2026-08-13 16:45:00', 
    'Site #7, Metro Line 3 Corridor, Hinjewadi Phase 2, Pune, Maharashtra - 411057', 
    'Heavy 10-wheel flatbed trailer (Vehicle: MH-46-AR-8821). Driver Contact: 9819001122.', 
    '2026-08-11 15:30:00', '2026-08-13 16:45:00'
),
-- Shipment for Order 102 (In Transit via Delhivery B2B)
(
    702, 'SHP-20260816-002', 102, 5, 1, 
    'DEL-883921092', 'AWB-DEL-883921092', 
    'https://cdn.hinchmart.com/labels/SHP-20260816-002.pdf', 'IN_TRANSIT', 
    '2026-08-18', NULL, 
    'Site #7, Metro Line 3 Corridor, Hinjewadi Phase 2, Pune, Maharashtra - 411057', 
    'Palletized 200 bags cement. Waterproof covered freight truck.', 
    '2026-08-16 14:00:00', '2026-08-17 09:00:00'
),
-- Shipment for Order 103 (Out for Delivery via Rivigo)
(
    703, 'SHP-20260818-003', 103, 5, 3, 
    'RIV-5519827', 'AWB-RIV-5519827', 
    'https://cdn.hinchmart.com/labels/SHP-20260818-003.pdf', 'OUT_FOR_DELIVERY', 
    '2026-08-19', NULL, 
    'Site #7, Metro Line 3 Corridor, Hinjewadi Phase 2, Pune, Maharashtra - 411057', 
    '5 Bundles HDPE 63mm Coils. Direct site dispatch.', 
    '2026-08-18 10:00:00', '2026-08-19 08:30:00'
)
AS new_shipment
ON DUPLICATE KEY UPDATE 
    `status` = new_shipment.`status`,
    `tracking_number` = new_shipment.`tracking_number`,
    `actual_delivery_date` = new_shipment.`actual_delivery_date`;

-- ------------------------------------------------------------------------------
-- 8. SHIPMENT TRACKING CHECKPOINTS TABLE (`shipment_tracking`)
-- ------------------------------------------------------------------------------
INSERT INTO `shipment_tracking` (
    `id`, `shipment_id`, `status`, `location`, `description`, `timestamp`
) VALUES 
-- Shipment 701 Tracking History
(801, 701, 'PENDING', 'Kalamboli Steel Yard, Navi Mumbai', 'Shipment manifest registered and order picked', '2026-08-11 15:30:00'),
(802, 701, 'PICKED_UP', 'Kalamboli Steel Yard, Navi Mumbai', 'Loaded onto 10-wheel flatbed trailer MH-46-AR-8821', '2026-08-12 08:00:00'),
(803, 701, 'IN_TRANSIT', 'Mumbai-Pune Expressway Toll Plaza', 'Consignment in transit towards Pune hub', '2026-08-12 13:30:00'),
(804, 701, 'REACHED_DESTINATION', 'VRL Logistics Hub, Chakan, Pune', 'Arrived at Chakan distribution yard for delivery allocation', '2026-08-12 19:45:00'),
(805, 701, 'OUT_FOR_DELIVERY', 'Hinjewadi Sector, Pune', 'Out for final delivery to Hinjewadi Site #7', '2026-08-13 08:30:00'),
(806, 701, 'DELIVERED', 'Site #7 Metro Corridor, Hinjewadi Phase 2, Pune', 'Successfully unloaded and verified by Site Engineer Rajesh Sharma', '2026-08-13 16:45:00'),

-- Shipment 702 Tracking History
(807, 702, 'PENDING', 'Bhiwandi Central Depot, Mumbai', 'Dispatch manifest created by warehouse', '2026-08-16 14:00:00'),
(808, 702, 'PICKED_UP', 'Bhiwandi Central Depot, Mumbai', 'Palletized stock picked up by Delhivery B2B Freight', '2026-08-16 18:30:00'),
(809, 702, 'IN_TRANSIT', 'Panvel Freight Hub', 'Consignment departing from Panvel Hub towards Pune', '2026-08-17 09:00:00'),

-- Shipment 703 Tracking History
(810, 703, 'PICKED_UP', 'Panvel Logistics Center', 'Package picked up by Rivigo Express', '2026-08-18 11:00:00'),
(811, 703, 'IN_TRANSIT', 'Rivigo Relay Hub, Lonavala', 'Relay checkpoint passed smoothly', '2026-08-18 17:00:00'),
(812, 703, 'REACHED_DESTINATION', 'Rivigo Pune West Hub', 'Consignment unloaded at Pune hub', '2026-08-18 22:30:00'),
(813, 703, 'OUT_FOR_DELIVERY', 'Hinjewadi delivery zone, Pune', 'Out for delivery to construction site', '2026-08-19 08:30:00')
AS new_tracking
ON DUPLICATE KEY UPDATE 
    `status` = new_tracking.`status`,
    `location` = new_tracking.`location`,
    `description` = new_tracking.`description`;

-- ------------------------------------------------------------------------------
-- 9. INVOICES TABLE (`invoices`)
-- ------------------------------------------------------------------------------
INSERT INTO `invoices` (
    `id`, `invoice_number`, `order_id`, `order_number`, `seller_id`, 
    `seller_name`, `seller_company_name`, `seller_gstin`, `buyer_id`, 
    `buyer_name`, `buyer_company_name`, `buyer_gstin`, `billing_address`, 
    `shipping_address`, `place_of_supply`, `is_intra_state`, `taxable_value`, 
    `cgst_amount`, `sgst_amount`, `igst_amount`, `total_gst`, `delivery_charge`, 
    `grand_total`, `payment_status`, `invoice_date`, `created_at`
) VALUES 
-- Invoice 901 for Order 101
(
    901, 'INV-2026-000101', 101, 'ORD-2026-0810-001', 5, 
    'Anand Verma', 'Tata Steel Distribution Hub Pvt Ltd', '27AAACT2727Q1ZW', 4, 
    'Rajesh Sharma', 'Apex Infra Projects Pvt Ltd', '27AAAAA0000A1Z5', 
    'Plot 45, MIDC Industrial Area, Phase 2, Pune, Maharashtra - 411057', 
    'Site #7, Metro Line 3 Corridor, Hinjewadi Phase 2, Pune, Maharashtra - 411057', 
    'Maharashtra (27)', 1, 599000.00, 
    53910.00, 53910.00, 0.00, 107820.00, 4500.00, 
    711320.00, 'PAID', '2026-08-10', '2026-08-10 11:45:00'
),
-- Invoice 902 for Order 102
(
    902, 'INV-2026-000102', 102, 'ORD-2026-0815-002', 5, 
    'Anand Verma', 'Tata Steel Distribution Hub Pvt Ltd', '27AAACT2727Q1ZW', 4, 
    'Rajesh Sharma', 'Apex Infra Projects Pvt Ltd', '27AAAAA0000A1Z5', 
    'Plot 45, MIDC Industrial Area, Phase 2, Pune, Maharashtra - 411057', 
    'Site #7, Metro Line 3 Corridor, Hinjewadi Phase 2, Pune, Maharashtra - 411057', 
    'Maharashtra (27)', 1, 73000.00, 
    10220.00, 10220.00, 0.00, 20440.00, 2000.00, 
    95440.00, 'PAID', '2026-08-15', '2026-08-15 11:20:00'
),
-- Invoice 903 for Order 103
(
    903, 'INV-2026-000103', 103, 'ORD-2026-0817-003', 5, 
    'Anand Verma', 'Tata Steel Distribution Hub Pvt Ltd', '27AAACT2727Q1ZW', 4, 
    'Rajesh Sharma', 'Apex Infra Projects Pvt Ltd', '27AAAAA0000A1Z5', 
    'Plot 45, MIDC Industrial Area, Phase 2, Pune, Maharashtra - 411057', 
    'Site #7, Metro Line 3 Corridor, Hinjewadi Phase 2, Pune, Maharashtra - 411057', 
    'Maharashtra (27)', 1, 56000.00, 
    5040.00, 5040.00, 0.00, 10080.00, 1500.00, 
    67580.00, 'PAID', '2026-08-17', '2026-08-17 14:22:00'
),
-- Invoice 904 for Order 104
(
    904, 'INV-2026-000104', 104, 'ORD-2026-0818-004', 5, 
    'Anand Verma', 'Tata Steel Distribution Hub Pvt Ltd', '27AAACT2727Q1ZW', 4, 
    'Rajesh Sharma', 'Apex Infra Projects Pvt Ltd', '27AAAAA0000A1Z5', 
    'Plot 45, MIDC Industrial Area, Phase 2, Pune, Maharashtra - 411057', 
    'Site #7, Metro Line 3 Corridor, Hinjewadi Phase 2, Pune, Maharashtra - 411057', 
    'Maharashtra (27)', 1, 246000.00, 
    22140.00, 22140.00, 0.00, 44280.00, 3000.00, 
    293280.00, 'PAID', '2026-08-18', '2026-08-18 09:50:00'
)
AS new_invoice
ON DUPLICATE KEY UPDATE 
    `invoice_number` = new_invoice.`invoice_number`,
    `grand_total` = new_invoice.`grand_total`,
    `payment_status` = new_invoice.`payment_status`;

-- ------------------------------------------------------------------------------
-- 10. INVOICE ITEMS TABLE (`invoice_items`)
-- ------------------------------------------------------------------------------
INSERT INTO `invoice_items` (
    `id`, `invoice_id`, `product_id`, `product_name`, `hsn_code`, 
    `quantity`, `unit`, `unit_price`, `taxable_value`, `gst_rate`, 
    `cgst_rate`, `cgst_amount`, `sgst_rate`, `sgst_amount`, `igst_rate`, 
    `igst_amount`, `total_amount`
) VALUES 
-- Items for Invoice 901
(
    1001, 901, 1, 'TATA Tiscon 550D TMT Bar', '72142090', 
    10, 'TON', 59900.00, 599000.00, 18.00, 
    9.00, 53910.00, 9.00, 53910.00, 0.00, 
    0.00, 706820.00
),
-- Items for Invoice 902
(
    1002, 902, 2, 'UltraTech Super Cement (PPC 50kg Bag)', '25232910', 
    200, 'BAG', 365.00, 73000.00, 28.00, 
    14.00, 10220.00, 14.00, 10220.00, 0.00, 
    0.00, 93440.00
),
-- Items for Invoice 903
(
    1003, 903, 3, 'Astral Taurus PE 100 HDPE Pipe 63mm PN 10', '39172110', 
    500, 'METER', 112.00, 56000.00, 18.00, 
    9.00, 5040.00, 9.00, 5040.00, 0.00, 
    0.00, 66080.00
),
-- Items for Invoice 904
(
    1004, 904, 1, 'TATA Tiscon 550D TMT Bar', '72142090', 
    4, 'TON', 61500.00, 246000.00, 18.00, 
    9.00, 22140.00, 9.00, 22140.00, 0.00, 
    0.00, 290280.00
)
AS new_inv_item
ON DUPLICATE KEY UPDATE 
    `product_name` = new_inv_item.`product_name`,
    `total_amount` = new_inv_item.`total_amount`;

-- ------------------------------------------------------------------------------
-- 11. ACTIVITY LOGS TABLE (`activity_logs`)
-- ------------------------------------------------------------------------------
INSERT INTO `activity_logs` (
    `id`, `user_id`, `user_email`, `action`, `entity_type`, `entity_id`, 
    `details`, `ip_address`, `timestamp`
) VALUES 
(
    1101, 4, 'buyer@demo.com', 'ORDER_CREATED', 'ORDER', 101, 
    'Buyer placed Order ORD-2026-0810-001 for 10 Tons Tata Tiscon 550D', '103.21.124.5', '2026-08-10 10:30:00'
),
(
    1102, 4, 'buyer@demo.com', 'PAYMENT_COMPLETED', 'PAYMENT', 401, 
    'RTGS Payment of INR 7,11,320.00 processed for Order ORD-2026-0810-001', '103.21.124.5', '2026-08-10 11:45:00'
),
(
    1103, 5, 'seller@tata.com', 'SHIPMENT_DISPATCHED', 'SHIPMENT', 701, 
    'Shipment SHP-20260811-001 handed over to VRL Logistics Heavy Freight', '49.36.110.82', '2026-08-12 08:00:00'
),
(
    1104, 4, 'buyer@demo.com', 'ORDER_DELIVERED', 'ORDER', 101, 
    'Order ORD-2026-0810-001 marked as delivered. Digital POD acknowledged', '103.21.124.5', '2026-08-13 16:45:00'
),
(
    1105, 4, 'buyer@demo.com', 'ORDER_CREATED', 'ORDER', 102, 
    'Buyer placed Order ORD-2026-0815-002 for 200 Bags UltraTech Super PPC Cement', '103.21.124.5', '2026-08-15 11:15:00'
),
(
    1106, 4, 'buyer@demo.com', 'PAYMENT_COMPLETED', 'PAYMENT', 402, 
    'UPI Payment of INR 95,440.00 verified via Razorpay', '103.21.124.5', '2026-08-15 11:20:00'
),
(
    1107, 5, 'seller@tata.com', 'SHIPMENT_CREATED', 'SHIPMENT', 702, 
    'Shipment SHP-20260816-002 created with Delhivery B2B Freight', '49.36.110.82', '2026-08-16 14:00:00'
),
(
    1108, 4, 'buyer@demo.com', 'ORDER_CANCELLED', 'ORDER', 105, 
    'Buyer requested cancellation for Order ORD-2026-0812-005', '103.21.124.5', '2026-08-13 11:30:00'
),
(
    1109, 2, 'admin@hinchmart.com', 'REFUND_PROCESSED', 'REFUND', 601, 
    'Refund of INR 49,840.00 processed via Razorpay API for Order ORD-2026-0812-005', '182.74.241.10', '2026-08-13 11:40:00'
)
AS new_log
ON DUPLICATE KEY UPDATE 
    `action` = new_log.`action`,
    `details` = new_log.`details`;

-- ------------------------------------------------------------------------------
-- 12. NOTIFICATIONS TABLE (`notifications`)
-- ------------------------------------------------------------------------------
INSERT INTO `notifications` (
    `id`, `recipient_id`, `title`, `message`, `type`, 
    `reference_id`, `reference_type`, `is_read`, `created_at`
) VALUES 
(
    1201, 4, 'Order Placed Successfully', 
    'Your order ORD-2026-0810-001 for 10 Tons Tata Tiscon 550D TMT Bar has been placed.', 
    'ORDER_PLACED', 101, 'ORDER', 1, '2026-08-10 10:30:00'
),
(
    1202, 4, 'Payment Confirmed', 
    'Payment of ₹7,11,320.00 received successfully for Order ORD-2026-0810-001.', 
    'PAYMENT_SUCCESS', 401, 'PAYMENT', 1, '2026-08-10 11:45:00'
),
(
    1203, 4, 'Order Shipped', 
    'Your order ORD-2026-0810-001 has been dispatched via VRL Logistics. Tracking #: VRL-PUN-2026-88910', 
    'ORDER_SHIPPED', 701, 'SHIPMENT', 1, '2026-08-12 08:00:00'
),
(
    1204, 4, 'Order Delivered', 
    'Your order ORD-2026-0810-001 has been delivered to Site #7 Hinjewadi. Thank you for choosing HinchMart!', 
    'ORDER_DELIVERED', 101, 'ORDER', 1, '2026-08-13 16:45:00'
),
(
    1205, 5, 'New Order Received', 
    'You have received a new order ORD-2026-0815-002 for 200 Bags UltraTech PPC Cement.', 
    'ORDER_PLACED', 102, 'ORDER', 0, '2026-08-15 11:15:00'
),
(
    1206, 4, 'Shipment In Transit', 
    'Your order ORD-2026-0815-002 has departed from Panvel Freight Hub via Delhivery.', 
    'ORDER_SHIPPED', 702, 'SHIPMENT', 0, '2026-08-17 09:00:00'
),
(
    1207, 4, 'Out For Delivery', 
    'Order ORD-2026-0817-003 is out for delivery today with Rivigo Express Surface.', 
    'OUT_FOR_DELIVERY', 703, 'SHIPMENT', 0, '2026-08-19 08:30:00'
),
(
    1208, 4, 'Refund Processed', 
    'A refund of ₹49,840.00 for cancelled order ORD-2026-0812-005 has been credited back to your account.', 
    'PAYMENT_SUCCESS', 601, 'REFUND', 1, '2026-08-13 11:40:00'
)
AS new_notif
ON DUPLICATE KEY UPDATE 
    `title` = new_notif.`title`,
    `message` = new_notif.`message`,
    `is_read` = new_notif.`is_read`;

-- Re-enable Foreign Key constraints
SET FOREIGN_KEY_CHECKS = 1;
