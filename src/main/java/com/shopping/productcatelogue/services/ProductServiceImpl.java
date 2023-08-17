package com.shopping.productcatelogue.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.shopping.productcatelogue.domain.Product;
import com.shopping.productcatelogue.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Page<Product> listProducts(Optional<String> name, Optional<String> size,
            PageRequest pageRequest) {
        return productRepository.findAll(pageRequest);
    }

    @Override
    public Optional<Product> getProductById(Long productId) {
        throw new UnsupportedOperationException("Unimplemented method 'getProductById'");
    }

}
