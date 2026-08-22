package com.hinchmart.service;

import com.hinchmart.dto.request.BulkPriceTierDto;
import com.hinchmart.dto.request.PincodeInventoryRequest;
import com.hinchmart.dto.request.ProductCreateRequest;
import com.hinchmart.dto.request.ProductUpdateRequest;
import com.hinchmart.dto.response.BrandDto;
import com.hinchmart.dto.response.PincodeInventoryDto;
import com.hinchmart.dto.response.ProductBulkPriceDto;
import com.hinchmart.dto.response.ProductDto;
import com.hinchmart.dto.response.ProductImageDto;
import com.hinchmart.entity.*;
import com.hinchmart.entity.enums.ApprovalStatus;
import com.hinchmart.entity.enums.Role;
import com.hinchmart.exception.BadRequestException;
import com.hinchmart.exception.ResourceNotFoundException;
import com.hinchmart.exception.UnauthorizedException;
import com.hinchmart.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final BrandRepository brandRepository;
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;
    private final ActivityLogService activityLogService;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          SubcategoryRepository subcategoryRepository,
                          BrandRepository brandRepository,
                          UserRepository userRepository,
                          InventoryRepository inventoryRepository,
                          ActivityLogService activityLogService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.brandRepository = brandRepository;
        this.userRepository = userRepository;
        this.inventoryRepository = inventoryRepository;
        this.activityLogService = activityLogService;
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> getProducts(String query, Long categoryId, Long subcategoryId, Long brandId,
                                        Long sellerId, BigDecimal minPrice, BigDecimal maxPrice,
                                        Boolean inStock, Pageable pageable) {
        Page<Product> products = productRepository.searchProductsFiltered(
                query, categoryId, subcategoryId, brandId, sellerId, minPrice, maxPrice, inStock,
                ApprovalStatus.APPROVED, true, pageable
        );
        List<ProductDto> dtos = products.getContent().stream()
                .map(this::mapToProductDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, products.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> getProductsForAdmin(String query, Long categoryId, Long subcategoryId,
                                                Long brandId, Long sellerId, ApprovalStatus status,
                                                Boolean isActive, Pageable pageable) {
        Page<Product> products = productRepository.searchProductsFiltered(
                query, categoryId, subcategoryId, brandId, sellerId, null, null, null,
                status, isActive, pageable
        );
        List<ProductDto> dtos = products.getContent().stream()
                .map(this::mapToProductDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, products.getTotalElements());
    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        return mapToProductDto(product);
    }

    @Transactional(readOnly = true)
    public ProductDto getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with slug: " + slug));
        return mapToProductDto(product);
    }

    @Transactional
    public ProductDto createProduct(Long sellerUserId, ProductCreateRequest request) {
        User seller = userRepository.findById(sellerUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller user not found: " + sellerUserId));

        if (productRepository.existsBySku(request.getSku())) {
            throw new BadRequestException("SKU already exists: " + request.getSku());
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + request.getCategoryId()));

        Subcategory subcategory = null;
        if (request.getSubcategoryId() != null) {
            subcategory = subcategoryRepository.findById(request.getSubcategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Subcategory not found: " + request.getSubcategoryId()));
        }

        Brand brand = null;
        if (request.getBrandId() != null) {
            brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand not found: " + request.getBrandId()));
        }

        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setSlug(generateUniqueSlug(request.getProductName()));
        product.setSeller(seller);
        product.setCategory(category);
        product.setSubcategory(subcategory);
        product.setBrand(brand);
        product.setSku(request.getSku());
        product.setHsnCode(request.getHsnCode());
        product.setGstRate(request.getGstRate());
        product.setMoq(request.getMoq());
        product.setUnit(request.getUnit());
        product.setMrp(request.getMrp());
        product.setSellingPrice(request.getSellingPrice());
        product.setStock(request.getStock());
        product.setDeliveryDays(request.getDeliveryDays() != null ? request.getDeliveryDays() : 3);
        product.setApprovalStatus(seller.hasAnyRole(Role.ADMIN, Role.SUPER_ADMIN) ?
                ApprovalStatus.APPROVED : ApprovalStatus.PENDING);
        product.setDescription(request.getDescription());
        product.setSpecifications(request.getSpecifications());
        product.setActive(true);

        // Add Images
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            for (int i = 0; i < request.getImageUrls().size(); i++) {
                ProductImage img = new ProductImage(product, request.getImageUrls().get(i), i == 0, i);
                product.addImage(img);
            }
        }

        // Add Bulk Prices
        if (request.getBulkPrices() != null && !request.getBulkPrices().isEmpty()) {
            for (BulkPriceTierDto tier : request.getBulkPrices()) {
                ProductBulkPrice bp = new ProductBulkPrice(
                        product,
                        tier.getMinQuantity(),
                        tier.getMaxQuantity(),
                        tier.getPricePerUnit(),
                        tier.getDiscountPercentage()
                );
                product.addBulkPrice(bp);
            }
        }

        // Initial Pincode-Wise Inventory & Central Inventory
        String defaultWarehouse = "Central Warehouse";
        String defaultPincode = "411057";
        String defaultCity = "Pune";
        String defaultState = "Maharashtra";

        if (seller.getSellerProfile() != null) {
            if (seller.getSellerProfile().getPincode() != null && !seller.getSellerProfile().getPincode().isBlank()) {
                defaultPincode = seller.getSellerProfile().getPincode().trim();
            }
            if (seller.getSellerProfile().getWarehouseAddress() != null && !seller.getSellerProfile().getWarehouseAddress().isBlank()) {
                defaultWarehouse = seller.getSellerProfile().getWarehouseAddress();
            }
            if (seller.getSellerProfile().getCity() != null) {
                defaultCity = seller.getSellerProfile().getCity();
            }
            if (seller.getSellerProfile().getState() != null) {
                defaultState = seller.getSellerProfile().getState();
            }
        }

        int totalStock = request.getStock() != null ? request.getStock() : 0;

        if (request.getPincodeInventories() != null && !request.getPincodeInventories().isEmpty()) {
            int calculatedStock = 0;
            for (PincodeInventoryRequest pinReq : request.getPincodeInventories()) {
                PincodeInventory pi = new PincodeInventory(
                        product,
                        seller,
                        pinReq.getPincode().trim(),
                        pinReq.getWarehouseName() != null ? pinReq.getWarehouseName() : defaultWarehouse,
                        pinReq.getCity() != null ? pinReq.getCity() : defaultCity,
                        pinReq.getState() != null ? pinReq.getState() : defaultState,
                        pinReq.getQuantity(),
                        pinReq.getDeliveryDays() != null ? pinReq.getDeliveryDays() : (request.getDeliveryDays() != null ? request.getDeliveryDays() : 3)
                );
                if (pinReq.getMinOrderQuantity() != null) {
                    pi.setMinOrderQuantity(pinReq.getMinOrderQuantity());
                }
                product.addPincodeInventory(pi);
                calculatedStock += (pinReq.getQuantity() != null ? pinReq.getQuantity() : 0);
            }
            if (calculatedStock > 0) {
                totalStock = calculatedStock;
                product.setStock(totalStock);
            }
        } else {
            // Auto-populate default seller warehouse / pincode inventory
            PincodeInventory pi = new PincodeInventory(
                    product,
                    seller,
                    defaultPincode,
                    defaultWarehouse,
                    defaultCity,
                    defaultState,
                    totalStock,
                    request.getDeliveryDays() != null ? request.getDeliveryDays() : 3
            );
            product.addPincodeInventory(pi);
        }

        Inventory inventory = new Inventory(product, totalStock, 0, 10, defaultWarehouse);
        product.setInventory(inventory);

        Product saved = productRepository.save(product);
        log.info(">>> [SKU CREATED] SKU: '{}', Product: '{}' (ID: {}), Category: '{}', Total Stock: {}, Status: {}",
                saved.getSku(), saved.getProductName(), saved.getId(),
                saved.getCategory() != null ? saved.getCategory().getName() : "N/A",
                saved.getStock(), saved.getApprovalStatus());

        activityLogService.log(sellerUserId, seller.getEmail(), "PRODUCT_CREATED", "PRODUCT",
                saved.getId(), "Created product: " + saved.getProductName(), null);

        return mapToProductDto(saved);
    }

    @Transactional
    public ProductDto updateProduct(Long productId, Long sellerUserId, ProductUpdateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));

        User currentUser = userRepository.findById(sellerUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + sellerUserId));

        // Check ownership if not admin
        if (!currentUser.hasAnyRole(Role.ADMIN, Role.SUPER_ADMIN) &&
                !product.getSeller().getId().equals(sellerUserId)) {
            throw new UnauthorizedException("You do not have permission to update this product");
        }

        if (request.getProductName() != null) product.setProductName(request.getProductName());
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + request.getCategoryId()));
            product.setCategory(category);
        }
        if (request.getSubcategoryId() != null) {
            Subcategory subcategory = subcategoryRepository.findById(request.getSubcategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Subcategory not found: " + request.getSubcategoryId()));
            product.setSubcategory(subcategory);
        }
        if (request.getBrandId() != null) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand not found: " + request.getBrandId()));
            product.setBrand(brand);
        }
        if (request.getSku() != null) product.setSku(request.getSku());
        if (request.getHsnCode() != null) product.setHsnCode(request.getHsnCode());
        if (request.getGstRate() != null) product.setGstRate(request.getGstRate());
        if (request.getMoq() != null) product.setMoq(request.getMoq());
        if (request.getUnit() != null) product.setUnit(request.getUnit());
        if (request.getMrp() != null) product.setMrp(request.getMrp());
        if (request.getSellingPrice() != null) product.setSellingPrice(request.getSellingPrice());
        if (request.getStock() != null) {
            product.setStock(request.getStock());
            if (product.getInventory() != null) {
                product.getInventory().setQuantity(request.getStock());
            }
        }
        if (request.getDeliveryDays() != null) product.setDeliveryDays(request.getDeliveryDays());
        if (request.getApprovalStatus() != null && currentUser.hasAnyRole(Role.ADMIN, Role.SUPER_ADMIN)) {
            product.setApprovalStatus(request.getApprovalStatus());
        }
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getSpecifications() != null) product.setSpecifications(request.getSpecifications());
        if (request.getIsActive() != null) product.setActive(request.getIsActive());

        // Update bulk prices if provided
        if (request.getBulkPrices() != null) {
            product.getBulkPrices().clear();
            for (BulkPriceTierDto tier : request.getBulkPrices()) {
                ProductBulkPrice bp = new ProductBulkPrice(
                        product,
                        tier.getMinQuantity(),
                        tier.getMaxQuantity(),
                        tier.getPricePerUnit(),
                        tier.getDiscountPercentage()
                );
                product.addBulkPrice(bp);
            }
        }

        Product saved = productRepository.save(product);
        log.info(">>> [SKU UPDATED] SKU: '{}', Product: '{}' (ID: {}), Price: {}, Stock: {}",
                saved.getSku(), saved.getProductName(), saved.getId(), saved.getSellingPrice(), saved.getStock());

        activityLogService.log(sellerUserId, currentUser.getEmail(), "PRODUCT_UPDATED", "PRODUCT",
                saved.getId(), "Updated product: " + saved.getProductName(), null);

        return mapToProductDto(saved);
    }

    @Transactional
    public ProductDto approveProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        product.setApprovalStatus(ApprovalStatus.APPROVED);
        Product saved = productRepository.save(product);
        log.info(">>> [SKU APPROVED] Product '{}' (SKU: {}) approved and now live in catalog", saved.getProductName(), saved.getSku());
        return mapToProductDto(saved);
    }

    @Transactional
    public ProductDto rejectProduct(Long productId, String rejectionReason) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        product.setApprovalStatus(ApprovalStatus.REJECTED);
        Product saved = productRepository.save(product);
        log.info(">>> [SKU REJECTED] Product '{}' (SKU: {}) rejected. Reason: {}", saved.getProductName(), saved.getSku(), rejectionReason);
        return mapToProductDto(saved);
    }

    @Transactional
    public ProductDto toggleProductActive(Long productId, boolean active) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        product.setActive(active);
        Product saved = productRepository.save(product);
        return mapToProductDto(saved);
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> getSellerProducts(Long sellerUserId, ApprovalStatus status, Boolean isActive,
                                              String query, Pageable pageable) {
        Page<Product> products = productRepository.searchProductsFiltered(
                query, null, null, null, sellerUserId, null, null, null,
                status, isActive, pageable
        );
        List<ProductDto> dtos = products.getContent().stream()
                .map(this::mapToProductDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, products.getTotalElements());
    }

    @Transactional(readOnly = true)
    public ProductDto getSellerProductById(Long productId, Long sellerUserId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        if (!product.getSeller().getId().equals(sellerUserId)) {
            throw new UnauthorizedException("You do not have permission to view this product");
        }
        return mapToProductDto(product);
    }

    @Transactional
    public void deleteProduct(Long productId, Long sellerUserId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        User user = userRepository.findById(sellerUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + sellerUserId));

        if (user.getRole() != Role.ADMIN && user.getRole() != Role.SUPER_ADMIN &&
                !product.getSeller().getId().equals(sellerUserId)) {
            throw new UnauthorizedException("You do not have permission to delete this product");
        }

        productRepository.delete(product);
        activityLogService.log(sellerUserId, user.getEmail(), "PRODUCT_DELETED", "PRODUCT",
                productId, "Deleted product ID: " + productId, null);
    }

    @Transactional
    public ProductDto toggleSellerProductStatus(Long productId, Long sellerUserId, boolean active) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        if (!product.getSeller().getId().equals(sellerUserId)) {
            throw new UnauthorizedException("You do not have permission to modify this product");
        }

        product.setActive(active);
        Product saved = productRepository.save(product);
        return mapToProductDto(saved);
    }

    @Transactional(readOnly = true)
    public List<BrandDto> getAllBrands() {
        return brandRepository.findByIsActiveTrueOrderByNameAsc().stream()
                .map(this::mapToBrandDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Brand createBrand(Brand brand) {
        if (brandRepository.existsByName(brand.getName())) {
            throw new BadRequestException("Brand already exists: " + brand.getName());
        }
        if (brand.getSlug() == null || brand.getSlug().trim().isEmpty()) {
            brand.setSlug(brand.getName().toLowerCase().replaceAll("[^a-z0-9\\s-]", "").replaceAll("\\s+", "-"));
        }
        return brandRepository.save(brand);
    }

    public ProductDto mapToProductDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setProductName(product.getProductName());
        dto.setSlug(product.getSlug());
        dto.setSku(product.getSku());
        dto.setHsnCode(product.getHsnCode());
        dto.setGstRate(product.getGstRate());
        dto.setMoq(product.getMoq());
        dto.setUnit(product.getUnit());
        dto.setMrp(product.getMrp());
        dto.setSellingPrice(product.getSellingPrice());
        dto.setStock(product.getStock());
        dto.setDeliveryDays(product.getDeliveryDays());
        dto.setApprovalStatus(product.getApprovalStatus());
        dto.setDescription(product.getDescription());
        dto.setSpecifications(product.getSpecifications());
        dto.setActive(product.isActive());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());

        if (product.getSeller() != null) {
            dto.setSellerId(product.getSeller().getId());
            dto.setSellerName(product.getSeller().getFullName());
            if (product.getSeller().getSellerProfile() != null) {
                dto.setSellerCompanyName(product.getSeller().getSellerProfile().getCompanyName());
            }
        }

        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }

        if (product.getSubcategory() != null) {
            dto.setSubcategoryId(product.getSubcategory().getId());
            dto.setSubcategoryName(product.getSubcategory().getName());
        }

        if (product.getBrand() != null) {
            dto.setBrandId(product.getBrand().getId());
            dto.setBrandName(product.getBrand().getName());
        }

        if (product.getProductImages() != null && !product.getProductImages().isEmpty()) {
            List<ProductImageDto> imgDtos = new ArrayList<>();
            for (ProductImage img : product.getProductImages()) {
                if (img.isPrimary() && dto.getPrimaryImageUrl() == null) {
                    dto.setPrimaryImageUrl(img.getImageUrl());
                }
                imgDtos.add(new ProductImageDto(img.getId(), img.getImageUrl(), img.isPrimary(), img.getDisplayOrder()));
            }
            if (dto.getPrimaryImageUrl() == null && !imgDtos.isEmpty()) {
                dto.setPrimaryImageUrl(imgDtos.get(0).getImageUrl());
            }
            dto.setImages(imgDtos);
        }

        if (product.getBulkPrices() != null && !product.getBulkPrices().isEmpty()) {
            String unitDisplayName = product.getUnit() != null ? product.getUnit().getDisplayName() : "Unit";
            List<ProductBulkPriceDto> bulkDtos = product.getBulkPrices().stream()
                    .map(bp -> new ProductBulkPriceDto(
                            bp.getId(),
                            bp.getMinQuantity(),
                            bp.getMaxQuantity(),
                            bp.getPricePerUnit(),
                            bp.getDiscountPercentage(),
                            unitDisplayName
                    ))
                    .collect(Collectors.toList());
            dto.setBulkPrices(bulkDtos);
        }

        if (product.getPincodeInventories() != null && !product.getPincodeInventories().isEmpty()) {
            List<PincodeInventoryDto> pinDtos = product.getPincodeInventories().stream()
                    .map(pi -> {
                        PincodeInventoryDto pinDto = new PincodeInventoryDto();
                        pinDto.setId(pi.getId());
                        pinDto.setProductId(product.getId());
                        pinDto.setProductSku(product.getSku());
                        pinDto.setProductName(product.getProductName());
                        if (product.getSeller() != null) {
                            pinDto.setSellerId(product.getSeller().getId());
                            pinDto.setSellerName(product.getSeller().getFullName());
                        }
                        pinDto.setPincode(pi.getPincode());
                        pinDto.setWarehouseName(pi.getWarehouseName());
                        pinDto.setCity(pi.getCity());
                        pinDto.setState(pi.getState());
                        pinDto.setQuantity(pi.getQuantity());
                        pinDto.setReservedQuantity(pi.getReservedQuantity());
                        pinDto.setAvailableQuantity(pi.getAvailableQuantity());
                        pinDto.setDeliveryDays(pi.getDeliveryDays());
                        pinDto.setMinOrderQuantity(pi.getMinOrderQuantity());
                        pinDto.setServiceable(pi.isActive() && pi.getAvailableQuantity() > 0);
                        pinDto.setActive(pi.isActive());
                        pinDto.setUpdatedAt(pi.getUpdatedAt());
                        return pinDto;
                    })
                    .collect(Collectors.toList());
            dto.setPincodeInventories(pinDtos);
        }

        return dto;
    }

    public BrandDto mapToBrandDto(Brand brand) {
        BrandDto dto = new BrandDto();
        dto.setId(brand.getId());
        dto.setName(brand.getName());
        dto.setSlug(brand.getSlug());
        dto.setLogoUrl(brand.getLogoUrl());
        dto.setDescription(brand.getDescription());
        dto.setActive(brand.isActive());
        return dto;
    }

    private String generateUniqueSlug(String name) {
        String baseSlug = name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-");
        String slug = baseSlug;
        int count = 1;
        while (productRepository.findBySlug(slug).isPresent()) {
            slug = baseSlug + "-" + count++;
        }
        return slug;
    }
}
