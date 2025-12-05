package com.gustavosdaniel.ecommerce_api.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductResponse createProduct(Integer categoryId, ProductRequest productRequest);

    Page<ProductResponse> getAllProducts(Pageable pageable);

    Page<ProductResponse> getAllActiveProducts(Pageable pageable);

    Page<ProductResponse> getAllActiveFalseProducts(Pageable pageable);

    Page<ProductResponse> getAllProductsByCategoryId(Integer categoryId, Pageable pageable);

    Page<ProductResponse> searchProductByName(String name, Pageable pageable);

    ProductResponse getProductById(Long productId);

    ProductUpdateResponse updateProduct(Long productId, ProductUpdateRequest productRequest);

    StockUpdateResponse updateStock(Long productId, StockUpdateRequest stockUpdateDTO) throws StockOperationExceptionAddAndRemove, StockOperationExceptionSet, insuficienteStockException;

    void reactivateProduct(Long productId);

    void desativeProduct(Long productId);

    void deleteProduct(Long id);
}
