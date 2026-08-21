package com.hinchmart.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hinchmart.entity.enums.DocumentType;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SellerDocumentDto {

    private Object id;
    private DocumentType documentType;
    private String documentTypeStr;
    private String fileName;
    private String fileSize;
    private String documentUrl;
    private String fileUrl;
    private String documentNumber;
    private String verificationStatus;
    private Boolean verified;
    private LocalDateTime verifiedAt;
    private LocalDateTime uploadedAt;

    public SellerDocumentDto() {
    }

    public SellerDocumentDto(Object id, String documentTypeStr, String fileName, String fileSize, String fileUrl, String verificationStatus, LocalDateTime uploadedAt) {
        this.id = id;
        this.documentTypeStr = documentTypeStr;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileUrl = fileUrl;
        this.documentUrl = fileUrl;
        this.verificationStatus = verificationStatus;
        this.uploadedAt = uploadedAt;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
        if (documentType != null) {
            this.documentTypeStr = documentType.name();
        }
    }

    public void setDocumentType(String docType) {
        this.documentTypeStr = docType;
        try {
            this.documentType = DocumentType.valueOf(docType);
        } catch (Exception ignored) {
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getDocumentUrl() {
        return documentUrl != null ? documentUrl : fileUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
        if (this.fileUrl == null) {
            this.fileUrl = documentUrl;
        }
    }

    public String getFileUrl() {
        return fileUrl != null ? fileUrl : documentUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
        if (this.documentUrl == null) {
            this.documentUrl = fileUrl;
        }
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public Boolean getVerified() {
        return verified != null ? verified : ("APPROVED".equalsIgnoreCase(verificationStatus) || "VERIFIED".equalsIgnoreCase(verificationStatus));
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
        if (verified != null && verified) {
            this.verificationStatus = "APPROVED";
        }
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt != null ? uploadedAt : verifiedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
