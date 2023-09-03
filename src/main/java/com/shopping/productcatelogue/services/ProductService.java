package com.shopping.productcatelogue.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;

import com.shopping.productcatelogue.domain.Product;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

@Validated
public interface ProductService {

        Product saveProduct(@NotNull Product product);

        Optional<Product> updateProduct(@Min(1L) @Max(Long.MAX_VALUE) @NotNull Long productId, String name,
                        Integer quantity,
                        String size);

        void deleteProductById(@Min(1L) @Max(Long.MAX_VALUE) @NotNull Long productId);

        Optional<Product> getProductById(@Min(1L) @Max(Long.MAX_VALUE) @NotNull Long productId);

        Page<Product> listProducts(
                        String name,
                        @Pattern(regexp = "Large|Medium|Small", message = "{products.errors.invalid_size}") String size,
                        @PositiveOrZero() Integer pageNumber,
                        @Min(1) @Max(100) Integer pageSize,
                        @Pattern(regexp = "id|name|size|quantity", message = "{products.errors.invalid_sort_by}") String sortBy,
                        Sort.Direction sortDirection);

}
