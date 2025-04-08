package com.cuongnguyen.cse441.mapper;

import com.cuongnguyen.cse441.dto.request.RatingRequest;
import com.cuongnguyen.cse441.dto.response.RatingResponse;
import com.cuongnguyen.cse441.entity.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    @Mapping(target = "product", source = "productId", ignore = true)
    Rating toRating(RatingRequest request);

    @Mapping(target = "productId", source = "product.id")
    RatingResponse toRatingResponse(Rating rating);


}
