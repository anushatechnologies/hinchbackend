package com.hinchmart.controller;

import com.hinchmart.dto.response.ApiResponse;
import com.hinchmart.dto.response.ProductDto;
import com.hinchmart.dto.response.SearchSuggestionsDto;
import com.hinchmart.entity.Category;
import com.hinchmart.entity.Product;
import com.hinchmart.entity.enums.ApprovalStatus;
import com.hinchmart.repository.CategoryRepository;
import com.hinchmart.repository.ProductRepository;
import com.hinchmart.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
@Tag(name = "Live Search & Auto-Complete (Flow 3)", description = "Endpoints for Buyer Live Search Engine, Instant Auto-Complete Suggestions, and Category Matching")
public class SearchController {

    private final ProductService productService;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public SearchController(ProductService productService,
                            ProductRepository productRepository,
                            CategoryRepository categoryRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/suggestions")
    @Operation(summary = "Live Search Suggestions & Auto-Complete", description = "Returns instant search suggestions and matching categories for query string.")
    public ResponseEntity<ApiResponse<SearchSuggestionsDto>> getSuggestions(@RequestParam(name = "q", defaultValue = "") String q) {
        String cleanQuery = q != null ? q.trim() : "";
        if (cleanQuery.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success(new SearchSuggestionsDto(Collections.emptyList(), Collections.emptyList())));
        }

        // 1. Fetch matching product titles for suggestions
        Pageable topSuggestions = PageRequest.of(0, 8);
        Page<Product> matchingProducts = productRepository.searchProductsFiltered(
                cleanQuery, null, null, null, null, null, null, null,
                ApprovalStatus.APPROVED, true, topSuggestions
        );

        List<String> suggestions = matchingProducts.getContent().stream()
                .map(Product::getProductName)
                .distinct()
                .limit(6)
                .collect(Collectors.toList());

        // 2. Fetch matching categories
        List<Map<String, Object>> matchingCategories = categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc().stream()
                .filter(cat -> cat.getName().toLowerCase().contains(cleanQuery.toLowerCase()) ||
                               (cat.getDescription() != null && cat.getDescription().toLowerCase().contains(cleanQuery.toLowerCase())))
                .limit(4)
                .map(cat -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("id", "cat_" + cat.getId());
                    map.put("categoryId", cat.getId());
                    map.put("name", cat.getName());
                    map.put("slug", cat.getSlug());
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(new SearchSuggestionsDto(suggestions, matchingCategories)));
    }

    @GetMapping("/products")
    @Operation(summary = "Live Product Search", description = "Searches verified material catalog by query with optional price and category filters.")
    public ResponseEntity<ApiResponse<Page<ProductDto>>> searchProducts(
            @RequestParam(name = "q", defaultValue = "") String q,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "featured") String sortBy) {

        Sort sort = switch (sortBy.toLowerCase()) {
            case "price_asc" -> Sort.by(Sort.Direction.ASC, "basePrice");
            case "price_desc" -> Sort.by(Sort.Direction.DESC, "basePrice");
            case "rating" -> Sort.by(Sort.Direction.DESC, "rating");
            case "newest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            default -> Sort.by(Sort.Direction.DESC, "isFeatured").and(Sort.by(Sort.Direction.DESC, "createdAt"));
        };

        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<ProductDto> results = productService.getProducts(q, categoryId, null, brandId, null, minPrice, maxPrice, inStock, pageable);
        return ResponseEntity.ok(ApiResponse.success(results));
    }
}
