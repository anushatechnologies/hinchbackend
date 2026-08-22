package com.hinchmart.repository;

import com.hinchmart.entity.PincodeInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PincodeInventoryRepository extends JpaRepository<PincodeInventory, Long> {

    Optional<PincodeInventory> findByProductIdAndPincode(Long productId, String pincode);

    Optional<PincodeInventory> findByProductSkuAndPincode(String sku, String pincode);

    List<PincodeInventory> findByProductId(Long productId);

    List<PincodeInventory> findByProductSku(String sku);

    List<PincodeInventory> findBySellerId(Long sellerId);

    List<PincodeInventory> findByPincode(String pincode);

    boolean existsByProductIdAndPincode(Long productId, String pincode);

    boolean existsByProductSkuAndPincode(String sku, String pincode);

    void deleteByProductIdAndPincode(Long productId, String pincode);
}
