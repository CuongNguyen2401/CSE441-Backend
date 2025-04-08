package com.cuongnguyen.cse441.service.impl;

import com.cuongnguyen.cse441.dto.request.CategoryRequest;
import com.cuongnguyen.cse441.dto.request.UpdateCategoryRequest;
import com.cuongnguyen.cse441.dto.response.CategoryResponse;
import com.cuongnguyen.cse441.entity.Category;
import com.cuongnguyen.cse441.entity.Product;
import com.cuongnguyen.cse441.enums.ProductStatus;
import com.cuongnguyen.cse441.enums.Status;
import com.cuongnguyen.cse441.exception.ErrorCode;
import com.cuongnguyen.cse441.exception.ResourceNotFound;
import com.cuongnguyen.cse441.mapper.CategoryMapper;
import com.cuongnguyen.cse441.repository.CategoryRepository;
import com.cuongnguyen.cse441.repository.ProductRepository;
import com.cuongnguyen.cse441.service.ICategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService implements ICategoryService {
    CategoryRepository categoryRepository;

    CategoryMapper categoryMapper;

    ProductRepository productRepository;

    @Override
    public List<CategoryResponse> findAll() {
        List<Category> listCategory = categoryRepository.findAll();

        return listCategory.stream().map(categoryMapper::toCategoryResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(Long[] ids) {
        for (Long id : ids) {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFound(ErrorCode.RESOURCE_NOT_FOUND));
            category.setStatus(Status.INACTIVE);
            categoryRepository.save(category);
            setProductsInactive(category);
        }
    }

    @Override
    public CategoryResponse updateCategory(UpdateCategoryRequest category) {
        Category categoryEntity = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new ResourceNotFound(ErrorCode.RESOURCE_NOT_FOUND));

        categoryEntity.setName(category.getName());
        categoryEntity.setDescription(category.getDescription());
        categoryEntity.setStatus(category.getStatus());

        Category savedCategory = categoryRepository.save(categoryEntity);
        if (category.getStatus().equals(Status.INACTIVE)) {
            setProductsInactive(savedCategory);
        }
        return categoryMapper.toCategoryResponse(savedCategory);
    }

    private void setProductsInactive(Category category) {
        List<Product> productsByCategory = productRepository.findByCategory(category);
        productsByCategory.forEach(product -> {
            product.setProductStatus(ProductStatus.INACTIVE);
        });
        productRepository.saveAll(productsByCategory);
    }

    @Override
    public long countProductByCategory(String categoryName) {
        if (categoryName.equals("All Products")) {
            return productRepository.count();
        }

        return categoryRepository.countProductByCategory(categoryName);
    }

    @Override
    public CategoryResponse findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound(ErrorCode.RESOURCE_NOT_FOUND));
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest category) {

        Category savedCategory = categoryRepository.save(categoryMapper.toCategory(category));


        return categoryMapper.toCategoryResponse(savedCategory);
    }
}
