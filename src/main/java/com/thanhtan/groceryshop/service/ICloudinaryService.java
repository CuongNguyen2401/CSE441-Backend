package com.thanhtan.groceryshop.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface ICloudinaryService {
     Map upload(MultipartFile file);

     CompletableFuture<Boolean> deleteImage(String publicId);
}
