package com.gustavosdaniel.ecommerce_api.product;

public interface ProductService {

    ProductResponse createProduct(Integer categoryId, ProductRequest productRequest);

    void reactivateProduct(Long productId);

    void desativeProduct(Long productId);

    void deleteProduct(Long id);
}
