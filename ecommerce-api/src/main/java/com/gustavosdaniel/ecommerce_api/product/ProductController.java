package com.gustavosdaniel.ecommerce_api.product;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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
