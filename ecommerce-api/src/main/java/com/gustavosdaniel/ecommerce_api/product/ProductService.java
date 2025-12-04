package com.gustavosdaniel.ecommerce_api.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductResponse createProduct(Integer categoryId, ProductRequest productRequest);

    Page<ProductResponse> getAllProductsByCategoryId(Integer categoryId, Pageable pageable);

    void reactivateProduct(Long productId);

    void desativeProduct(Long productId);

    void deleteProduct(Long id);
}
