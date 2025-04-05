package com.thanhtan.groceryshop.service.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thanhtan.groceryshop.dto.request.ProductRequest;
import com.thanhtan.groceryshop.dto.request.UpdateProductRequest;
import com.thanhtan.groceryshop.dto.response.ProductResponse;
import com.thanhtan.groceryshop.dto.response.ProductSalesResponse;
import com.thanhtan.groceryshop.dto.response.SalesCategory;
import com.thanhtan.groceryshop.entity.Category;
import com.thanhtan.groceryshop.entity.Product;
import com.thanhtan.groceryshop.entity.QCategory;
import com.thanhtan.groceryshop.entity.QProduct;
import com.thanhtan.groceryshop.enums.ProductStatus;
import com.thanhtan.groceryshop.exception.ErrorCode;
import com.thanhtan.groceryshop.exception.ResourceNotFound;
import com.thanhtan.groceryshop.mapper.ProductMapper;
import com.thanhtan.groceryshop.repository.CategoryRepository;
import com.thanhtan.groceryshop.repository.OrderRepository;
import com.thanhtan.groceryshop.repository.ProductRepository;
import com.thanhtan.groceryshop.repository.RatingRepository;
import com.thanhtan.groceryshop.service.IProductService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.thanhtan.groceryshop.service.impl.CloudinaryService.getPublicIdFromUrl;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService implements IProductService {

    ProductRepository productRepository;

    ProductMapper productMapper;

    CategoryRepository categoryRepository;

    CloudinaryService cloudinaryService;

    JPAQueryFactory queryFactory;

    RatingRepository ratingRepository;

    OrderRepository orderRepository;


    @Override
    public List<ProductResponse> findByCategory(String category) {
        if (category.equals("products"))
            return productRepository.findAll().stream()
                    .map(productMapper::toProductResponse)
                    .toList();

        return productRepository.findByCategoryName(category).stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> find5ProductsByCategory(String categoryName) {
        QProduct qProduct = QProduct.product;
        List<Product> products = queryFactory
                .select(Projections.bean(Product.class, qProduct.id, qProduct.name, qProduct.price, qProduct.image, qProduct.slug))
                .from(qProduct)
                .where(qProduct.category.name.eq(categoryName))
                .limit(5)
                .fetch();

        return products.stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> findSalesProduct() {
        QProduct qProduct = QProduct.product;
        QCategory qCategory = QCategory.category;
        List<Product> products = queryFactory
                .select(Projections.bean(Product.class,
                        qProduct.id,
                        qProduct.name,
                        qProduct.price,
                        qProduct.image,
                        qProduct.slug,
                        qProduct.salePrice,
                        qProduct.productStatus,
                        Projections.bean(Category.class, qProduct.category.id.as("id"), qProduct.category.name.as("name")).as("category")))
                .from(qProduct)
                .join(qProduct.category, qCategory)
                .where(qProduct.salePrice.lt(qProduct.price))
                .fetch();

        return products.stream()
                .sorted((p1, p2) -> Double.compare(
                        (p2.getPrice() - p2.getSalePrice()) / p2.getPrice() * 100,
                        (p1.getPrice() - p1.getSalePrice()) / p1.getPrice() * 100))
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }


    @Override
    public ProductResponse findById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new ResourceNotFound(ErrorCode.RESOURCE_NOT_FOUND));
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest product, MultipartFile file) {
        Product newProduct = productMapper.toProduct(product);

        Category category = categoryRepository.findById(product.getCategoryId())
                .orElseThrow(() -> new ResourceNotFound(ErrorCode.RESOURCE_NOT_FOUND));

        newProduct.setCategory(category);

        if (product.getRelatedProducts() != null) {
            newProduct.setRelatedProducts(
                    product.getRelatedProducts().stream()
                            .map(id -> productRepository.findById(id)
                                    .orElseThrow(() -> new ResourceNotFound(ErrorCode.RESOURCE_NOT_FOUND)))
                            .collect(Collectors.toSet())
            );
        }

        Map data = cloudinaryService.upload(file);
        if (data != null) {
            newProduct.setImage((String) data.get("url"));
        }

        newProduct = productRepository.save(newProduct);

        String slug = createSlug(newProduct);

        newProduct.setSlug(slug);
        newProduct = productRepository.save(newProduct);

        return productMapper.toProductResponse(newProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(UpdateProductRequest product, MultipartFile file) {

        Product productToUpdate = productRepository.findById(product.getId())
                .orElseThrow(() -> new ResourceNotFound(ErrorCode.RESOURCE_NOT_FOUND));

        Category categoryToUpdate = categoryRepository.findById(product.getCategoryId())
                .orElseThrow(() -> new ResourceNotFound(ErrorCode.RESOURCE_NOT_FOUND));

        String oldImagePublicId = getPublicIdFromUrl(productToUpdate.getImage());
        productMapper.updateProduct(productToUpdate, product);
        productToUpdate.setSlug(createSlug(productToUpdate));
        productToUpdate.setCategory(categoryToUpdate);

        if (product.getRelatedProducts() != null) {
            productToUpdate.setRelatedProducts(
                    product.getRelatedProducts().stream()
                            .map(id -> productRepository.findById(id)
                                    .orElseThrow(() -> new ResourceNotFound(ErrorCode.RESOURCE_NOT_FOUND)))
                            .collect(Collectors.toSet())
            );
        }
        if (file != null) {

            Map data = cloudinaryService.upload(file);
            if (data != null) {
                String newImageUrl = (String) data.get("url");
                cloudinaryService.deleteImage(oldImagePublicId);
                productToUpdate.setImage(newImageUrl);
            }
        }
        productToUpdate = productRepository.save(productToUpdate);

        return productMapper.toProductResponse(productToUpdate);
    }


    @Override
    public ProductResponse findBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFound(ErrorCode.RESOURCE_NOT_FOUND));

        ProductResponse productResponse = productMapper.toProductResponse(product);

        productResponse.setRelatedProducts(
                productRepository.findByRelatedProductsContains(product)
                        .stream()
                        .map(productMapper::toProductResponse)
                        .collect(Collectors.toSet())
        );


        return productResponse;
    }

    @Override
    public List<ProductResponse> findProductsByQueryString(String queryString) {
        QProduct qProduct = QProduct.product;
        QCategory qCategory = QCategory.category;
        List<Product> products = queryFactory
                .select(Projections.bean(Product.class,
                        qProduct.id,
                        qProduct.name,
                        qProduct.price,
                        qProduct.image,
                        qProduct.slug,
                        qProduct.salePrice,
                        Projections.bean(Category.class,
                                qProduct.category.id.as("id"),
                                qProduct.category.name.as("name")).as("category")))
                .from(qProduct)
                .join(qProduct.category, qCategory)
                .where(qProduct.name.containsIgnoreCase(queryString)
                        .or(qProduct.description.containsIgnoreCase(queryString))
                        .or(qProduct.category.name.containsIgnoreCase(queryString)))
                .fetch();

        return products.stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    @Override
    public void deleteProductsById(Long[] id) {
        for (Long aLong : id) {
            productRepository.updateStatusById(aLong, ProductStatus.INACTIVE);
        }
    }

    @Override
    public void updateStatusByIds(Long[] ids) {
        for (Long aLong : ids) {
            productRepository.updateStatusById(aLong, ProductStatus.ACTIVE);
        }
    }

    @Override
    public List<ProductSalesResponse> findMostSoldProduct() {
        return orderRepository.findMostSoldProduct().stream()
                .map(tuple -> new ProductSalesResponse(
                        productMapper.toProductResponse(tuple.get(0, Product.class)),
                        tuple.get(1, Integer.class).longValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<SalesCategory> findSalesCategory(Date startDate, Date endDate) {
        return productRepository.findRevenueByCategoryInPeriod(startDate, endDate).stream()
                .map(tuple -> new SalesCategory(
                        tuple.get(0, String.class),
                        tuple.get(1, Double.class)))
                .collect(Collectors.toList());
    }


    @Override
    public List<ProductResponse> findAll() {
        List<ProductResponse> result = productRepository.findAll().stream()
                .map(productMapper::toProductResponse)
                .toList();

        for (ProductResponse productResponse : result) {

            productResponse.setRatings(ratingRepository.getAverageRating(productResponse.getId()));
        }

        return result;
    }

    private String createSlug(Product product) {
        return product.getName().toLowerCase().replace(" ", "-") + "-" + product.getId();
    }
}
