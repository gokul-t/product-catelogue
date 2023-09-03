package com.shopping.productcatelogue.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.shopping.productcatelogue.domain.Product;
import com.shopping.productcatelogue.domain.QProduct;
import com.shopping.productcatelogue.repositories.ProductRepository;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
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
    public Optional<Product> updateProduct(Long id, String name, Integer quantity, String size) {
        return getProductById(id)
                .map(product -> {
                    product.setName(name);
                    product.setQuantity(quantity);
                    product.setSize(size);
                    return product;
                }).map(productRepository::save);
    }

    @Override
    public void deleteProductById(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    private Page<Product> listProducts(Optional<String> nameOptional,
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

    @Override
    public Page<Product> listProducts(String name,
            @Pattern(regexp = "Large|Medium|Small", message = "{products.errors.invalid_size}") String size,
            @PositiveOrZero Integer pageNumber, @Min(1) @Max(100) Integer pageSize,
            @Pattern(regexp = "id|name|size|quantity", message = "{products.errors.invalid_sort_by}") String sortBy,
            Direction sortDirection) {
        Sort sort = Sort.by(sortDirection, sortBy);
        return listProducts(Optional.ofNullable(name),
                Optional.ofNullable(size),
                PageRequest.of(pageNumber, pageSize, sort));
    }

}
