package com.example.onlinecoursesplatform.dataTrasferObject;

import jakarta.validation.constraints.NotBlank;

public class CourseCategoryDTO {
    private Long categoryId;
    @NotBlank(message = "Category name cannot be blank")
    private String categoryName;

    public CourseCategoryDTO() {

    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}