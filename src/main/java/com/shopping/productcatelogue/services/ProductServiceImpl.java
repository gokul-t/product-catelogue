package com.shopping.productcatelogue.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.shopping.productcatelogue.domain.Product;
import com.shopping.productcatelogue.domain.QProduct;
import com.shopping.productcatelogue.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Page<Product> listProducts(String name, String size,
            PageRequest pageRequest) {
        QProduct qProduct = QProduct.product;
        List<BooleanExpression> booleanExpressionList = new ArrayList<>();
        if (Optional.ofNullable(name).isPresent()) {
            BooleanExpression productHasName = qProduct.name.contains(name);
            booleanExpressionList.add(productHasName);
        }
        if (Optional.ofNullable(size).isPresent()) {
            BooleanExpression productHasSize = qProduct.size.eq(size);
            booleanExpressionList.add(productHasSize);
        }
        Optional<BooleanExpression> joinedBooleanExpression = booleanExpressionList.stream()
                .reduce(BooleanExpression::and);
        if (joinedBooleanExpression.isPresent())
            return productRepository.findAll(joinedBooleanExpression.get(), pageRequest);
        return productRepository.findAll(pageRequest);
    }

    @Override
    public Optional<Product> getProductById(Long productId) {
        throw new UnsupportedOperationException("Unimplemented method 'getProductById'");
    }

}
