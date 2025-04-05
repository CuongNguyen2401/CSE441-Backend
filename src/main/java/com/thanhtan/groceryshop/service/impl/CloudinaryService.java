package com.thanhtan.groceryshop.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.thanhtan.groceryshop.service.ICloudinaryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryService implements ICloudinaryService {

    Cloudinary cloudinary;


    public Map upload(MultipartFile file)  {
        try{
            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            return data;
        }catch (IOException io){
            throw new RuntimeException("Image upload fail");
        }
    }

    @Override
    public CompletableFuture<Boolean> deleteImage(String publicId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Map response = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                return "ok".equals(response.get("result"));
            } catch (Exception e) {
                System.err.println("Failed to delete image: " + e.getMessage());
                return false;
            }
        });
    }

    public static String getPublicIdFromUrl(String url) {

        if (url == null || !url.contains("/image/upload/")) {
            return "";
        }
        String[] parts = url.split("/image/upload/");
        if (parts.length < 2) {
            return "";
        }

        String versionAndPublicId = parts[1];

        String[] versionAndPublicIdParts = versionAndPublicId.split("/");

        String publicIdWithExtension = versionAndPublicIdParts[versionAndPublicIdParts.length - 1];

        String publicId = publicIdWithExtension.substring(0, publicIdWithExtension.lastIndexOf('.'));

        return publicId;
    }
}