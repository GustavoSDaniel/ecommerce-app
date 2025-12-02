package com.gustavosdaniel.ecommerce_api.category;

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

    @Nested
    @DisplayName("Should delete a category with success")
    class DeleteCategory {

        @Test
        void deleteCategory() {

            Integer id = 1;
            String name = "Eletricos";
            String description = "Produtos eletricos de qualidade superior";

            Category category = new Category(name, description);
            ReflectionTestUtils.setField(category, "id", id);

            when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

            categoryService.deleteCategory(id);

            verify(categoryRepository).findById(id);
            verify(categoryRepository).delete(category);
        }

        @Test
        void exceptionDeleteCategory() {

            Integer id = 1;
            String name = "Eletricos";
            String description = "Produtos eletricos de qualidade superior";

            Category category = new Category(name, description);
            ReflectionTestUtils.setField(category, "id", id);

            when(categoryRepository.findById(id)).thenReturn(Optional.empty());

            assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(id));

        }
    }

    @Nested
    @DisplayName("Should all categories with sucesso")
    class GetAllCategories {

        @Test
        void getAllCategories() {

            Pageable pageable = PageRequest.of(0, 10);

            Integer id = 1;
            Integer id2 = 2;
            Integer id3 = 3;

            Category category = new Category(
                    "Eletricos", "Produtos eletricos de qualidade superior");
            ReflectionTestUtils.setField(category, "id", id);

            Category category1 = new Category(
                    "Roupas", "Produtos roupas de qualidade superior");
            ReflectionTestUtils.setField(category1, "id", id2);

            Category category2 = new Category("Casa", "Produtos casa superior");
            ReflectionTestUtils.setField(category2, "id", id3);

            List<Category> categories = Arrays.asList(category,category2, category1);

            Page<Category> categoriesPage = new PageImpl<>(categories, pageable, categories.size());

            CategoryResponse response = new CategoryResponse(
                    id,"Eletricos",
                    "Produtos roupas de qualidade superior", "Gustavo");

            CategoryResponse response2 = new CategoryResponse(
                    id2,"Roupas", "Produtos casa superior", "Gustavo");

            CategoryResponse response3 = new CategoryResponse(
                    id3,"Casa", "Produtos casa superior", "Gustavo");

            when(categoryRepository.findAll(pageable)).thenReturn(categoriesPage);
            when(categoryMapper.toCategoryResponse(category)).thenReturn(response);
            when(categoryMapper.toCategoryResponse(category1)).thenReturn(response2);
            when(categoryMapper.toCategoryResponse(category2)).thenReturn(response3);

            Page<CategoryResponse> output = categoryService.getCategories(pageable);

            assertNotNull(output);

            verify(categoryRepository).findAll(pageable);
            verify(categoryMapper, times(3)).toCategoryResponse(any(Category.class));


        }
    }

    @Nested
    @DisplayName("Should update category with sucesso")
    class UpdateCategory {


        @Test
        void updateCategory() {

            Integer id = 1;
            String name = "Eletricos";
            String description = "Produtos eletricos de qualidade superior";

            String nomeUpdate = "EletricosAtualizado";
            String descriptionUpdate = "Produtos roupas de qualidade superior ATUALIZADO";
            String lastModifiedBy = "Atualizado By Gustavo";

            Category category = new Category(name, description);
            ReflectionTestUtils.setField(category, "id", id);

            CategoryUpdateRequest request = new CategoryUpdateRequest(nomeUpdate, descriptionUpdate);

            CategoryUpdateResponse response = new CategoryUpdateResponse(
                    id,nomeUpdate, descriptionUpdate, lastModifiedBy);

            when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
            when(categoryRepository.save(any(Category.class))).thenReturn(category);
            when(categoryMapper.toUpdateCategoryResponse(category)).thenReturn(response);

            CategoryUpdateResponse output = categoryService.updateCategory(id, request);

            assertNotNull(output);
            assertEquals(response, output);

            verify(categoryRepository).findById(id);
            verify(categoryRepository).save(any(Category.class));
            verify(categoryMapper).toUpdateCategoryResponse(category);

        }
    }

}