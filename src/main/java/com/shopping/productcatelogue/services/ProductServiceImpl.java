package com.shopping.productcatelogue.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.shopping.productcatelogue.domain.Product;
import com.shopping.productcatelogue.domain.QProduct;
import com.shopping.productcatelogue.mappers.ProductMapper;
import com.shopping.productcatelogue.model.PagedList;
import com.shopping.productcatelogue.model.ProductDTO;
import com.shopping.productcatelogue.repositories.ProductRepository;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Validated
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Override
    public ProductDTO saveProduct(ProductDTO productDTO) {
        Product product = productMapper.productDTOToProduct(productDTO);
        productRepository.save(product);
        return productMapper.productToProductDTO(product);
    }

    @Override
    public Optional<ProductDTO> updateProduct(ProductDTO productDTO) {
        return getProductById(productDTO.getId())
                .map(pdt -> productMapper.productDTOToProduct(productDTO))
                .map(productRepository::save)
                .map(productMapper::productToProductDTO);
    }

    @Override
    public Optional<ProductDTO> deleteProductById(@Min(1L) @Max(Long.MAX_VALUE) @NotNull Long productId) {
        return getProductById(productId)
                .map(productDTO -> {
                    productRepository.deleteById(productDTO.getId());
                    return productDTO;
                });
    }

    @Override
    public Optional<ProductDTO> getProductById(@Min(1L) @Max(Long.MAX_VALUE) @NotNull Long productId) {
        return productRepository.findById(productId).map(productMapper::productToProductDTO);
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
    public PagedList<ProductDTO> listProducts(String name,
            @Pattern(regexp = "Large|Medium|Small", message = "{products.errors.invalid_size}") String size,
            @PositiveOrZero Integer pageNumber, @Min(1) @Max(100) Integer pageSize,
            @Pattern(regexp = "id|name|size|quantity", message = "{products.errors.invalid_sort_by}") String sortBy,
            Direction sortDirection) {
        Sort sort = Sort.by(sortDirection, sortBy);

        Page<Product> productPage = listProducts(Optional.ofNullable(name),
                Optional.ofNullable(size),
                PageRequest.of(pageNumber, pageSize, sort));

        return PagedList.getPagedList(productPage,
                productMapper::productToProductDTO);
    }

}
