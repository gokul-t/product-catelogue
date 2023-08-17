package com.shopping.productcatelogue.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping.productcatelogue.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
