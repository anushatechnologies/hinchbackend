package com.hinchmart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hinchmart.dto.request.LoginRequest;
import com.hinchmart.dto.request.SellerAddressUpdateRequest;
import com.hinchmart.dto.request.SellerLegalUpdateRequest;
import com.hinchmart.dto.request.SellerProfileUpdateRequest;
import com.hinchmart.dto.request.SellerRegisterRequest;
import com.hinchmart.dto.request.SellerVerifyOtpRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SellerFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCompleteSellerAuthAndOnboardingFlow() throws Exception {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String email = "seller_" + uniqueId + "@testcompany.com";
        String phone = "+919" + (System.currentTimeMillis() % 1000000000L);

        // 1. Seller Registration
        SellerRegisterRequest regReq = new SellerRegisterRequest();
        regReq.setFullName("Anand Verma");
        regReq.setEmail(email);
        regReq.setPhone(phone);
        regReq.setCompanyName("Verma Steel & Pipes LLP");
        regReq.setBusinessType("Distributor");
        regReq.setGstin("27AAACV1234E1Z5");
        regReq.setPassword("Password@123");
        regReq.setConfirmPassword("Password@123");

        mockMvc.perform(post("/api/v1/auth/seller/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.requiresOtp").value(true));

        // 2. Verify OTP
        SellerVerifyOtpRequest otpReq = new SellerVerifyOtpRequest(phone, "123456", "REGISTER");
        String verifyRes = mockMvc.perform(post("/api/v1/auth/seller/verify-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(otpReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andReturn().getResponse().getContentAsString();

        String token = objectMapper.readTree(verifyRes).path("data").path("token").asText();

        // 3. Get Profile
        mockMvc.perform(get("/api/v1/seller/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.companyName").value("Verma Steel & Pipes LLP"));

        // 4. Update Profile
        SellerProfileUpdateRequest profReq = new SellerProfileUpdateRequest();
        profReq.setEstablishedYear(2016);
        profReq.setEmployees("50-100");
        profReq.setWebsite("https://vermasteel.com");
        profReq.setDescription("Authorized supplier of structural steel and TMT bars.");

        mockMvc.perform(patch("/api/v1/seller/profile")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.progress").isNumber());

        // 5. Update Address
        SellerAddressUpdateRequest addrReq = new SellerAddressUpdateRequest();
        addrReq.setState("Maharashtra");
        addrReq.setCity("Mumbai");
        addrReq.setArea("Kalamboli Steel Market");
        addrReq.setPincode("410218");
        addrReq.setCompleteAddress("Plot 45, Kalamboli Steel Market, Navi Mumbai - 410218");

        mockMvc.perform(patch("/api/v1/seller/address")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addrReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // 6. Update Legal & Bank Details
        SellerLegalUpdateRequest legalReq = new SellerLegalUpdateRequest();
        legalReq.setGstin("27AAACV1234E1Z5");
        legalReq.setPan("AAACV1234E");
        legalReq.setCin("U27100MH2016LLP123456");
        legalReq.setTradeLicense("BMC/TL/2026/10293");
        legalReq.setMsme("UDYAM-MH-33-1029384");
        legalReq.setBankAccountNumber("987654321012");
        legalReq.setBankIfscCode("HDFC0001234");
        legalReq.setBankName("HDFC Bank");
        legalReq.setBankAccountName("Verma Steel and Pipes LLP");

        mockMvc.perform(patch("/api/v1/seller/legal")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(legalReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // 7. Submit For Verification
        mockMvc.perform(post("/api/v1/seller/submit-verification")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.verificationStatus").value("UNDER_REVIEW"));
    }

    @Test
    public void testAdminHubEndpoints() throws Exception {
        // Login as Super Admin
        LoginRequest adminLogin = new LoginRequest("superadmin@hinchmart.com", "SuperAdmin@123");
        String adminRes = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLogin)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String adminToken = objectMapper.readTree(adminRes).path("data").path("accessToken").asText();

        // 1. Admin Buyers Directory
        mockMvc.perform(get("/api/admin/buyers")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray());

        // 2. Admin Global Orders
        mockMvc.perform(get("/api/admin/orders")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray());

        // 3. Admin Global Payments
        mockMvc.perform(get("/api/admin/payments")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray());
    }
}
