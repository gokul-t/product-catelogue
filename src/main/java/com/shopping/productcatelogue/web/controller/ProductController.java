package com.shopping.productcatelogue.web.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.productcatelogue.domain.Product;
import com.shopping.productcatelogue.services.ProductService;
import com.shopping.productcatelogue.web.mappers.ProductMapper;
import com.shopping.productcatelogue.web.model.PagedList;
import com.shopping.productcatelogue.web.model.ProductDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<PagedList<ProductDto>> listProducts(
            @Valid @RequestParam(required = false) String name,
            @Valid @Pattern(regexp = "Large|Medium|Small", message = "Invalid size") @RequestParam(required = false) String size,
            @Valid @PositiveOrZero() @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @Valid @Min(1) @Max(100) @RequestParam(required = false, defaultValue = "25") Integer pageSize,
            @Valid @Pattern(regexp = "id|name|size|quantity", message = "Invalid sort by") @RequestParam(required = false, defaultValue = "id") String sortBy,
            @Valid @RequestParam(required = false, defaultValue = "ASC") Sort.Direction sortDirection) {
        Sort sort = Sort.by(sortDirection, sortBy);
        Page<Product> productPage = productService.listProducts(name, size,
                PageRequest.of(pageNumber, pageSize, sort));
        PagedList<ProductDto> productPagedList = PagedList.getPagedList(productPage,
                productMapper::productToProductDto);
        return new ResponseEntity<>(productPagedList, HttpStatus.OK);
    }


    @GetMapping("/{productId}")
    public String getProductsById(@Valid @Min(1L) @PathVariable Long productId) {
        log.info("Hello, World");
        return "Hello, World";
    }
}
