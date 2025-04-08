package com.cuongnguyen.cse441.controller;

import com.cuongnguyen.cse441.dto.request.CategoryRequest;
import com.cuongnguyen.cse441.dto.request.UpdateCategoryRequest;
import com.cuongnguyen.cse441.dto.response.ApiResponse;
import com.cuongnguyen.cse441.dto.response.CategoryResponse;
import com.cuongnguyen.cse441.service.ICategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cuongnguyen.cse441.constant.PathConstant.API_V1_CATEGORY;


@RestController
@RequestMapping(API_V1_CATEGORY)
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    ICategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryRequest category) {
        return ApiResponse.success(categoryService.createCategory(category));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CategoryResponse> updateCategory(@RequestBody UpdateCategoryRequest category) {
        return ApiResponse.success(categoryService.updateCategory(category));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<CategoryResponse> deleteCategory(@RequestParam Long[] ids) {
        categoryService.deleteCategory(ids);
        return ApiResponse.success(null);
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getCategories() {
        return ApiResponse.success(categoryService.findAll());
    }


    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return ApiResponse.success(categoryService.findById(id));
    }


    @GetMapping("/{categoryName}/products/count")
    public ApiResponse<Long> countProductByCategory(@PathVariable String categoryName) {
        return ApiResponse.success(categoryService.countProductByCategory(categoryName));
    }
}
