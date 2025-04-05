package com.thanhtan.groceryshop;

import static com.thanhtan.groceryshop.service.impl.CloudinaryService.getPublicIdFromUrl;

public class test {
    public static void main(String[] args) {
        String url = "http://res.cloudinary.com/db9wedddz/image/upload/v1715617711/amizvaz8i9lgyugwucww.jpg";
        String publicId = getPublicIdFromUrl(url);
        System.out.println("Public ID: " + publicId);
    }
}
