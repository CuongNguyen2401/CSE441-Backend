package com.thanhtan.groceryshop.service;

import com.thanhtan.groceryshop.dto.request.RatingRequest;
import com.thanhtan.groceryshop.dto.response.RatingResponse;

public interface IRatingService {

    RatingResponse createRating(RatingRequest ratingRequest);

    double getAverageRating(Long productId);


}
