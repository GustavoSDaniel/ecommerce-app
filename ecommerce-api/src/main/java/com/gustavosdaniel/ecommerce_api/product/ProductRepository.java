package com.gustavosdaniel.ecommerce_api.product;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategory_Id(Integer id, Pageable pageable);

    boolean existsByNameIgnoreCase( String name);

    Page<Product> findActiveProducts(Pageable pageable);}
