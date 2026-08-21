package com.hinchmart.repository;

import com.hinchmart.entity.User;
import com.hinchmart.entity.enums.AccountStatus;
import com.hinchmart.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    Optional<User> findByEmailOrPhone(String email, String phone);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    List<User> findByRole(Role role);
    List<User> findByStatus(AccountStatus status);
    long countByRole(Role role);

    Page<User> findByRoleOrderByCreatedAtDesc(Role role, Pageable pageable);

    @Query("SELECT u FROM User u LEFT JOIN u.buyerProfile bp WHERE u.role = com.hinchmart.entity.enums.Role.BUYER AND " +
            "(:search IS NULL OR :search = '' OR " +
            "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.phone) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "(bp.companyName IS NOT NULL AND LOWER(bp.companyName) LIKE LOWER(CONCAT('%', :search, '%'))) OR " +
            "(bp.gstin IS NOT NULL AND LOWER(bp.gstin) LIKE LOWER(CONCAT('%', :search, '%'))) OR " +
            "(bp.city IS NOT NULL AND LOWER(bp.city) LIKE LOWER(CONCAT('%', :search, '%')))) " +
            "ORDER BY u.createdAt DESC")
    Page<User> searchBuyers(@Param("search") String search, Pageable pageable);
}
