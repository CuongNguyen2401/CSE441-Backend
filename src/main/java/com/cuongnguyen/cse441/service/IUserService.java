package com.cuongnguyen.cse441.service;

import com.cuongnguyen.cse441.dto.request.CreateSystemUserRequest;
import com.cuongnguyen.cse441.dto.request.UpdateSystemUserRequest;
import com.cuongnguyen.cse441.dto.request.UpdateUserRequest;
import com.cuongnguyen.cse441.dto.request.UserRequest;
import com.cuongnguyen.cse441.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService {
    UserResponse createUser(UserRequest request);

    UserResponse getMyInfo();

    UserResponse updateUserProfile(UpdateUserRequest request, MultipartFile file);

    UserResponse createSystemUser(CreateSystemUserRequest request, MultipartFile file);

    UserResponse updateSystemUser(UpdateSystemUserRequest request, MultipartFile file, Long userId);

    UserResponse updateUserProfileById(UpdateUserRequest request, MultipartFile file, Long userId);

    List<UserResponse> getUsers();

    List<UserResponse> getAllCustomer();

    UserResponse getCustomerById(Long userId);

    UserResponse getSystemUsersById(Long userId);

    List<UserResponse> getAllAdmin();

    void deleteUserssByIds(Long[] ids);

}
