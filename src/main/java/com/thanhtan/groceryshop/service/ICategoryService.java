package com.thanhtan.groceryshop.service;

import com.thanhtan.groceryshop.dto.request.CategoryRequest;
import com.thanhtan.groceryshop.dto.request.UpdateCategoryRequest;
import com.thanhtan.groceryshop.dto.response.CategoryResponse;

import java.util.List;

public interface ICategoryService {
    CategoryResponse createCategory(CategoryRequest category);

   List<CategoryResponse>  findAll();

   long countProductByCategory(String categoryName);

    CategoryResponse findById(Long id);

    CategoryResponse updateCategory(UpdateCategoryRequest category);

    void deleteCategory(Long [] ids);
}
