package com.hinchmart.service;

import com.hinchmart.dto.request.SellerAddressUpdateRequest;
import com.hinchmart.dto.request.SellerLegalUpdateRequest;
import com.hinchmart.dto.request.SellerProfileUpdateRequest;
import com.hinchmart.dto.response.SellerDocumentDto;
import com.hinchmart.dto.response.SellerOnboardingProfileDto;
import com.hinchmart.entity.SellerDocument;
import com.hinchmart.entity.SellerProfile;
import com.hinchmart.entity.User;
import com.hinchmart.entity.enums.AccountStatus;
import com.hinchmart.entity.enums.ApprovalStatus;
import com.hinchmart.entity.enums.DocumentType;
import com.hinchmart.entity.enums.SellerStatus;
import com.hinchmart.exception.BadRequestException;
import com.hinchmart.exception.ResourceNotFoundException;
import com.hinchmart.repository.SellerDocumentRepository;
import com.hinchmart.repository.SellerProfileRepository;
import com.hinchmart.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class SellerOnboardingService {

    private final UserRepository userRepository;
    private final SellerProfileRepository sellerProfileRepository;
    private final SellerDocumentRepository sellerDocumentRepository;
    private final ActivityLogService activityLogService;

    public SellerOnboardingService(UserRepository userRepository,
                                   SellerProfileRepository sellerProfileRepository,
                                   SellerDocumentRepository sellerDocumentRepository,
                                   ActivityLogService activityLogService) {
        this.userRepository = userRepository;
        this.sellerProfileRepository = sellerProfileRepository;
        this.sellerDocumentRepository = sellerDocumentRepository;
        this.activityLogService = activityLogService;
    }

    @Transactional(readOnly = true)
    public SellerOnboardingProfileDto getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller user not found with ID: " + userId));

        SellerProfile sp = user.getSellerProfile();
        if (sp == null) {
            sp = sellerProfileRepository.findByUserId(userId)
                    .orElseGet(() -> {
                        SellerProfile draft = new SellerProfile(user, user.getFullName() + " Trading Co.", null, "Distributor", SellerStatus.DRAFT);
                        return sellerProfileRepository.save(draft);
                    });
        }

        return mapToOnboardingDto(user, sp);
    }

    @Transactional
    public Map<String, Object> updateCompanyProfile(Long userId, SellerProfileUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller user not found with ID: " + userId));

        SellerProfile sp = getOrCreateSellerProfile(user);

        if (request.getCompanyName() != null && !request.getCompanyName().trim().isEmpty()) {
            sp.setCompanyName(request.getCompanyName().trim());
        }
        if (request.getBusinessType() != null) {
            sp.setBusinessType(request.getBusinessType());
        }
        if (request.getEstablishedYear() != null) {
            sp.setEstablishedYear(request.getEstablishedYear());
        }
        if (request.getEmployees() != null) {
            sp.setEmployeeCount(request.getEmployees());
        }
        if (request.getWebsite() != null) {
            sp.setWebsite(request.getWebsite());
        }
        if (request.getCompanyEmail() != null) {
            sp.setCompanyEmail(request.getCompanyEmail());
        }
        if (request.getBusinessPhone() != null) {
            sp.setBusinessPhone(request.getBusinessPhone());
        }
        if (request.getDescription() != null) {
            sp.setDescription(request.getDescription());
        }

        recalculateProgress(sp);
        sellerProfileRepository.save(sp);

        activityLogService.log(userId, user.getEmail(), "SELLER_PROFILE_UPDATED", "SELLER", sp.getId(),
                "Updated company details", null);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("progress", sp.getCompletionPercentage());
        response.put("data", mapToOnboardingDto(user, sp));
        return response;
    }

    @Transactional
    public Map<String, Object> updateAddress(Long userId, SellerAddressUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller user not found with ID: " + userId));

        SellerProfile sp = getOrCreateSellerProfile(user);

        if (request.getCountry() != null) {
            sp.setCountry(request.getCountry());
        }
        if (request.getState() != null) {
            sp.setState(request.getState());
        }
        if (request.getDistrict() != null) {
            sp.setDistrict(request.getDistrict());
        }
        if (request.getCity() != null) {
            sp.setCity(request.getCity());
        }
        if (request.getArea() != null) {
            sp.setArea(request.getArea());
        }
        if (request.getPincode() != null) {
            sp.setPincode(request.getPincode());
        }
        if (request.getCompleteAddress() != null) {
            sp.setCompleteAddress(request.getCompleteAddress());
            sp.setWarehouseAddress(request.getCompleteAddress());
        }

        recalculateProgress(sp);
        sellerProfileRepository.save(sp);

        activityLogService.log(userId, user.getEmail(), "SELLER_ADDRESS_UPDATED", "SELLER", sp.getId(),
                "Updated operational address", null);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("progress", sp.getCompletionPercentage());
        response.put("data", mapToOnboardingDto(user, sp));
        return response;
    }

    @Transactional
    public Map<String, Object> updateLegal(Long userId, SellerLegalUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller user not found with ID: " + userId));

        SellerProfile sp = getOrCreateSellerProfile(user);

        if (request.getGstin() != null) {
            sp.setGstin(request.getGstin());
        }
        if (request.getPan() != null) {
            sp.setPanNumber(request.getPan());
        }
        if (request.getCin() != null) {
            sp.setCin(request.getCin());
        }
        if (request.getTradeLicense() != null) {
            sp.setTradeLicense(request.getTradeLicense());
        }
        if (request.getMsme() != null) {
            sp.setMsme(request.getMsme());
        }
        if (request.getBankAccountNumber() != null) {
            sp.setBankAccountNumber(request.getBankAccountNumber());
        }
        if (request.getBankIfscCode() != null) {
            sp.setBankIfscCode(request.getBankIfscCode());
        }
        if (request.getBankName() != null) {
            sp.setBankName(request.getBankName());
        }
        if (request.getBankAccountName() != null) {
            sp.setBankAccountName(request.getBankAccountName());
        }

        recalculateProgress(sp);
        sellerProfileRepository.save(sp);

        activityLogService.log(userId, user.getEmail(), "SELLER_LEGAL_UPDATED", "SELLER", sp.getId(),
                "Updated legal and tax details", null);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("progress", sp.getCompletionPercentage());
        response.put("data", mapToOnboardingDto(user, sp));
        return response;
    }

    @Transactional
    public Map<String, Object> uploadDocument(Long userId, String documentTypeStr, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Please select a valid document file to upload");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller user not found with ID: " + userId));

        SellerProfile sp = getOrCreateSellerProfile(user);

        DocumentType docType = parseDocumentType(documentTypeStr);

        String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "document.pdf";
        String formattedFileSize = formatFileSize(file.getSize());

        String uploadDir = "uploads/sellers/seller_" + sp.getId();
        String storedFileName = System.currentTimeMillis() + "_" + originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");
        String fileUrl = "https://storage.hinchmart.com/sellers/seller_" + sp.getId() + "/" + storedFileName;

        try {
            Path targetDir = Paths.get(uploadDir);
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }
            Path targetFile = targetDir.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            // Fallback URL if local filesystem fails
            fileUrl = "/uploads/documents/" + storedFileName;
        }

        SellerDocument doc = sellerDocumentRepository.findBySellerProfileIdAndDocumentType(sp.getId(), docType)
                .orElseGet(() -> new SellerDocument(sp, docType, "", originalFilename, formattedFileSize));

        doc.setDocumentUrl(fileUrl);
        doc.setFileName(originalFilename);
        doc.setFileSize(formattedFileSize);
        doc.setVerificationStatus(ApprovalStatus.PENDING);
        SellerDocument savedDoc = sellerDocumentRepository.save(doc);

        recalculateProgress(sp);
        sellerProfileRepository.save(sp);

        SellerDocumentDto docDto = new SellerDocumentDto(
                "doc_" + savedDoc.getId(),
                savedDoc.getDocumentType().name(),
                savedDoc.getFileName(),
                savedDoc.getFileSize(),
                savedDoc.getDocumentUrl(),
                savedDoc.getVerificationStatus().name(),
                savedDoc.getCreatedAt() != null ? savedDoc.getCreatedAt() : LocalDateTime.now()
        );

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("progress", sp.getCompletionPercentage());
        response.put("data", docDto);
        return response;
    }

    @Transactional
    public Map<String, Object> submitForVerification(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller user not found with ID: " + userId));

        SellerProfile sp = getOrCreateSellerProfile(user);
        sp.setStatus(SellerStatus.UNDER_REVIEW);
        sp.setCompletionPercentage(100);
        sellerProfileRepository.save(sp);

        activityLogService.log(userId, user.getEmail(), "SELLER_VERIFICATION_SUBMITTED", "SELLER", sp.getId(),
                "Submitted business profile for onboarding verification", null);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("verificationStatus", "UNDER_REVIEW");
        response.put("message", "Application submitted for review");
        return response;
    }

    private SellerProfile getOrCreateSellerProfile(User user) {
        if (user.getSellerProfile() != null) {
            return user.getSellerProfile();
        }
        return sellerProfileRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    SellerProfile profile = new SellerProfile(user, user.getFullName() + " Trading Co.", null, "Distributor", SellerStatus.DRAFT);
                    return sellerProfileRepository.save(profile);
                });
    }

    private void recalculateProgress(SellerProfile sp) {
        int progress = 25; // initial registration
        if (sp.getEstablishedYear() != null || sp.getDescription() != null || sp.getEmployeeCount() != null) {
            progress = Math.max(progress, 40);
        }
        if (sp.getCity() != null && sp.getState() != null && (sp.getCompleteAddress() != null || sp.getWarehouseAddress() != null)) {
            progress = Math.max(progress, 60);
        }
        if (sp.getGstin() != null && sp.getPanNumber() != null) {
            progress = Math.max(progress, 80);
        }
        List<SellerDocument> docs = sellerDocumentRepository.findBySellerProfileId(sp.getId());
        if (docs != null && !docs.isEmpty()) {
            progress = Math.max(progress, 95);
        }
        if (sp.getStatus() == SellerStatus.UNDER_REVIEW || sp.getStatus() == SellerStatus.VERIFIED || sp.getStatus() == SellerStatus.APPROVED) {
            progress = 100;
        }
        sp.setCompletionPercentage(progress);
    }

    private DocumentType parseDocumentType(String typeStr) {
        if (typeStr == null) return DocumentType.GST_CERTIFICATE;
        String clean = typeStr.trim().toUpperCase();
        try {
            return DocumentType.valueOf(clean);
        } catch (Exception e) {
            if (clean.contains("GST")) return DocumentType.GST_CERTIFICATE;
            if (clean.contains("PAN")) return DocumentType.PAN_CARD;
            if (clean.contains("CHEQUE")) return DocumentType.CANCELLED_CHEQUE;
            if (clean.contains("MSME")) return DocumentType.MSME;
            if (clean.contains("INCORP")) return DocumentType.INCORPORATION;
            if (clean.contains("LICENSE")) return DocumentType.TRADE_LICENSE;
            return DocumentType.OTHER;
        }
    }

    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format(Locale.ENGLISH, "%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }

    private SellerOnboardingProfileDto mapToOnboardingDto(User user, SellerProfile sp) {
        SellerOnboardingProfileDto dto = new SellerOnboardingProfileDto();
        dto.setId("seller_" + sp.getId());
        dto.setName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone() != null ? user.getPhone() : sp.getBusinessPhone());
        dto.setCompanyName(sp.getCompanyName());
        dto.setBusinessType(sp.getBusinessType());
        dto.setEstablishedYear(sp.getEstablishedYear());
        dto.setEmployees(sp.getEmployeeCount());
        dto.setWebsite(sp.getWebsite());
        dto.setCompanyEmail(sp.getCompanyEmail() != null ? sp.getCompanyEmail() : user.getEmail());
        dto.setBusinessPhone(sp.getBusinessPhone() != null ? sp.getBusinessPhone() : user.getPhone());
        dto.setDescription(sp.getDescription());

        // Address map
        Map<String, Object> address = new LinkedHashMap<>();
        address.put("country", sp.getCountry() != null ? sp.getCountry() : "India");
        address.put("state", sp.getState());
        address.put("district", sp.getDistrict());
        address.put("city", sp.getCity());
        address.put("area", sp.getArea());
        address.put("pincode", sp.getPincode());
        address.put("completeAddress", sp.getCompleteAddress() != null ? sp.getCompleteAddress() : sp.getWarehouseAddress());
        dto.setAddress(address);

        // Legal map
        Map<String, Object> legal = new LinkedHashMap<>();
        legal.put("gstin", sp.getGstin());
        legal.put("pan", sp.getPanNumber());
        legal.put("cin", sp.getCin());
        legal.put("tradeLicense", sp.getTradeLicense());
        legal.put("msme", sp.getMsme());
        dto.setLegal(legal);

        // Verification progress checklist
        List<SellerDocument> docs = sellerDocumentRepository.findBySellerProfileId(sp.getId());
        boolean hasApprovedDocs = docs != null && docs.stream().anyMatch(d -> d.getVerificationStatus() == ApprovalStatus.APPROVED);

        Map<String, Boolean> checklist = new LinkedHashMap<>();
        checklist.put("mobileVerified", user.getPhone() != null && user.getStatus() == AccountStatus.ACTIVE);
        checklist.put("emailVerified", user.getEmail() != null);
        checklist.put("businessDetails", sp.getEstablishedYear() != null && sp.getDescription() != null);
        checklist.put("gstVerified", sp.getGstin() != null && !sp.getGstin().trim().isEmpty());
        checklist.put("documentsApproved", hasApprovedDocs || (docs != null && !docs.isEmpty()));
        dto.setVerificationProgress(checklist);

        dto.setVerificationStatus(sp.getStatus().name());
        dto.setCompletionPercentage(sp.getCompletionPercentage());

        return dto;
    }
}
