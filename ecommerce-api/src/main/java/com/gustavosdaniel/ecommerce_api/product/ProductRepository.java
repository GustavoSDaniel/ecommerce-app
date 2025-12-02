package com.gustavosdaniel.ecommerce_api.product;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName( String name);

    boolean existsByNameIgnoreCase( String name);
}
