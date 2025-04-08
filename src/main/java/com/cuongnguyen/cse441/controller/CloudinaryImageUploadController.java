package com.cuongnguyen.cse441.controller;

import com.cuongnguyen.cse441.service.impl.CloudinaryService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/cloudinary/upload")
@RequiredArgsConstructor
public class CloudinaryImageUploadController {

    private final CloudinaryService cloudinaryService;

    @PostMapping
    public ResponseEntity<Map> uploadImage(@RequestParam("image") MultipartFile file){
        Map data = this.cloudinaryService.upload(file);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteImage(@RequestParam("public_id") String publicId){
        this.cloudinaryService.deleteImage(publicId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}