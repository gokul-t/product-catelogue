package com.shopping.productcatelogue.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.shopping.productcatelogue.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByNameAndSize(
            @Param("name") String name,
            @Param("size") String size,
            PageRequest pageRequest);
}
