package com.hinchmart.repository;

import com.hinchmart.entity.SellerDocument;
import com.hinchmart.entity.enums.ApprovalStatus;
import com.hinchmart.entity.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerDocumentRepository extends JpaRepository<SellerDocument, Long> {
    List<SellerDocument> findBySellerProfileId(Long sellerProfileId);
    List<SellerDocument> findByVerificationStatus(ApprovalStatus status);
    Optional<SellerDocument> findBySellerProfileIdAndDocumentType(Long sellerProfileId, DocumentType documentType);
}
