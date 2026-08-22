package com.hinchmart.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VendorDto {

    private String id;
    private Long brandId;
    private String name;
    private boolean verified = true;
    private Double rating = 4.8;
    private Integer productCount = 0;
    private String logo;
    private String city = "National / Direct Dispatch";
    private List<String> categories = new ArrayList<>();
    private String description;

    public VendorDto() {
    }

    public VendorDto(String id, String name, String logo, String city, List<String> categories, Integer productCount) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.city = city;
        this.categories = categories;
        this.productCount = productCount;
        this.verified = true;
        this.rating = 4.8;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
