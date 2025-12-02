package com.gustavosdaniel.ecommerce_api.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    boolean existsByName(String name);

    boolean existsByNameIgnoreCase( String name);

    List<Category> findByNameContainingIgnoreCase(String name);
}
