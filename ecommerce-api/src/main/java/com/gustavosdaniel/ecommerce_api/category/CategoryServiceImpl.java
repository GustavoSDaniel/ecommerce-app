package com.gustavosdaniel.ecommerce_api.category;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "categories")
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final static Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }


    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public CategoryResponse createCategory(CategoryRequest request) {

        log.info("create Category");

        if (categoryRepository.existsByNameIgnoreCase(request.name())){

            throw new NameCategoryExistException();
        }

        Category newCategory = categoryMapper.toCategory(request);

        Category savedCategory = categoryRepository.save(newCategory);

        log.info("created Category {} com sucesso", savedCategory.getName());

        return categoryMapper.toCategoryResponse(savedCategory);
    }

    @Override
    @Cacheable(key = "'categories-page' + #pageable.pageNumber + '-size' + #pageable.pageSize")
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<CategoryResponse> getCategories(Pageable pageable) {

        log.info("Buscando categorias");

        Page<Category> categories = categoryRepository.findAll(pageable);

        if (categories.isEmpty()){

            log.info("Nenhuma categoria encontrada");
            return Page.empty();
        }

        log.info("Categorias encontradas com sucesso {}", categories.getNumberOfElements());

        return categories.map(categoryMapper::toCategoryResponse);
    }

    @Override
    @Cacheable(key = "#id")
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Integer id) {

        log.info("Buscando categoria {}", id);

        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);

        log.info("Categoria encontrada com sucesso {}", category.getName());

        return categoryMapper.toCategoryResponse(category);

    }

    @Override
    @Cacheable(key = "'category-search-' + #root.target.normalizeSearchTerm(#name)")
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<CategoryResponse> searchCategories(String name) {

        if (name == null || name.isBlank()){
            log.warn("Tentativa de busca com nome vazio");

            return List.of();
        }

        log.info("Buscando categorias {}", name);

        List<Category> categories = categoryRepository.findByNameContainingIgnoreCase(name);

        if (categories.isEmpty()){

            log.info("Nenhuma categoria com esse nome {} encontrada", name);

            return List.of();
        }

        log.info("Categorias encontradas com sucesso {}", categories.size());

        return categories.stream().map(categoryMapper::toCategoryResponse).collect(Collectors.toList());

    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(key = "#id"),
            @CacheEvict(allEntries = true)
    })
    public CategoryUpdateResponse updateCategory(Integer id, CategoryUpdateRequest request) {

        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);


        if (request.name() != null && !request.name().isBlank()){

            if (!category.getName().equalsIgnoreCase(request.name()) &&
                    categoryRepository.existsByNameIgnoreCase(request.name())){
                log.info("Tentativa de atualizar categoria com nome já existente: {}", request.name());
                throw new NameCategoryExistException();
            }
        }

        categoryMapper.toUpdateCategory(request,category);

        Category savedCategory = categoryRepository.save(category);

        log.info("Categoria atualizada com sucesso: {}", savedCategory.getName());

        return categoryMapper.toUpdateCategoryResponse(savedCategory);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(key = "#id"),
            @CacheEvict(allEntries = true)
    })
    public void deleteCategory(Integer id) {

        log.info("delete Category");

        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);

        log.info("delete Category {} com sucesso", category.getName());

        categoryRepository.delete(category);

    }
}
