package com.hinchmart.controller;

import com.hinchmart.dto.response.ApiResponse;
import com.hinchmart.dto.response.VendorDto;
import com.hinchmart.entity.Brand;
import com.hinchmart.exception.ResourceNotFoundException;
import com.hinchmart.repository.BrandRepository;
import com.hinchmart.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vendors")
@Tag(name = "Verified Manufacturers & Brands (Flow 9)", description = "Endpoints for Certified Material Manufacturers (Tata Steel, UltraTech, JSW) and Direct Vendor Hubs")
public class VendorController {

    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    public VendorController(BrandRepository brandRepository, ProductRepository productRepository) {
        this.brandRepository = brandRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    @Operation(summary = "List Verified Manufacturers & Brands", description = "Returns certified enterprise manufacturers with product counts, verified badges, and categories.")
    public ResponseEntity<ApiResponse<List<VendorDto>>> getVendors() {
        List<Brand> brands = brandRepository.findByIsActiveTrue();
        List<VendorDto> vendors = brands.stream().map(this::mapToVendorDto).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(vendors));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Manufacturer Details", description = "Returns brand profile and manufacturer hub details.")
    public ResponseEntity<ApiResponse<VendorDto>> getVendorById(@PathVariable String id) {
        Brand brand = findBrandByIdentifier(id);
        return ResponseEntity.ok(ApiResponse.success(mapToVendorDto(brand)));
    }

    private Brand findBrandByIdentifier(String identifier) {
        if (identifier.startsWith("vnd_") || identifier.startsWith("brand_")) {
            String slug = identifier.replace("vnd_", "").replace("brand_", "");
            return brandRepository.findBySlug(slug)
                    .orElseGet(() -> {
                        try {
                            Long num = Long.parseLong(slug);
                            return brandRepository.findById(num)
                                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found: " + identifier));
                        } catch (NumberFormatException e) {
                            throw new ResourceNotFoundException("Vendor not found: " + identifier);
                        }
                    });
        }
        try {
            Long num = Long.parseLong(identifier);
            return brandRepository.findById(num)
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found with ID: " + identifier));
        } catch (NumberFormatException e) {
            return brandRepository.findBySlug(identifier)
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found: " + identifier));
        }
    }

    private VendorDto mapToVendorDto(Brand brand) {
        VendorDto dto = new VendorDto();
        dto.setId("vnd_" + (brand.getSlug() != null ? brand.getSlug() : brand.getId()));
        dto.setBrandId(brand.getId());
        dto.setName(brand.getName());
        dto.setLogo(brand.getLogoUrl() != null ? brand.getLogoUrl() : "https://images.unsplash.com/photo-1504307651254-35680f356dfd");
        dto.setDescription(brand.getDescription());
        dto.setVerified(true);
        dto.setRating(4.9);

        // Derive city & categories based on brand name
        String nameLower = brand.getName().toLowerCase();
        if (nameLower.contains("tata")) {
            dto.setCity("Jamshedpur / Mumbai");
            dto.setCategories(Arrays.asList("Structural Steel", "TMT Rebars", "Wire Mesh"));
            dto.setProductCount(120);
        } else if (nameLower.contains("ultratech")) {
            dto.setCity("Mumbai / Rajasthan");
            dto.setCategories(Arrays.asList("OPC 53 Cement", "PPC Cement", "Ready-Mix Concrete"));
            dto.setProductCount(85);
        } else if (nameLower.contains("jsw")) {
            dto.setCity("Vijayanagar / Mumbai");
            dto.setCategories(Arrays.asList("TMT Steel", "Coated Sheets", "Color Coated Coils"));
            dto.setProductCount(95);
        } else if (nameLower.contains("astral")) {
            dto.setCity("Ahmedabad / Gujarat");
            dto.setCategories(Arrays.asList("CPVC Pipes", "Drainage Fittings", "Plumbing"));
            dto.setProductCount(140);
        } else {
            dto.setCity("Pan-India Direct Dispatch");
            dto.setCategories(Arrays.asList("Construction Materials", "Industrial Supplies"));
            dto.setProductCount(30);
        }

        return dto;
    }
}
