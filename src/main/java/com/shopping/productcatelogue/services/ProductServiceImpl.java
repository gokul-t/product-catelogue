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
    public Page<Product> listProducts(String name, String size,
            PageRequest pageRequest) {
        return productRepository.findAllByNameAndSize(name, size, pageRequest);
    }

    @Override
    public Optional<Product> getProductById(Long productId) {
        throw new UnsupportedOperationException("Unimplemented method 'getProductById'");
    }

}
