package com.cuongnguyen.cse441.mapper;


import com.cuongnguyen.cse441.dto.request.CreateSystemUserRequest;
import com.cuongnguyen.cse441.dto.request.UpdateSystemUserRequest;
import com.cuongnguyen.cse441.dto.request.UpdateUserRequest;
import com.cuongnguyen.cse441.dto.request.UserRequest;
import com.cuongnguyen.cse441.dto.response.UserResponse;
import com.cuongnguyen.cse441.entity.User;
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
