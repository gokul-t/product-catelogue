package com.shopping.productcatelogue.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.shopping.productcatelogue.domain.Product;

public interface ProductService {
    Page<Product> listProducts(Optional<String> nameOptional,
            Optional<String> sizeOptional,
            PageRequest pageRequest);

    Optional<Product> getProductById(Long productId);

    Product saveProduct(Product product);

    Product updateProduct(Product product);

    void deleteProductById(Long productId);
}
