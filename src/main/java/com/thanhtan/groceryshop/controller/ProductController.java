package com.thanhtan.groceryshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thanhtan.groceryshop.dto.request.ProductRequest;
import com.thanhtan.groceryshop.dto.request.UpdateProductRequest;
import com.thanhtan.groceryshop.dto.response.ApiResponse;
import com.thanhtan.groceryshop.dto.response.ProductResponse;
import com.thanhtan.groceryshop.dto.response.ProductSalesResponse;
import com.thanhtan.groceryshop.dto.response.SalesCategory;
import com.thanhtan.groceryshop.service.IProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.thanhtan.groceryshop.constant.PathConstant.API_V1_PRODUCTS;


@RestController
@RequestMapping(API_V1_PRODUCTS)
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    IProductService productService;


    @GetMapping
    public ApiResponse<List<ProductResponse>> getProducts() {
        return ApiResponse.success(productService.findAll());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')||hasRole('STAFF')")
    public ApiResponse<ProductResponse> createProduct(
            @RequestPart("product") String productStr,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        ProductRequest productRequest = objectMapper.readValue(productStr, ProductRequest.class);
        return ApiResponse.success(productService.createProduct(productRequest, file));

    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')||hasRole('STAFF')")
    public ApiResponse<ProductResponse> updateProduct(
            @RequestPart("product") String productStr,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        UpdateProductRequest productRequest = objectMapper.readValue(productStr, UpdateProductRequest.class);

        return ApiResponse.success(productService.updateProduct(productRequest, file));
    }

    @GetMapping("/sales")
    public ApiResponse<List<ProductResponse>> getSalesProduct() {
        return ApiResponse.success(productService.findSalesProduct());
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable Long id) {
        return ApiResponse.success(productService.findById(id));
    }

    @GetMapping("/detail/{slug}")
    public ApiResponse<ProductResponse> getProductBySlug(@PathVariable String slug) {
        return ApiResponse.success(productService.findBySlug(slug));
    }

    @GetMapping("/category/{categoryName}")
    public ApiResponse<List<ProductResponse>> getProductsByCategory(
            @PathVariable String categoryName,
            @RequestParam(required = false) Integer limit) {
        if (limit != null) {
            return ApiResponse.success(productService.find5ProductsByCategory(categoryName));
        }
        return ApiResponse.success(productService.findByCategory(categoryName));

    }

    @GetMapping("/list")
    public ApiResponse<List<ProductResponse>> getProductsByIds(@RequestParam List<Long> ids) {
        List<ProductResponse> products = ids.stream()
                .map(productService::findById)
                .toList();
        return ApiResponse.success(products);
    }

    @GetMapping("/search")
    public ApiResponse<List<ProductResponse>> searchProducts(@RequestParam String q) {
        return ApiResponse.success(productService.findProductsByQueryString(q));

    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')||hasRole('STAFF')")
    public ApiResponse<String> deleteProducts(@RequestParam Long[] ids) {
        productService.deleteProductsById(ids);
        return ApiResponse.success(null);
    }


    @GetMapping("/most-sold")
    @PreAuthorize("hasRole('ADMIN')||hasRole('STAFF')")
    public ApiResponse<List<ProductSalesResponse>> getMostSoldProduct() {
        return ApiResponse.success((productService.findMostSoldProduct()));


    }

    @GetMapping("/sales-category")
    @PreAuthorize("hasRole('ADMIN')||hasRole('STAFF')")
    public ApiResponse<List<SalesCategory>> getSalesCategory(
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date endDate) {
        return ApiResponse.success(productService.findSalesCategory(startDate, endDate));
    }


//    @PutMapping("/status")
//    public ApiResponse<String> updateProductStatus(@RequestParam Long[] ids) {
//        productService.updateProductStatus(ids);
//        return ApiResponse.<String>builder()
//                .build();
//    }


}
