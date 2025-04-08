package com.cuongnguyen.cse441.service;

import com.cuongnguyen.cse441.dto.request.RatingRequest;
import com.cuongnguyen.cse441.dto.response.RatingResponse;

public interface IRatingService {

    RatingResponse createRating(RatingRequest ratingRequest);

    double getAverageRating(Long productId);


}
