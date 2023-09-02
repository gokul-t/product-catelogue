package com.shopping.productcatelogue.web.controller;

import java.net.URI;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.shopping.productcatelogue.domain.Product;
import com.shopping.productcatelogue.services.ProductService;
import com.shopping.productcatelogue.web.mappers.ProductMapper;
import com.shopping.productcatelogue.web.model.CreateInfo;
import com.shopping.productcatelogue.web.model.PagedList;
import com.shopping.productcatelogue.web.model.ProductDto;
import com.shopping.productcatelogue.web.model.UpdateInfo;
import com.shopping.productcatelogue.web.utils.WebUtils;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
        private final WebUtils webUtils;

        private Supplier<ResponseStatusException> productNotFoundException(Long productId) {
                log.debug("Product id Not Found: {}", productId.toString());
                return webUtils.notFoundException(
                                "products.errors.not_found", new Object[] { productId });
        }

        @GetMapping
        public ResponseEntity<PagedList<ProductDto>> listProducts(
                        @RequestParam(required = false) String name,
                        @Pattern(regexp = "Large|Medium|Small", message = "{products.errors.invalid_size}") @RequestParam(required = false) String size,
                        @PositiveOrZero() @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                        @Min(1) @Max(100) @RequestParam(required = false, defaultValue = "25") Integer pageSize,
                        @Pattern(regexp = "id|name|size|quantity", message = "{products.errors.invalid_sort_by}") @RequestParam(required = false, defaultValue = "id") String sortBy,
                        @RequestParam(required = false, defaultValue = "ASC") Sort.Direction sortDirection) {
                Sort sort = Sort.by(sortDirection, sortBy);
                Page<Product> productPage = productService.listProducts(Optional.ofNullable(name),
                                Optional.ofNullable(size),
                                PageRequest.of(pageNumber, pageSize, sort));
                PagedList<ProductDto> productPagedList = PagedList.getPagedList(productPage,
                                productMapper::productToProductDto);
                return new ResponseEntity<>(productPagedList, HttpStatus.OK);
        }

        @GetMapping("/{productId}")
        public ResponseEntity<ProductDto> getProductById(
                        @Min(1L) @Max(Long.MAX_VALUE) @PathVariable Long productId) {
                return productService.getProductById(productId).map(productMapper::productToProductDto)
                                .map(ResponseEntity::ok)
                                .orElseThrow(productNotFoundException(productId));
        }

        @PostMapping
        public ResponseEntity<URI> saveProduct(
                        @Validated(CreateInfo.class) @NotNull @RequestBody ProductDto productDto) {
                Product product = Optional.of(productDto)
                                .map(productMapper::productDtoToProduct)
                                .map(productService::saveProduct)
                                .orElseThrow(webUtils.notValidException("products.errors.not_valid"));
                URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{productId}")
                                .buildAndExpand(product.getId())
                                .toUri();

                return ResponseEntity.created(uri).build();
        }

        @PutMapping("/{productId}")
        public ResponseEntity<Void> updateProduct(@Min(1L) @Max(Long.MAX_VALUE) @PathVariable Long productId,
                        @Validated(UpdateInfo.class) @NotNull @RequestBody ProductDto productDto) {
                productDto.setId(productId);
                return productService.getProductById(productId)
                                .map(pdt -> productDto)
                                .map(productMapper::productDtoToProduct)
                                .map(productService::updateProduct)
                                .map(pdt -> ResponseEntity.noContent().<Void>build())
                                .orElseThrow(productNotFoundException(productId));
        }

        @DeleteMapping("/{productId}")
        public ResponseEntity<Void> deleteProductById(
                        @Min(1L) @Max(Long.MAX_VALUE) @PathVariable Long productId) {
                return productService.getProductById(productId)
                                .map(Product::getId)
                                .map(id -> {
                                        productService.deleteProductById(id);
                                        return ResponseEntity.noContent().<Void>build();
                                })
                                .orElseThrow(productNotFoundException(productId));
        }

}
