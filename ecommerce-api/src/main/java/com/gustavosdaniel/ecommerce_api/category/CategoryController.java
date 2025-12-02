package com.gustavosdaniel.ecommerce_api.category;

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
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;

    }

    @PostMapping("/category")
    @Operation(summary = "Cria categoria")
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestBody @Valid CategoryRequest categoryRequest) {

        CategoryResponse category = categoryService.createCategory(categoryRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build().toUri();

        return ResponseEntity.created(location).body(category);
    }

    @GetMapping
    @Operation(summary = "Mostra todas as categorias")
    public ResponseEntity<Page<CategoryResponse>> getAllCategories(
            @ParameterObject
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        Page<CategoryResponse> categories = categoryService.getCategories(pageable);

        return ResponseEntity.ok(categories);

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta categoria")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {

        categoryService.deleteCategory(id);

        return ResponseEntity.noContent().build();

    }
}
