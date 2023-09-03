package com.shopping.productcatelogue.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.shopping.productcatelogue.domain.Product;
import com.shopping.productcatelogue.domain.QProduct;
import com.shopping.productcatelogue.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProductById(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public Page<Product> listProducts(Optional<String> nameOptional,
            Optional<String> sizeOptional,
            PageRequest pageRequest) {
        QProduct qProduct = QProduct.product;
        List<BooleanExpression> booleanExpressionList = new ArrayList<>();
        nameOptional.ifPresent(name -> {
            BooleanExpression productHasName = qProduct.name.contains(name);
            booleanExpressionList.add(productHasName);
        });
        sizeOptional.ifPresent(size -> {
            BooleanExpression productHasSize = qProduct.size.eq(size);
            booleanExpressionList.add(productHasSize);
        });
        return booleanExpressionList.stream()
                .reduce(BooleanExpression::and)
                .map(joined -> productRepository.findAll(joined, pageRequest))
                .orElseGet(() -> productRepository.findAll(pageRequest));
    }

}
