package com.hinchmart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hinchmart.dto.request.LoginRequest;
import com.hinchmart.dto.request.PincodeInventoryRequest;
import com.hinchmart.dto.request.ProductCreateRequest;
import com.hinchmart.entity.enums.ProductUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PincodeInventoryTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSkuCreationAndMultiPincodeInventorySync() throws Exception {
        // 1. Seller Login
        LoginRequest loginRequest = new LoginRequest("seller@tata.com", "Seller@123");
        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String token = objectMapper.readTree(loginResponse).path("data").path("accessToken").asText();

        // 2. Create a new SKU item with initial stock
        String uniqueSku = "JSW-NEO-TMT-" + System.currentTimeMillis();
        ProductCreateRequest createRequest = new ProductCreateRequest();
        createRequest.setProductName("JSW Neosteel 550D Fe Rebar");
        createRequest.setCategoryId(1L);
        createRequest.setSubcategoryId(1L);
        createRequest.setBrandId(1L);
        createRequest.setSku(uniqueSku);
        createRequest.setHsnCode("72142090");
        createRequest.setGstRate(new BigDecimal("18.00"));
        createRequest.setMoq(1);
        createRequest.setUnit(ProductUnit.TON);
        createRequest.setMrp(new BigDecimal("66000.00"));
        createRequest.setSellingPrice(new BigDecimal("62000.00"));
        createRequest.setStock(50);
        createRequest.setDeliveryDays(2);
        createRequest.setDescription("JSW Neosteel high strength TMT bars.");

        String createProductRes = mockMvc.perform(post("/api/seller/products")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.sku").value(uniqueSku))
                .andExpect(jsonPath("$.data.stock").value(50))
                .andExpect(jsonPath("$.data.pincodeInventories", hasSize(1)))
                .andReturn().getResponse().getContentAsString();

        Long createdProductId = objectMapper.readTree(createProductRes).path("data").path("id").asLong();

        // 3. Add inventory for a second pincode (Mumbai 400001) for this SKU
        PincodeInventoryRequest mumbaiInv = new PincodeInventoryRequest();
        mumbaiInv.setProductId(createdProductId);
        mumbaiInv.setSku(uniqueSku);
        mumbaiInv.setPincode("400001");
        mumbaiInv.setWarehouseName("Mumbai South Yard");
        mumbaiInv.setCity("Mumbai");
        mumbaiInv.setState("Maharashtra");
        mumbaiInv.setQuantity(30);
        mumbaiInv.setDeliveryDays(1);

        mockMvc.perform(post("/api/seller/inventory/pincode")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mumbaiInv)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.pincode").value("400001"))
                .andExpect(jsonPath("$.data.quantity").value(30));

        // 4. Verify total product stock is synchronized (50 + 30 = 80)
        mockMvc.perform(get("/api/seller/products/" + createdProductId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.stock").value(80))
                .andExpect(jsonPath("$.data.pincodeInventories", hasSize(2)));

        // 5. Query all pincode inventories for this SKU
        mockMvc.perform(get("/api/seller/inventory/pincode/sku/" + uniqueSku)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[*].pincode", hasItem("400001")));

        // 6. Check stock availability for Mumbai Pincode 400001 (Requires Bearer token, any role e.g. BUYER)
        LoginRequest buyerLogin = new LoginRequest("buyer@demo.com", "Buyer@123");
        String buyerLoginRes = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buyerLogin)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String buyerToken = objectMapper.readTree(buyerLoginRes).path("data").path("accessToken").asText();

        // 6a. Unauthenticated request to /api/inventory/check-availability should return 401 with JSON error body
        mockMvc.perform(get("/api/inventory/check-availability")
                        .param("skuOrId", uniqueSku)
                        .param("pincode", "400001")
                        .param("quantity", "20"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("UNAUTHORIZED"));

        // 6b. Authenticated request with Buyer Bearer token succeeds
        mockMvc.perform(get("/api/inventory/check-availability")
                        .header("Authorization", "Bearer " + buyerToken)
                        .param("skuOrId", uniqueSku)
                        .param("pincode", "400001")
                        .param("quantity", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.pincode").value("400001"))
                .andExpect(jsonPath("$.data.availableQuantity").value(30))
                .andExpect(jsonPath("$.data.serviceable").value(true));

        // 7. Admin approves the product catalog entry
        LoginRequest adminLogin = new LoginRequest("admin@hinchmart.com", "Admin@123");
        String adminLoginRes = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLogin)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String adminToken = objectMapper.readTree(adminLoginRes).path("data").path("accessToken").asText();

        mockMvc.perform(patch("/api/admin/products/" + createdProductId + "/approve")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.approvalStatus").value("APPROVED"));

        // 8. Get all SKUs available under Category 1 (Steel Rods & Rebars) - Public
        mockMvc.perform(get("/api/products")
                        .param("categoryId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$.data.content[*].sku", hasItem(uniqueSku)));

        // 9. Get all SKUs available in Pincode 411057 (Pune) under Category 1 - Authenticated with Buyer token
        mockMvc.perform(get("/api/inventory/pincode/411057")
                        .header("Authorization", "Bearer " + buyerToken)
                        .param("categoryId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.data[0].pincode").value("411057"));
    }
}
