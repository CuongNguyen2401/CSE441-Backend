package com.cuongnguyen.cse441.service.impl;

import com.cuongnguyen.cse441.dto.request.CreateSystemUserRequest;
import com.cuongnguyen.cse441.dto.request.UpdateSystemUserRequest;
import com.cuongnguyen.cse441.dto.request.UpdateUserRequest;
import com.cuongnguyen.cse441.dto.request.UserRequest;
import com.cuongnguyen.cse441.dto.response.UserResponse;
import com.cuongnguyen.cse441.entity.Role;
import com.cuongnguyen.cse441.entity.User;
import com.cuongnguyen.cse441.enums.Status;
import com.cuongnguyen.cse441.exception.AppException;
import com.cuongnguyen.cse441.exception.ErrorCode;
import com.cuongnguyen.cse441.mapper.UserMapper;
import com.cuongnguyen.cse441.repository.RoleRepository;
import com.cuongnguyen.cse441.repository.UserRepository;
import com.cuongnguyen.cse441.service.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.cuongnguyen.cse441.service.impl.CloudinaryService.getPublicIdFromUrl;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService {

    UserRepository userRepository;

    UserMapper userMapper;

    RoleRepository roleRepository;

    PasswordEncoder BCryptPasswordEncoder;

    CloudinaryService cloudinaryService;

    @Override
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);

        if (user.getStatus() == Status.INACTIVE)
            throw new AppException(ErrorCode.USER_INACTIVE);

        user.setPassword(BCryptPasswordEncoder.encode(request.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(com.cuongnguyen.cse441.enums.Role.USER.name()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        roles.add(userRole);
        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getMyInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        UserResponse userResponse = userMapper.toUserResponse(user);
        Set<String> roles = new HashSet<>();
        user.getRoles().forEach(role -> roles.add(role.getName()));
        userResponse.setRoles(roles);
        return userResponse;
    }

    @Override
    public UserResponse updateSystemUser(UpdateSystemUserRequest request, MultipartFile file, Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) throw new AppException(ErrorCode.USER_NOT_EXISTED);
        String oldImagePublicId = getPublicIdFromUrl(user.getAvatar() != null ? user.getAvatar() : "");
        userMapper.updateUser(user, request);
        if (file != null) {
            Map data = cloudinaryService.upload(file);
            if (data != null) {
                String newImageUrl = (String) data.get("url");
                if (!oldImagePublicId.isEmpty()) cloudinaryService.deleteImage(oldImagePublicId);
                user.setAvatar(newImageUrl);
            }
        }
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(request.getRoles())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        roles.add(userRole);
        user.setRoles(roles);
        User updatedUser = userRepository.save(user);

        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    public UserResponse createSystemUser(CreateSystemUserRequest request, MultipartFile file) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);

        if (user.getStatus() == Status.INACTIVE)
            throw new AppException(ErrorCode.USER_INACTIVE);

        user.setPassword(BCryptPasswordEncoder.encode(request.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(request.getRoles())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        roles.add(userRole);
        user.setRoles(roles);

        if (file != null) {
            Map data = cloudinaryService.upload(file);
            if (data != null) {
                String imageUrl = (String) data.get("url");
                user.setAvatar(imageUrl);
            }
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateUserProfileById(UpdateUserRequest request, MultipartFile file, Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) throw new AppException(ErrorCode.USER_NOT_EXISTED);
        String oldImagePublicId = getPublicIdFromUrl(user.getAvatar() != null ? user.getAvatar() : "");
        userMapper.updateUser(user, request);
        if (file != null) {
            Map data = cloudinaryService.upload(file);
            if (data != null) {
                String newImageUrl = (String) data.get("url");
                if (!oldImagePublicId.isEmpty()) cloudinaryService.deleteImage(oldImagePublicId);
                user.setAvatar(newImageUrl);
            }
        }
//       user.setStatus(request.getStatus());
        User updatedUser = userRepository.save(user);

        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    public UserResponse updateUserProfile(UpdateUserRequest request, MultipartFile file) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String oldImagePublicId = getPublicIdFromUrl(user.getAvatar() != null ? user.getAvatar() : "");
        userMapper.updateUser(user, request);
        if (file != null) {
            Map data = cloudinaryService.upload(file);
            if (data != null) {
                String newImageUrl = (String) data.get("url");
                if (!oldImagePublicId.isEmpty()) cloudinaryService.deleteImage(oldImagePublicId);
                user.setAvatar(newImageUrl);
            }
        }
        User updatedUser = userRepository.save(user);

        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    public List<UserResponse> getUsers() {

        return userMapper.toUserResponseList(userRepository.findAllWithRoles());
    }

    @Override
    public UserResponse getSystemUsersById(Long userId) {
        User user = userRepository.findById(userId);
        Set<String> roles = new HashSet<>();
        user.getRoles().forEach(role -> roles.add(role.getName()));

        if (!roles.contains("ADMIN") && !roles.contains("STAFF")) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        UserResponse userResponse = userMapper.toUserResponse(user);
        userResponse.setRoles(roles);
        return userResponse;
    }

    @Override
    public UserResponse getCustomerById(Long userId) {
        User user = userRepository.findById(userId);
        Set<String> roles = new HashSet<>();
        user.getRoles().forEach(role -> roles.add(role.getName()));


        if (!roles.contains("USER")) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        UserResponse userResponse = userMapper.toUserResponse(user);
        userResponse.setRoles(roles);
        return userResponse;
    }

    @Override
    public void deleteUserssByIds(Long[] ids) {
        for (Long id : ids) {
            User user = userRepository.findById(id);
            if (user != null) {
                user.setStatus(Status.INACTIVE);
                userRepository.save(user);
            }
        }
    }

    @Override
    public List<UserResponse> getAllAdmin() {
        List<User> adminUsers = userRepository.findAllWithRoles(com.cuongnguyen.cse441.enums.Role.ADMIN.name());
        List<User> staffUsers = userRepository.findAllWithRoles(com.cuongnguyen.cse441.enums.Role.STAFF.name());

        List<User> combinedUsers = new ArrayList<>();
        combinedUsers.addAll(adminUsers);
        combinedUsers.addAll(staffUsers);

        return userMapper.toUserResponseList(combinedUsers);
    }

    @Override
    public List<UserResponse> getAllCustomer() {
        return userMapper.toUserResponseList(userRepository.findAllWithRoles(com.cuongnguyen.cse441.enums.Role.USER.name()));
    }


}
