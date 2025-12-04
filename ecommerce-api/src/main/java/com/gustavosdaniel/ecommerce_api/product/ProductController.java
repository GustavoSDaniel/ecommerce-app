package com.gustavosdaniel.ecommerce_api.product;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/categories/{categoryId}")
    @Operation(summary = "Cria Produto")
    public ResponseEntity<ProductResponse> createProduct(
            @PathVariable Integer categoryId,
            @RequestBody @Valid ProductRequest request) {

        ProductResponse newProduct = productService.createProduct(categoryId, request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newProduct.id())
                .toUri();

        return ResponseEntity.created(location).body(newProduct);

    }

    @GetMapping
    @Operation(summary = "Mostra todos os produtos")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @ParameterObject
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable){

         Page<ProductResponse> products = productService.getAllProducts(pageable);

         return ResponseEntity.ok(products);
    }


    @GetMapping("/categories/{categoryId}/products")
    @Operation(summary = "Lista produtos vinculados a uma categoria específica")
    public ResponseEntity<Page<ProductResponse>> findAllProductsByCategoryId(
            @PathVariable Integer categoryId,
            @ParameterObject
            @PageableDefault(size = 20, sort = "name",direction = Sort.Direction.ASC )
            Pageable pageable) {

        Page<ProductResponse> products = productService.getAllProductsByCategoryId(categoryId, pageable);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca Produto através do ID")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {

        ProductResponse product = productService.getProductById(id);

        return ResponseEntity.ok(product);
    }

    @PatchMapping("{id}/activate")
    @Operation(summary = "Ativa produto")
    public ResponseEntity<Void> ativarProduct(@PathVariable Long id){

        productService.reactivateProduct(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desative")
    @Operation(summary = "Desativa produto")
    public ResponseEntity<Void> desativarProduto(@PathVariable Long id) {

        productService.desativeProduct(id);

        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta produto através do ID")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}
