package com.cuongnguyen.cse441.service;

import com.cuongnguyen.cse441.dto.request.CategoryRequest;
import com.cuongnguyen.cse441.dto.request.UpdateCategoryRequest;
import com.cuongnguyen.cse441.dto.response.CategoryResponse;

import java.util.List;

public interface ICategoryService {
    CategoryResponse createCategory(CategoryRequest category);

   List<CategoryResponse>  findAll();

   long countProductByCategory(String categoryName);

    CategoryResponse findById(Long id);

    CategoryResponse updateCategory(UpdateCategoryRequest category);

    void deleteCategory(Long [] ids);
}
