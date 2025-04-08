package com.cuongnguyen.cse441.util;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.cuongnguyen.cse441.dto.request.CreateSystemUserRequest;
import com.cuongnguyen.cse441.dto.request.UpdateSystemUserRequest;
import com.cuongnguyen.cse441.dto.request.UpdateUserRequest;
import com.cuongnguyen.cse441.exception.AppException;
import com.cuongnguyen.cse441.exception.ErrorCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;


public class ValidationUtil {


    public static UpdateUserRequest validateUserStr(String userStr) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        UpdateUserRequest updateUserRequest = objectMapper.readValue(userStr, UpdateUserRequest.class);
        Set<ConstraintViolation<UpdateUserRequest>> violations = validator.validate(updateUserRequest);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            ErrorCode errorCode = determineErrorCode(errorMessage);
            throw new AppException(errorCode);
        }

        return updateUserRequest;
    }

    public static CreateSystemUserRequest validateSystemUserStr(String userStr) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        CreateSystemUserRequest userRequest = objectMapper.readValue(userStr, CreateSystemUserRequest.class);
        Set<ConstraintViolation<CreateSystemUserRequest>> violations = validator.validate(userRequest);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            ErrorCode errorCode = determineErrorCode(errorMessage);
            throw new AppException(errorCode);
        }

        return userRequest;
    }

    public static UpdateSystemUserRequest validateUpdateSystemUserStr(String userStr) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        UpdateSystemUserRequest userRequest = objectMapper.readValue(userStr, UpdateSystemUserRequest.class);
        Set<ConstraintViolation<UpdateSystemUserRequest>> violations = validator.validate(userRequest);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            ErrorCode errorCode = determineErrorCode(errorMessage);
            throw new AppException(errorCode);
        }

        return userRequest;
    }

    private static ErrorCode determineErrorCode(String errorMessage) {

        if (errorMessage.contains("USERNAME_INVALID")) {
            return ErrorCode.USERNAME_INVALID;
        } else if (errorMessage.contains("INVALID_PASSWORD")) {
            return ErrorCode.INVALID_PASSWORD;
        } else if (errorMessage.contains("INVALID_DOB")) {
            return ErrorCode.INVALID_DOB;
        } else {
            return ErrorCode.INVALID_KEY;
        }
    }
}