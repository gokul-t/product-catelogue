package com.shopping.productcatelogue.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.shopping.productcatelogue.domain.Product;

public interface ProductService {
    Page<Product> listProducts(String name, String size, PageRequest pageRequest);

    Optional<Product> getProductById(Long productId);
}
