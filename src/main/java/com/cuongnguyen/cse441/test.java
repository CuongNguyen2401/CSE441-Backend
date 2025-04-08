package com.cuongnguyen.cse441;

import static com.cuongnguyen.cse441.service.impl.CloudinaryService.getPublicIdFromUrl;

public class test {
    public static void main(String[] args) {
        String url = "http://res.cloudinary.com/db9wedddz/image/upload/v1715617711/amizvaz8i9lgyugwucww.jpg";
        String publicId = getPublicIdFromUrl(url);
        System.out.println("Public ID: " + publicId);
    }
}
