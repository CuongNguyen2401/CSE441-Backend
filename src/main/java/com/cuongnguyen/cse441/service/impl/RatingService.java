package com.cuongnguyen.cse441.service.impl;

import com.cuongnguyen.cse441.dto.request.RatingRequest;
import com.cuongnguyen.cse441.dto.response.RatingResponse;
import com.cuongnguyen.cse441.entity.Product;
import com.cuongnguyen.cse441.entity.Rating;
import com.cuongnguyen.cse441.exception.ErrorCode;
import com.cuongnguyen.cse441.exception.ResourceNotFound;
import com.cuongnguyen.cse441.mapper.RatingMapper;
import com.cuongnguyen.cse441.repository.ProductRepository;
import com.cuongnguyen.cse441.repository.RatingRepository;
import com.cuongnguyen.cse441.service.IRatingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingService implements IRatingService {

    RatingRepository ratingRepository;

    RatingMapper ratingMapper;

    ProductRepository productRepository;

    @Override
    public RatingResponse createRating(RatingRequest ratingRequest) {
        Rating newRating = ratingMapper.toRating(ratingRequest);

        Product product = productRepository.findById(ratingRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFound(ErrorCode.RESOURCE_NOT_FOUND));
        newRating.setProduct(product);

        Optional<Rating> existingRating =
                ratingRepository.findByProductIdAndUserId(product.getId(), newRating.getUserId());

        if (existingRating.isPresent()) {
            ratingRepository.updateRate(newRating.getRate(), existingRating.get().getId());
        } else {
            newRating = ratingRepository.save(newRating);
        }

        RatingResponse ratingResponse = ratingMapper.toRatingResponse(newRating);
        ratingResponse.setProductId(newRating.getProduct().getId());

        return ratingResponse;
    }


    @Override
    public double getAverageRating(Long productId) {
        return ratingRepository.getAverageRating(productId);
    }
}
