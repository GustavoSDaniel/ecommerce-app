package com.gustavosdaniel.ecommerce_api.category;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Nested
    @DisplayName("Should create a category with success")
    class CreateCategory {

        @Test
        void createCategory() {

            Integer id = 1;
            String name = "Eletricos";
            String description = "Produtos eletricos de qualidade superior";

            Category category = new Category(name, description);
            ReflectionTestUtils.setField(category, "id", id);

            CategoryRequest request = new CategoryRequest(name, description);

            CategoryResponse response = new CategoryResponse(id, name, description, "Gustavo");

            when(categoryRepository.existsByName(name)).thenReturn(false);
            when(categoryMapper.toCategory(request)).thenReturn(category);
            when(categoryRepository.save(any(Category.class))).thenReturn(category);
            when(categoryMapper.toCategoryResponse(category)).thenReturn(response);

            CategoryResponse output = categoryService.createCategory(request);

            assertNotNull(output);
            assertEquals(id, output.id());
            assertEquals(name, output.name());
            assertEquals(description, output.description());
            assertEquals("Gustavo", output.createdBy());

            verify(categoryRepository).existsByName(name);
            verify(categoryRepository).save(category);
            verify(categoryMapper).toCategory(request);
            verify(categoryMapper).toCategoryResponse(category);

        }

        @Test
        void exceptionCreateCategory() {

            String name = "Eletricos";
            String description = "Produtos eletricos de qualidade superior";

            CategoryRequest request = new CategoryRequest(name, description);

            when(categoryRepository.existsByName(name)).thenReturn(true);

            assertThrows(NameCategoryExistException.class, () -> categoryService.createCategory(request));
        }
    }

}