package com.hinchmart.service;

import com.hinchmart.dto.request.PincodeInventoryRequest;
import com.hinchmart.dto.response.PincodeInventoryDto;
import com.hinchmart.entity.Inventory;
import com.hinchmart.entity.PincodeInventory;
import com.hinchmart.entity.Product;
import com.hinchmart.entity.User;
import com.hinchmart.entity.enums.Role;
import com.hinchmart.exception.BadRequestException;
import com.hinchmart.exception.ResourceNotFoundException;
import com.hinchmart.exception.UnauthorizedException;
import com.hinchmart.repository.InventoryRepository;
import com.hinchmart.repository.PincodeInventoryRepository;
import com.hinchmart.repository.ProductRepository;
import com.hinchmart.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    private final PincodeInventoryRepository pincodeInventoryRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final ActivityLogService activityLogService;

    public InventoryService(PincodeInventoryRepository pincodeInventoryRepository,
            ProductRepository productRepository,
            InventoryRepository inventoryRepository,
            UserRepository userRepository,
            ActivityLogService activityLogService) {
        this.pincodeInventoryRepository = pincodeInventoryRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.userRepository = userRepository;
        this.activityLogService = activityLogService;
    }

    /**
     * Add or update inventory for a specific SKU and Pincode, automatically syncing
     * Product and Inventory tables.
     */
    @Transactional
    public PincodeInventoryDto addOrUpdatePincodeInventory(Long sellerUserId, PincodeInventoryRequest request) {
        Product product = findProductByRequest(request);
        User user = userRepository.findById(sellerUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + sellerUserId));

        // Authorization check: User must be ADMIN/SUPER_ADMIN or the product's seller
        if (!user.hasAnyRole(Role.ADMIN, Role.SUPER_ADMIN) && !product.getSeller().getId().equals(sellerUserId)) {
            throw new UnauthorizedException("You do not have permission to manage inventory for this product");
        }

        String pincode = request.getPincode().trim();
        Optional<PincodeInventory> existingOpt = pincodeInventoryRepository.findByProductIdAndPincode(product.getId(),
                pincode);

        PincodeInventory pincodeInventory;
        if (existingOpt.isPresent()) {
            pincodeInventory = existingOpt.get();
            pincodeInventory.setQuantity(request.getQuantity());
            if (request.getWarehouseName() != null)
                pincodeInventory.setWarehouseName(request.getWarehouseName());
            if (request.getCity() != null)
                pincodeInventory.setCity(request.getCity());
            if (request.getState() != null)
                pincodeInventory.setState(request.getState());
            if (request.getDeliveryDays() != null)
                pincodeInventory.setDeliveryDays(request.getDeliveryDays());
            if (request.getMinOrderQuantity() != null)
                pincodeInventory.setMinOrderQuantity(request.getMinOrderQuantity());
            if (request.getIsActive() != null)
                pincodeInventory.setActive(request.getIsActive());
        } else {
            pincodeInventory = new PincodeInventory(
                    product,
                    product.getSeller(),
                    pincode,
                    request.getWarehouseName(),
                    request.getCity(),
                    request.getState(),
                    request.getQuantity(),
                    request.getDeliveryDays());
            if (request.getMinOrderQuantity() != null) {
                pincodeInventory.setMinOrderQuantity(request.getMinOrderQuantity());
            }
            if (request.getIsActive() != null) {
                pincodeInventory.setActive(request.getIsActive());
            }
            product.addPincodeInventory(pincodeInventory);
        }

        PincodeInventory saved = pincodeInventoryRepository.save(pincodeInventory);

        // Recalculate aggregate stock for the product and inventory table
        syncProductTotalStock(product);

        log.info(
                ">>> [INVENTORY TRANSACTION] Saved inventory for SKU: '{}' at Pincode: '{}' -> Stock: {}, Warehouse: '{}', User: '{}'",
                product.getSku(), pincode, request.getQuantity(), pincodeInventory.getWarehouseName(), user.getEmail());

        activityLogService.log(sellerUserId, user.getEmail(), "INVENTORY_UPDATED", "PRODUCT",
                product.getId(), "Updated inventory for SKU: " + product.getSku() + " at pincode: " + pincode
                        + " to qty: " + request.getQuantity(),
                null);

        return mapToPincodeInventoryDto(saved);
    }

    /**
     * Bulk add or update inventory across multiple pincodes.
     */
    @Transactional
    public List<PincodeInventoryDto> bulkAddOrUpdatePincodeInventory(Long sellerUserId,
            List<PincodeInventoryRequest> requests) {
        List<PincodeInventoryDto> results = new ArrayList<>();
        for (PincodeInventoryRequest req : requests) {
            results.add(addOrUpdatePincodeInventory(sellerUserId, req));
        }
        return results;
    }

    /**
     * Get all pincode-wise inventory records for a product by ID.
     */
    @Transactional(readOnly = true)
    public List<PincodeInventoryDto> getPincodeInventoriesByProductId(Long productId) {
        return pincodeInventoryRepository.findByProductId(productId).stream()
                .map(this::mapToPincodeInventoryDto)
                .collect(Collectors.toList());
    }

    /**
     * Get all pincode-wise inventory records for a product by SKU.
     */
    @Transactional(readOnly = true)
    public List<PincodeInventoryDto> getPincodeInventoriesBySku(String sku) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU: " + sku));
        return getPincodeInventoriesByProductId(product.getId());
    }

    /**
     * Comprehensive multi-parameter inventory search by pincode, category,
     * subcategory, brand, and keyword.
     */
    @Transactional(readOnly = true)
    public List<PincodeInventoryDto> searchPincodeInventory(String pincode, Long categoryId, Long subcategoryId,
            Long brandId, String query, Boolean inStockOnly) {
        List<PincodeInventory> list = (pincode != null && !pincode.isBlank())
                ? pincodeInventoryRepository.findByPincode(pincode.trim())
                : pincodeInventoryRepository.findAll();

        return list.stream()
                .filter(PincodeInventory::isActive)
                .filter(pi -> pi.getProduct() != null && pi.getProduct().isActive())
                .filter(pi -> categoryId == null || (pi.getProduct().getCategory() != null
                        && pi.getProduct().getCategory().getId().equals(categoryId)))
                .filter(pi -> subcategoryId == null || (pi.getProduct().getSubcategory() != null
                        && pi.getProduct().getSubcategory().getId().equals(subcategoryId)))
                .filter(pi -> brandId == null
                        || (pi.getProduct().getBrand() != null && pi.getProduct().getBrand().getId().equals(brandId)))
                .filter(pi -> {
                    if (query == null || query.isBlank())
                        return true;
                    String q = query.trim().toLowerCase();
                    return (pi.getProduct().getProductName() != null
                            && pi.getProduct().getProductName().toLowerCase().contains(q))
                            || (pi.getProduct().getSku() != null && pi.getProduct().getSku().toLowerCase().contains(q))
                            || (pi.getCity() != null && pi.getCity().toLowerCase().contains(q))
                            || (pi.getWarehouseName() != null && pi.getWarehouseName().toLowerCase().contains(q));
                })
                .filter(pi -> {
                    if (inStockOnly == null || !inStockOnly)
                        return true;
                    int available = pi.getQuantity()
                            - (pi.getReservedQuantity() != null ? pi.getReservedQuantity() : 0);
                    return available > 0;
                })
                .map(this::mapToPincodeInventoryDto)
                .collect(Collectors.toList());
    }

    /**
     * Get all active SKU inventories available in a specific delivery pincode,
     * optionally filtered by category.
     */
    @Transactional(readOnly = true)
    public List<PincodeInventoryDto> getInventoriesByPincodeAndCategory(String pincode, Long categoryId) {
        return searchPincodeInventory(pincode, categoryId, null, null, null, null);
    }

    /**
     * Check stock availability and delivery timeline for a given SKU/Product and
     * delivery pincode.
     */
    @Transactional(readOnly = true)
    public PincodeInventoryDto checkPincodeAvailability(String skuOrId, String pincode, Integer requestedQuantity) {
        Product product;
        try {
            Long id = Long.parseLong(skuOrId);
            product = productRepository.findById(id)
                    .orElseGet(() -> productRepository.findBySku(skuOrId)
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + skuOrId)));
        } catch (NumberFormatException e) {
            product = productRepository.findBySku(skuOrId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU: " + skuOrId));
        }

        int qtyToCheck = requestedQuantity != null ? requestedQuantity : 1;
        Optional<PincodeInventory> pinInvOpt = pincodeInventoryRepository.findByProductIdAndPincode(product.getId(),
                pincode.trim());

        if (pinInvOpt.isPresent() && pinInvOpt.get().isActive()) {
            PincodeInventory pi = pinInvOpt.get();
            PincodeInventoryDto dto = mapToPincodeInventoryDto(pi);
            dto.setServiceable(pi.getAvailableQuantity() >= qtyToCheck);
            return dto;
        }

        // If no explicit pincode inventory row exists, check if general product
        // inventory serves it
        PincodeInventoryDto fallbackDto = new PincodeInventoryDto();
        fallbackDto.setProductId(product.getId());
        fallbackDto.setProductSku(product.getSku());
        fallbackDto.setProductName(product.getProductName());
        fallbackDto.setPincode(pincode);
        fallbackDto.setQuantity(product.getStock());
        fallbackDto.setAvailableQuantity(product.getStock());
        fallbackDto.setReservedQuantity(0);
        fallbackDto.setDeliveryDays(product.getDeliveryDays() != null ? product.getDeliveryDays() : 3);
        fallbackDto
                .setServiceable(product.getStock() != null && product.getStock() >= qtyToCheck && product.isActive());
        fallbackDto.setActive(product.isActive());
        fallbackDto.setWarehouseName(
                product.getInventory() != null ? product.getInventory().getWarehouseLocation() : "Central Warehouse");
        return fallbackDto;
    }

    /**
     * Delete a pincode inventory entry and recalculate aggregate stock.
     */
    @Transactional
    public void deletePincodeInventory(Long sellerUserId, Long productId, String pincode) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));

        User user = userRepository.findById(sellerUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + sellerUserId));

        if (!user.hasAnyRole(Role.ADMIN, Role.SUPER_ADMIN) && !product.getSeller().getId().equals(sellerUserId)) {
            throw new UnauthorizedException("You do not have permission to delete inventory for this product");
        }

        pincodeInventoryRepository.deleteByProductIdAndPincode(productId, pincode.trim());
        syncProductTotalStock(product);
    }

    /**
     * Recalculates total product stock from all active pincode inventories,
     * updating both Product and Inventory tables.
     */
    @Transactional
    public void syncProductTotalStock(Product product) {
        List<PincodeInventory> inventories = pincodeInventoryRepository.findByProductId(product.getId());
        if (!inventories.isEmpty()) {
            int totalStock = inventories.stream()
                    .filter(PincodeInventory::isActive)
                    .mapToInt(PincodeInventory::getQuantity)
                    .sum();

            int totalReserved = inventories.stream()
                    .filter(PincodeInventory::isActive)
                    .mapToInt(PincodeInventory::getReservedQuantity)
                    .sum();

            product.setStock(totalStock);

            if (product.getInventory() != null) {
                product.getInventory().setQuantity(totalStock);
                product.getInventory().setReservedQuantity(totalReserved);
            } else {
                Inventory inv = new Inventory(product, totalStock, totalReserved, 10, "Multi-location Warehouse");
                product.setInventory(inv);
            }
            productRepository.save(product);
            log.info(">>> [STOCK SYNC] Product '{}' (SKU: {}) total aggregated stock updated to {} (Reserved: {})",
                    product.getProductName(), product.getSku(), totalStock, totalReserved);
        }
    }

    private Product findProductByRequest(PincodeInventoryRequest request) {
        if (request.getProductId() != null) {
            return productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found with ID: " + request.getProductId()));
        } else if (request.getSku() != null && !request.getSku().isBlank()) {
            return productRepository.findBySku(request.getSku().trim())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Product not found with SKU: " + request.getSku()));
        } else {
            throw new BadRequestException("Either productId or sku must be provided");
        }
    }

    public PincodeInventoryDto mapToPincodeInventoryDto(PincodeInventory pi) {
        PincodeInventoryDto dto = new PincodeInventoryDto();
        dto.setId(pi.getId());
        if (pi.getProduct() != null) {
            dto.setProductId(pi.getProduct().getId());
            dto.setProductSku(pi.getProduct().getSku());
            dto.setProductName(pi.getProduct().getProductName());
        }
        if (pi.getSeller() != null) {
            dto.setSellerId(pi.getSeller().getId());
            dto.setSellerName(pi.getSeller().getFullName());
        }
        dto.setPincode(pi.getPincode());
        dto.setWarehouseName(pi.getWarehouseName());
        dto.setCity(pi.getCity());
        dto.setState(pi.getState());
        dto.setQuantity(pi.getQuantity());
        dto.setReservedQuantity(pi.getReservedQuantity());
        dto.setAvailableQuantity(pi.getAvailableQuantity());
        dto.setDeliveryDays(pi.getDeliveryDays());
        dto.setMinOrderQuantity(pi.getMinOrderQuantity());
        dto.setServiceable(pi.isActive() && pi.getAvailableQuantity() > 0);
        dto.setActive(pi.isActive());
        dto.setUpdatedAt(pi.getUpdatedAt());
        return dto;
    }
}
