package com.gustavosdaniel.ecommerce_api.product;

import com.gustavosdaniel.ecommerce_api.category.Category;
import com.gustavosdaniel.ecommerce_api.category.CategoryRepository;
import com.gustavosdaniel.ecommerce_api.orderItem.OrderItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImplTest.class);
    @Mock
    private ProductMapper productMapper;
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private CategoryRepository categoryRepository;
    
    @InjectMocks
    private ProductServiceImpl productService;
    
    @Nested
    @DisplayName("Should create product with sucesso")
    class CreateProduct {
        
        @Test
        void createProduct() {
            
            Long productId = 1L;
            String name = "TV";
            String description = "TV descricao";
            MeasureUnit measureUnit = MeasureUnit.UNIDADE;
            BigDecimal availableQuantity = BigDecimal.valueOf(5);
            BigDecimal price = BigDecimal.valueOf(10);

            Product product = new Product(name, description, measureUnit, availableQuantity, price);
            ReflectionTestUtils.setField(product, "id", productId);

            Integer id = 1;
            String categoryName = "Eletricos";
            String categoryDescription = "Produtos eletricos de qualidade superior";
            
            
            Category category = new Category(categoryName, categoryDescription);
            ReflectionTestUtils.setField(category, "id", id);

            ProductRequest request = new ProductRequest(
                    name, description, measureUnit, availableQuantity, price);

            ProductResponse response = new ProductResponse(
                    categoryName, productId, name, description, measureUnit,
                    availableQuantity, price, "Gustavo");

            when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
            when(productRepository.existsByNameIgnoreCase(name)).thenReturn(false);
            when(productMapper.toProduct(request)).thenReturn(product);
            when(productRepository.save(any(Product.class))).thenReturn(product);
            when(productMapper.toProductResponse(product)).thenReturn(response);

            ProductResponse output = productService.createProduct(id, request);

            assertNotNull(output);
            assertEquals(response, output);

            verify(productRepository).existsByNameIgnoreCase(name);
            verify(categoryRepository).findById(id);
            verify(productRepository).save(product);
            verify(productMapper).toProductResponse(product);

        }

        @Test
        void createProduct_notFound() {

            Long productId = 1L;
            String name = "TV";
            String description = "TV descricao";
            MeasureUnit measureUnit = MeasureUnit.UNIDADE;
            BigDecimal availableQuantity = BigDecimal.valueOf(5);
            BigDecimal price = BigDecimal.valueOf(10);

            ProductRequest request = new ProductRequest(
                    name, description, measureUnit, availableQuantity, price);

            Integer id = 1;
            String categoryName = "Eletricos";
            String categoryDescription = "Produtos eletricos de qualidade superior";


            Category category = new Category(categoryName, categoryDescription);
            ReflectionTestUtils.setField(category, "id", id);

            when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
            when(productRepository.existsByNameIgnoreCase(name)).thenReturn(true);

            assertThrows(ProductNameExistsException.class,
                    () -> {productService.createProduct(id, request);});

        }
    }

    @Nested
    @DisplayName("Should delete product with sucesso")
    class DeleteProduct {

        @Test
        void deleteProduct() {

            Long productId = 1L;
            String name = "TV";
            String description = "TV descricao";
            MeasureUnit measureUnit = MeasureUnit.UNIDADE;
            BigDecimal availableQuantity = BigDecimal.valueOf(5);
            BigDecimal price = BigDecimal.valueOf(10);

            Product product = new Product(
                    name, description, measureUnit, availableQuantity, price);
            ReflectionTestUtils.setField(product, "id", productId);

            when(productRepository.findById(productId)).thenReturn(Optional.of(product));

            productService.deleteProduct(productId);

        }

        @Test
        void deleteProduct_notFound() {

            Long productId = 1L;

            when(productRepository.findById(productId)).thenReturn(Optional.empty());

            assertThrows(ProductNotFoundException.class, () -> {productService.deleteProduct(productId);});
        }

    }

}