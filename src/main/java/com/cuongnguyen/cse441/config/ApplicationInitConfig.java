package com.cuongnguyen.cse441.config;

import com.cuongnguyen.cse441.entity.Category;
import com.cuongnguyen.cse441.entity.Product;
import com.cuongnguyen.cse441.entity.Role;
import com.cuongnguyen.cse441.entity.User;
import com.cuongnguyen.cse441.enums.ProductStatus;
import com.cuongnguyen.cse441.enums.Status;
import com.cuongnguyen.cse441.repository.CategoryRepository;
import com.cuongnguyen.cse441.repository.ProductRepository;
import com.cuongnguyen.cse441.repository.RoleRepository;
import com.cuongnguyen.cse441.repository.UserRepository;
import com.cuongnguyen.cse441.util.EmailSender;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    EmailSender emailSender;

    @Bean
    ApplicationRunner applicationRunner(
            UserRepository userRepository,
            RoleRepository roleRepository,
            CategoryRepository categoryRepository,
            ProductRepository productRepository) {
        return args -> {
            // Initialize roles if they don't exist
            initRoles(roleRepository);

            // Initialize admin user if it doesn't exist
            initAdminUser(userRepository, roleRepository);

            // Initialize categories if they don't exist
            initCategories(categoryRepository);

            // Initialize products if they don't exist
            initProducts(productRepository, categoryRepository);

            log.info("Initialization completed successfully");
        };
    }

    private void initRoles(RoleRepository roleRepository) {
        if (roleRepository.count() == 0) {
            // Create ADMIN role
            Role adminRole = new Role("ADMIN", "Administrator with all privileges", null);
            roleRepository.save(adminRole);

            // Create USER role
            Role userRole = new Role("USER", "Regular user with basic privileges", null);
            roleRepository.save(userRole);

            // Create MANAGER role
            Role managerRole = new Role("MANAGER", "Manager with product management privileges", null);
            roleRepository.save(managerRole);

            log.info("Roles have been initialized");
        }
    }

    private void initAdminUser(UserRepository userRepository, RoleRepository roleRepository) {
        if (userRepository.findByUsername("admin").isEmpty()) {
            var roles = new HashSet<Role>();
            Role adminRole = roleRepository.findById("ADMIN").orElseThrow();
            roles.add(adminRole);

            User user = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .email("admin@example.com")
                    .firstName("Admin")
                    .lastName("User")
                    .status(Status.ACTIVE)
                    .roles(roles)
                    .build();

            userRepository.save(user);
            log.warn("Admin user has been created with default password: admin, please change it");
        }
    }

    private void initCategories(CategoryRepository categoryRepository) {
        if (categoryRepository.count() == 0) {
            // Create Electronics category
            Category electronics = Category.builder()
                    .name("Electronics")
                    .description("Electronic devices and accessories")
                    .status(Status.ACTIVE)
                    .build();
            categoryRepository.save(electronics);

            // Create Clothing category
            Category clothing = Category.builder()
                    .name("Clothing")
                    .description("Apparel and fashion items")
                    .status(Status.ACTIVE)
                    .build();
            categoryRepository.save(clothing);

            // Create Food category
            Category food = Category.builder()
                    .name("Food")
                    .description("Groceries and food items")
                    .status(Status.ACTIVE)
                    .build();
            categoryRepository.save(food);

            log.info("Categories have been initialized");
        }
    }

    private void initProducts(ProductRepository productRepository, CategoryRepository categoryRepository) {
        if (productRepository.count() == 0) {
            // Get categories
            Category electronics = categoryRepository.findByName("Electronics").orElseThrow();
            Category clothing = categoryRepository.findByName("Clothing").orElseThrow();
            Category food = categoryRepository.findByName("Food").orElseThrow();

            // Create Electronic products
            Product laptop = Product.builder()
                    .name("Laptop")
                    .description("High-performance laptop with latest specs")
                    .price(1299.99)
                    .salePrice(1199.99)
                    .quantity(50)
                    .image("laptop.jpg")
                    .slug("high-performance-laptop")
                    .productStatus(ProductStatus.ACTIVE)
                    .category(electronics)
                    .build();
            productRepository.save(laptop);

            Product smartphone = Product.builder()
                    .name("Smartphone")
                    .description("Latest smartphone with advanced camera")
                    .price(899.99)
                    .salePrice(849.99)
                    .quantity(100)
                    .image("smartphone.jpg")
                    .slug("latest-smartphone")
                    .productStatus(ProductStatus.ACTIVE)
                    .category(electronics)
                    .build();
            productRepository.save(smartphone);

            // Create Clothing product
            Product tShirt = Product.builder()
                    .name("T-Shirt")
                    .description("Comfortable cotton t-shirt")
                    .price(29.99)
                    .salePrice(24.99)
                    .quantity(200)
                    .image("tshirt.jpg")
                    .slug("comfortable-tshirt")
                    .productStatus(ProductStatus.ACTIVE)
                    .category(clothing)
                    .build();
            productRepository.save(tShirt);

            // Create Food products
            Product organicApples = Product.builder()
                    .name("Organic Apples")
                    .description("Fresh organic apples")
                    .price(5.99)
                    .salePrice(4.99)
                    .quantity(500)
                    .image("apples.jpg")
                    .slug("organic-apples")
                    .productStatus(ProductStatus.ACTIVE)
                    .category(food)
                    .build();
            productRepository.save(organicApples);

            Product chocolateBar = Product.builder()
                    .name("Chocolate Bar")
                    .description("Premium dark chocolate bar")
                    .price(3.99)
                    .salePrice(3.49)
                    .quantity(300)
                    .image("chocolate.jpg")
                    .slug("premium-chocolate")
                    .productStatus(ProductStatus.ACTIVE)
                    .category(food)
                    .build();
            productRepository.save(chocolateBar);

            log.info("Products have been initialized");
        }
    }
}
