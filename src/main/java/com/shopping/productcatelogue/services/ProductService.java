package com.shopping.productcatelogue.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.shopping.productcatelogue.domain.Product;

public interface ProductService {
    Page<Product> listProducts(Optional<String> name, Optional<String> size, PageRequest pageRequest);

    Optional<Product> getProductById(Long productId);
}
