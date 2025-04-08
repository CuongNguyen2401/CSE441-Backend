package com.cuongnguyen.cse441.mapper;

import com.cuongnguyen.cse441.dto.request.CategoryRequest;
import com.cuongnguyen.cse441.dto.response.CategoryResponse;
import com.cuongnguyen.cse441.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategory(CategoryRequest request);

    CategoryResponse toCategoryResponse(Category category);
}
