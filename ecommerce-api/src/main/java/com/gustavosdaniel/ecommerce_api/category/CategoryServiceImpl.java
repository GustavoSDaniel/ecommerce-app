package com.gustavosdaniel.ecommerce_api.category;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
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
    public CategoryResponse createCategory(CategoryRequest request) {

        log.info("create Category");

        if (categoryRepository.existsByName(request.name())){

            throw new NameCategoryExistException();
        }

        Category newCategory = categoryMapper.toCategory(request);

        Category savedCategory = categoryRepository.save(newCategory);

        log.info("created Category {} com sucesso", savedCategory.getName());

        return categoryMapper.toCategoryResponse(savedCategory);
    }

    @Override
    public void deleteCategory(Integer id) {

        log.info("delete Category");

        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);

        log.info("delete Category {} com sucesso", category.getName());

        categoryRepository.delete(category);

    }
}
