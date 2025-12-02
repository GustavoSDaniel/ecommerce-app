package com.gustavosdaniel.ecommerce_api.product;

public interface ProductService {

    ProductResponse createProduct(Integer categoryId, ProductRequest productRequest);
}
