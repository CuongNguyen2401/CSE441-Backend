package com.thanhtan.groceryshop.service.impl;

import com.thanhtan.groceryshop.dto.request.RatingRequest;
import com.thanhtan.groceryshop.dto.response.RatingResponse;
import com.thanhtan.groceryshop.entity.Product;
import com.thanhtan.groceryshop.entity.Rating;
import com.thanhtan.groceryshop.exception.AlreadyRatedException;
import com.thanhtan.groceryshop.exception.ErrorCode;
import com.thanhtan.groceryshop.exception.ResourceNotFound;
import com.thanhtan.groceryshop.mapper.RatingMapper;
import com.thanhtan.groceryshop.repository.ProductRepository;
import com.thanhtan.groceryshop.repository.RatingRepository;
import com.thanhtan.groceryshop.service.IRatingService;
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
