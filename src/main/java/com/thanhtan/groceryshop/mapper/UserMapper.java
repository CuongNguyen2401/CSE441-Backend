package com.thanhtan.groceryshop.mapper;


import com.thanhtan.groceryshop.dto.request.CreateSystemUserRequest;
import com.thanhtan.groceryshop.dto.request.UpdateSystemUserRequest;
import com.thanhtan.groceryshop.dto.request.UpdateUserRequest;
import com.thanhtan.groceryshop.dto.request.UserRequest;
import com.thanhtan.groceryshop.dto.response.UserResponse;
import com.thanhtan.groceryshop.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {


    User toUser(UserRequest request);

    @Mapping(target = "roles", source = "roles", ignore = true)
    User toUser(CreateSystemUserRequest request);


    @Mapping(target = "roles", source = "roles", ignore = true)
    UserResponse toUserResponse(User user);


    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "orders", ignore = true)
    void updateUser(@MappingTarget User user, UpdateUserRequest request);


    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "orders", ignore = true)
    void updateUser(@MappingTarget User user, UpdateSystemUserRequest request);


    List<UserResponse> toUserResponseList(List<User> allUsers);
}
