package com.cuongnguyen.cse441.controller;

import com.cuongnguyen.cse441.dto.request.CreateSystemUserRequest;
import com.cuongnguyen.cse441.dto.request.UpdateSystemUserRequest;
import com.cuongnguyen.cse441.dto.request.UpdateUserRequest;
import com.cuongnguyen.cse441.dto.request.UserRequest;
import com.cuongnguyen.cse441.dto.response.ApiResponse;
import com.cuongnguyen.cse441.dto.response.UserResponse;
import com.cuongnguyen.cse441.service.IUserService;
import com.cuongnguyen.cse441.util.ValidationUtil;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.cuongnguyen.cse441.constant.PathConstant.API_V1_USERS;


@RestController
@RequestMapping(API_V1_USERS)
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    IUserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserRequest request) {
        return ApiResponse.success(userService.createUser(request));

    }

    @GetMapping("/myInfo")
    @PreAuthorize("hasRole('USER')||hasRole('ADMIN')||hasRole('STAFF')")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.success(userService.getMyInfo());
    }


    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')||hasRole('ADMIN')")
    public ApiResponse<UserResponse> updateUserProfile(
            @RequestPart("user") String userStr,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        UpdateUserRequest updateUserRequest = ValidationUtil.validateUserStr(userStr);

        return ApiResponse.success(userService.updateUserProfile(updateUserRequest, file));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')||hasRole('STAFF')")
    ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.success(userService.getUsers());
    }

    @GetMapping("/customers")
    @PreAuthorize("hasRole('ADMIN')||hasRole('STAFF')")
    ApiResponse<List<UserResponse>> getAllCustomer() {
        return ApiResponse.success(userService.getAllCustomer());
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<List<UserResponse>> getAllAdmin() {
        return ApiResponse.success(userService.getAllAdmin());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')||hasRole('STAFF')")
    ApiResponse<UserResponse> getCustomerById(@PathVariable Long userId) {
        return ApiResponse.success(userService.getCustomerById(userId));
    }

    @GetMapping("/admins/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<UserResponse> getSystemUsersById(@PathVariable Long userId) {
        return ApiResponse.success(userService.getSystemUsersById(userId));
    }

    @PutMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponse> updateUserProfileById(
            @PathVariable Long userId,
            @RequestPart("user") String userStr,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        UpdateUserRequest updateUserRequest = ValidationUtil.validateUserStr(userStr);
        return ApiResponse.success(userService.updateUserProfileById(updateUserRequest, file, userId));
    }

    @PostMapping(value = "/admins", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponse> createSystemUserProfile(
            @RequestPart("user") String userStr,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        CreateSystemUserRequest request = ValidationUtil.validateSystemUserStr(userStr);
        return ApiResponse.success(userService.createSystemUser(request, file));
    }

    @PutMapping(value = "/admins/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponse> updateSystemUserProfile(
            @PathVariable Long userId,
            @RequestPart("user") String userStr,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

        UpdateSystemUserRequest request = ValidationUtil.validateUpdateSystemUserStr(userStr);
        return ApiResponse.success(userService.updateSystemUser(request, file, userId));
    }

    @DeleteMapping("/customers")
    @PreAuthorize("hasRole('ADMIN')||hasRole('STAFF')")
    public ApiResponse<String> deleteCustomers(@RequestParam Long[] ids) {
        userService.deleteUserssByIds(ids);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deleteAdmins(@RequestParam Long[] ids) {
        userService.deleteUserssByIds(ids);
        return ApiResponse.success(null);
    }


//    @GetMapping
//    ApiResponse<List<UserResponse>> getUsers() {
//        var authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        log.info("Username {}", authentication.getName());
//        authentication.getAuthorities().forEach(grantedAuthority -> log.info("GrantedAuthority {}", grantedAuthority.getAuthority()));
//
//        return (ApiResponse.<List<UserResponse>>builder()
//                .result(userService.getUsers())
//                .build());
//
//    }
//
//    @GetMapping("/page")
//    public PaginatedApiResponse<UserResponse> getUsers(
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "size", defaultValue = "10") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        PaginatedApiResponse<UserResponse> response = userService.getUsers(pageable);
//
//
//        return response;
//    }
//
//    @GetMapping("/{userId}")
//    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
//        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
//        apiResponse.setCode(1000);
//        apiResponse.setResult(userService.getUserRespone(userId));
//        return apiResponse;
//    }
//
//    @PutMapping("/{userId}")
//    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
//        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
//        apiResponse.setCode(1000);
//        apiResponse.setResult(userService.updateUser(userId, request));
//        return apiResponse;
//    }
//
//    @DeleteMapping("/{userId}")
//    String deleteUser(@PathVariable String userId) {
//        userService.deleteUser(userId);
//        return "User has been deleted";
//    }

}
