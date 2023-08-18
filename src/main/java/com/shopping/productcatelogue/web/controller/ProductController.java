package com.shopping.productcatelogue.web.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.productcatelogue.domain.Product;
import com.shopping.productcatelogue.services.ProductService;
import com.shopping.productcatelogue.web.mappers.ProductMapper;
import com.shopping.productcatelogue.web.model.PagedList;
import com.shopping.productcatelogue.web.model.ProductDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<PagedList<ProductDto>> listProducts(
            @Valid @RequestParam(required = false) String name,
            @Valid @RequestParam(required = false) String size,
            @Valid @PositiveOrZero() @RequestParam(required = false) Integer pageNumber,
            @Valid @RequestParam(required = false) Integer pageSize,
            @Valid @RequestParam(required = false) String sortBy,
            @Valid @RequestParam(required = false) Sort.Direction sortDirection) {
        pageNumber = Optional.ofNullable(pageNumber).orElse(DEFAULT_PAGE_NUMBER);
        pageSize = Optional.ofNullable(pageSize).orElse(DEFAULT_PAGE_SIZE);
        sortBy = Optional.ofNullable(sortBy).orElse("name");
        sortDirection = Optional.ofNullable(sortDirection).orElse(Sort.Direction.ASC);
        Sort sort = Sort.by(sortDirection, sortBy);
        Page<Product> productPage = productService.listProducts(name, size,
                PageRequest.of(pageNumber, pageSize, sort));
        PagedList<ProductDto> productPagedList = PagedList.getPagedList(productPage,
                productMapper::productToProductDto);
        return new ResponseEntity<PagedList<ProductDto>>(productPagedList, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public String getProductsById() {
        return "hii";
    }
}
