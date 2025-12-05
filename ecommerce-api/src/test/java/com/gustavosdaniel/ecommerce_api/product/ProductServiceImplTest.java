package com.gustavosdaniel.ecommerce_api.product;

import com.gustavosdaniel.ecommerce_api.category.Category;
import com.gustavosdaniel.ecommerce_api.category.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

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
    @DisplayName("Should all products")
    class FindAllProducts {

        @Test
        void getAllProducts() {

            Pageable pageable = PageRequest.of(0, 10);

            Long productId = 1L;
            Long productId1 = 2L;
            Long productId2 = 3L;

            Integer id = 1;
            String categoryName = "Eletricos";
            String description = "Produtos eletricos de qualidade superior";

            Category category = new Category(categoryName, description);
            ReflectionTestUtils.setField(category, "id", id);

            Product product = new Product("Maquita", "Maquita eletrica", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(5), BigDecimal.valueOf(40.00));
            ReflectionTestUtils.setField(product, "id", productId);

            Product product1 = new Product("TV", "TV GRANDE", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(3), BigDecimal.valueOf(90.00));
            ReflectionTestUtils.setField(product1, "id", productId1);

            Product product2 = new Product("PC", "PC DA XUXA", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(10), BigDecimal.valueOf(300));
            ReflectionTestUtils.setField(product2, "id", productId2);

            List<Product> products = Arrays.asList(product, product1, product2);

            Page<Product> productPage = new PageImpl<>(products, pageable, products.size());

            ProductResponse response = new ProductResponse(
                    categoryName, productId, "Maquita", "Maquita eletrica", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(5),BigDecimal.valueOf(40), "GUSTAVO");

            ProductResponse response1 = new ProductResponse(
                    categoryName, productId1, "TV", "TV GRANDE", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(3), BigDecimal.valueOf(90.00), "GUSTAVO");

            ProductResponse response2 = new ProductResponse(
                    categoryName, productId2, "PC", "PC DA XUXA", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(10), BigDecimal.valueOf(300), "GUSTAVO");

            when(productRepository.findAll(pageable)).thenReturn(productPage);

            when(productMapper.toProductResponse(product)).thenReturn(response);
            when(productMapper.toProductResponse(product1)).thenReturn(response1);
            when(productMapper.toProductResponse(product2)).thenReturn(response2);

            Page<ProductResponse> output = productService.getAllProducts( pageable);

            assertNotNull(output);
            assertEquals(3, output.getTotalElements());

            verify(productMapper, times(3)).toProductResponse(any(Product.class));

        }
    }

    @Nested
    @DisplayName("Should all products ativos")
    class GetProductsAtivos {

        @Test
        void getProductsAtivos() {

            Pageable pageable = PageRequest.of(0, 10);

            Long productId = 1L;
            Long productId1 = 2L;
            Long productId2 = 3L;

            Integer id = 1;
            String categoryName = "Eletricos";
            String description = "Produtos eletricos de qualidade superior";

            Category category = new Category(categoryName, description);
            ReflectionTestUtils.setField(category, "id", id);

            Product product = new Product("Maquita", "Maquita eletrica", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(5), BigDecimal.valueOf(40.00));
            ReflectionTestUtils.setField(product, "id", productId);

            Product product1 = new Product("TV", "TV GRANDE", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(3), BigDecimal.valueOf(90.00));
            ReflectionTestUtils.setField(product1, "id", productId1);

            Product product2 = new Product("PC", "PC DA XUXA", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(10), BigDecimal.valueOf(300));
            ReflectionTestUtils.setField(product2, "id", productId2);

            List<Product> products = Arrays.asList(product, product1, product2);

            Page<Product> productPage = new PageImpl<>(products, pageable, products.size());

            ProductResponse response = new ProductResponse(
                    categoryName, productId, "Maquita", "Maquita eletrica", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(5),BigDecimal.valueOf(40), "GUSTAVO");

            ProductResponse response1 = new ProductResponse(
                    categoryName, productId1, "TV", "TV GRANDE", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(3), BigDecimal.valueOf(90.00), "GUSTAVO");

            ProductResponse response2 = new ProductResponse(
                    categoryName, productId2, "PC", "PC DA XUXA", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(10), BigDecimal.valueOf(300), "GUSTAVO");

            when(productRepository.findActiveProducts(pageable)).thenReturn(productPage);

            when(productMapper.toProductResponse(product)).thenReturn(response);
            when(productMapper.toProductResponse(product1)).thenReturn(response1);
            when(productMapper.toProductResponse(product2)).thenReturn(response2);

            Page<ProductResponse> output = productService.getAllActiveProducts( pageable);

            assertNotNull(output);
            assertEquals(3, output.getTotalElements());

            verify(productMapper, times(3)).toProductResponse(any(Product.class));
        }
    }

    @Nested
    @DisplayName("Should all products inativos")
    class GetProductsInativos {

        @Test
        void getProductsInativos() {

            Pageable pageable = PageRequest.of(0, 10);

            Long productId = 1L;
            Long productId1 = 2L;
            Long productId2 = 3L;

            Integer id = 1;
            String categoryName = "Eletricos";
            String description = "Produtos eletricos de qualidade superior";

            Category category = new Category(categoryName, description);
            ReflectionTestUtils.setField(category, "id", id);

            Product product = new Product("Maquita", "Maquita eletrica", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(5), BigDecimal.valueOf(40.00));
            ReflectionTestUtils.setField(product, "id", productId);

            Product product1 = new Product("TV", "TV GRANDE", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(3), BigDecimal.valueOf(90.00));
            ReflectionTestUtils.setField(product1, "id", productId1);

            Product product2 = new Product("PC", "PC DA XUXA", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(10), BigDecimal.valueOf(300));
            ReflectionTestUtils.setField(product2, "id", productId2);

            List<Product> products = Arrays.asList(product, product1, product2);

            Page<Product> productPage = new PageImpl<>(products, pageable, products.size());

            ProductResponse response = new ProductResponse(
                    categoryName, productId, "Maquita", "Maquita eletrica", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(5),BigDecimal.valueOf(40), "GUSTAVO");

            ProductResponse response1 = new ProductResponse(
                    categoryName, productId1, "TV", "TV GRANDE", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(3), BigDecimal.valueOf(90.00), "GUSTAVO");

            ProductResponse response2 = new ProductResponse(
                    categoryName, productId2, "PC", "PC DA XUXA", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(10), BigDecimal.valueOf(300), "GUSTAVO");

            when(productRepository.findByActiveFalse(pageable)).thenReturn(productPage);

            when(productMapper.toProductResponse(product)).thenReturn(response);
            when(productMapper.toProductResponse(product1)).thenReturn(response1);
            when(productMapper.toProductResponse(product2)).thenReturn(response2);

            Page<ProductResponse> output = productService.getAllActiveFalseProducts( pageable);

            assertNotNull(output);
            assertEquals(3, output.getTotalElements());

            verify(productMapper, times(3)).toProductResponse(any(Product.class));
        }
    }

    @Nested
    @DisplayName("Should products by categories with sucesso")
    class GetProductsByCategory {

        @Test
        void getProductsByCategory() {

            Pageable pageable = PageRequest.of(0, 10);

            Long productId = 1L;
            Long productId1 = 2L;
            Long productId2 = 3L;

            Integer id = 1;
            String categoryName = "Eletricos";
            String description = "Produtos eletricos de qualidade superior";

            Category category = new Category(categoryName, description);
            ReflectionTestUtils.setField(category, "id", id);

            Product product = new Product("Maquita", "Maquita eletrica", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(5), BigDecimal.valueOf(40.00));
            ReflectionTestUtils.setField(product, "id", productId);

            Product product1 = new Product("TV", "TV GRANDE", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(3), BigDecimal.valueOf(90.00));
            ReflectionTestUtils.setField(product1, "id", productId1);

            Product product2 = new Product("PC", "PC DA XUXA", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(10), BigDecimal.valueOf(300));
            ReflectionTestUtils.setField(product2, "id", productId2);

            List<Product> products = Arrays.asList(product, product1, product2);

            Page<Product> productPage = new PageImpl<>(products, pageable, products.size());

            ProductResponse response = new ProductResponse(
                    categoryName, productId, "Maquita", "Maquita eletrica", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(5),BigDecimal.valueOf(40), "GUSTAVO");

            ProductResponse response1 = new ProductResponse(
                    categoryName, productId1, "TV", "TV GRANDE", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(3), BigDecimal.valueOf(90.00), "GUSTAVO");

            ProductResponse response2 = new ProductResponse(
                    categoryName, productId2, "PC", "PC DA XUXA", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(10), BigDecimal.valueOf(300), "GUSTAVO");

            when(productRepository.findByCategory_Id(id, pageable)).thenReturn(productPage);

            when(productMapper.toProductResponse(product)).thenReturn(response);
            when(productMapper.toProductResponse(product1)).thenReturn(response1);
            when(productMapper.toProductResponse(product2)).thenReturn(response2);

            Page<ProductResponse> output = productService.getAllProductsByCategoryId(id, pageable);

            assertNotNull(output);
            assertEquals(3, output.getTotalElements());

            verify(productMapper, times(3)).toProductResponse(any(Product.class));

        }
    }

    @Nested
    @DisplayName("Should product search name with sucesso")
    class GetProductsByNameSearch {

        @Test
        void getProductsByNameSearch() {

            Pageable pageable = PageRequest.of(0, 10);

            Long productId = 1L;

            Integer id = 1;
            String categoryName = "Eletricos";
            String description = "Produtos eletricos de qualidade superior";

            Category category = new Category(categoryName, description);
            ReflectionTestUtils.setField(category, "id", id);

            Product product = new Product("Maquita", "Maquita eletrica", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(5), BigDecimal.valueOf(40.00));
            ReflectionTestUtils.setField(product, "id", productId);

            List<Product> products = Arrays.asList(product);

            Page<Product> productPage = new PageImpl<>(products, pageable, products.size());

            ProductResponse response = new ProductResponse(
                    categoryName, productId, "Maquita", "Maquita eletrica", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(5),BigDecimal.valueOf(40), "GUSTAVO");

            when(productRepository.findByNameContainingIgnoreCase("Maquita", pageable))
                    .thenReturn(productPage);

            when(productMapper.toProductResponse(product)).thenReturn(response);

            Page<ProductResponse> output = productService.searchProductByName("Maquita", pageable);

            assertNotNull(output);
            assertEquals(response, output.getContent().get(0));

            verify(productRepository).findByNameContainingIgnoreCase("Maquita", pageable);
            verify(productMapper).toProductResponse(any(Product.class));

        }

    }

    @Nested
    @DisplayName("Should product by id with sucesso")
    class GetProductById {

        @Test
        void getProductById() {

            Integer id = 1;
            String categoryName = "Eletricos";
            String descriptionCategory = "Produtos eletricos de qualidade superior";

            Category category = new Category(categoryName, descriptionCategory);
            ReflectionTestUtils.setField(category, "id", id);

            Long productId = 1L;
            String name = "TV";
            String description = "TV descricao";
            MeasureUnit measureUnit = MeasureUnit.UNIDADE;
            BigDecimal availableQuantity = BigDecimal.valueOf(5);
            BigDecimal price = BigDecimal.valueOf(10);

            Product product = new Product(name, description, measureUnit, availableQuantity, price);
            ReflectionTestUtils.setField(product, "id", productId);

            ProductResponse response = new ProductResponse(
                    categoryName, productId, "Maquita", "Maquita eletrica", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(5),BigDecimal.valueOf(40), "GUSTAVO");

            when(productRepository.findById(productId)).thenReturn(Optional.of(product));
            when(productMapper.toProductResponse(product)).thenReturn(response);

            ProductResponse output = productService.getProductById(productId);

            assertNotNull(output);
            assertEquals(response, output);

            verify(productMapper).toProductResponse(any(Product.class));
            verify(productRepository).findById(productId);



        }
    }

    @Nested
    @DisplayName("Should update product with sucesso")
    class UpdateProduct{

        @Test
        void updateProduct() {

            Long productId = 1L;

            Integer id = 1;
            String categoryName = "Eletricos";
            String description = "Produtos eletricos de qualidade superior";

            Category category = new Category(categoryName, description);
            ReflectionTestUtils.setField(category, "id", id);

            Product product = new Product("Maquita", "Maquita eletrica", MeasureUnit.UNIDADE,
                    BigDecimal.valueOf(5), BigDecimal.valueOf(40.00));
            ReflectionTestUtils.setField(product, "id", productId);

            ProductUpdateRequest updateRequest = new ProductUpdateRequest(
                    "MAQUITAATUALIZADA", "Maquita eletrica ATUALIZADA", MeasureUnit.KIT,
                    BigDecimal.valueOf(52), BigDecimal.valueOf(400.00));

            ProductUpdateResponse response = new ProductUpdateResponse(
                    categoryName,productId,"MAQUITAATUALIZADA", "Maquita eletrica ATUALIZADA",
                    MeasureUnit.KIT,
                    BigDecimal.valueOf(52), BigDecimal.valueOf(400.00), "GUSTAVO");


            when(productRepository.findById(productId)).thenReturn(Optional.of(product));
            when(productRepository.existsByNameIgnoreCase(updateRequest.name())).thenReturn(false);
            when(productMapper.toProductUpdateResponse(product)).thenReturn(response);
            when(productRepository.save(any(Product.class))).thenReturn(product);

            ProductUpdateResponse output = productService.updateProduct(productId, updateRequest);

            assertNotNull(output);
            assertEquals(response, output);

            verify(productRepository).findById(productId);
            verify(productRepository).existsByNameIgnoreCase(updateRequest.name());
            verify(productMapper).toProductUpdateResponse(product);
            verify(productRepository).save(any(Product.class));

        }
    }

    @Nested
    @DisplayName("Should ative product with sucesso")
    class ActivateProduct {

        @Test
        void activateProduct() {

            Long productId = 1L;
            String name = "TV";
            String description = "TV descricao";
            MeasureUnit measureUnit = MeasureUnit.UNIDADE;
            BigDecimal availableQuantity = BigDecimal.valueOf(5);
            BigDecimal price = BigDecimal.valueOf(10);

            Product product = new Product(
                    name, description, measureUnit, availableQuantity, price);
            ReflectionTestUtils.setField(product, "id", productId);

            product.setActive(false);

            when(productRepository.findById(productId)).thenReturn(Optional.of(product));

            when(productRepository.save(any(Product.class))).thenReturn(product);

            productService.reactivateProduct(productId);

            verify(productRepository).save(product);

            assertTrue(product.getActive());

        }
    }

    @Nested
    @DisplayName("Should desative product with sucesso")
    class DesativeProduct {

        @Test
        void desativeProduct() {

            Long productId = 1L;
            String name = "TV";
            String description = "TV descricao";
            MeasureUnit measureUnit = MeasureUnit.UNIDADE;
            BigDecimal availableQuantity = BigDecimal.valueOf(5);
            BigDecimal price = BigDecimal.valueOf(10);

            Product product = new Product(
                    name, description, measureUnit, availableQuantity, price);
            ReflectionTestUtils.setField(product, "id", productId);

            product.setActive(true);

            when(productRepository.findById(productId)).thenReturn(Optional.of(product));

            when(productRepository.save(any(Product.class))).thenReturn(product);

            productService.desativeProduct(productId);

            verify(productRepository).save(product);

            assertFalse(product.getActive());

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

    @Nested
    @DisplayName("Should atualizeted stock with sucesso")
    class AtualizeStock {

        @Test
        void atualizeStock() throws StockOperationExceptionAddAndRemove, StockOperationExceptionSet, insuficienteStockException {

            Long productId = 1L;
            String name = "TV";
            String description = "TV descricao";
            MeasureUnit measureUnit = MeasureUnit.UNIDADE;
            BigDecimal availableQuantity = BigDecimal.valueOf(5);
            BigDecimal price = BigDecimal.valueOf(10);

            StockOperationType stockOperationType = StockOperationType.ADD;
            BigDecimal quantityMovimented = BigDecimal.valueOf(3);
            BigDecimal newQuantity = BigDecimal.valueOf(2);

            Product product = new Product(
                    name, description, measureUnit, availableQuantity, price);
            ReflectionTestUtils.setField(product, "id", productId);

            StockUpdateRequest request = new StockUpdateRequest(quantityMovimented, stockOperationType);

            StockUpdateResponse  stockUpdateResponse = new StockUpdateResponse(
                    productId, name,stockOperationType,quantityMovimented,newQuantity);

            when(productRepository.findById(productId)).thenReturn(Optional.of(product));
            when(productRepository.save(any(Product.class))).thenReturn(product);
            when(productMapper.toStockUpdateResponse(product,stockOperationType,quantityMovimented))
                    .thenReturn(stockUpdateResponse);

            StockUpdateResponse output = productService.updateStock(productId,request);

            assertNotNull(output);
            assertEquals(stockUpdateResponse,output);

            verify(productRepository).findById(productId);
            verify(productRepository).save(product);
            verify(productMapper).toStockUpdateResponse(product,stockOperationType,quantityMovimented);

        }
    }

}