package com.shopping.productcatelogue.web.controller;

import java.net.URI;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.shopping.productcatelogue.web.model.PagedList;
import com.shopping.productcatelogue.web.model.ProductDto;
import com.shopping.productcatelogue.web.model.info.AdvanceInfo;
import com.shopping.productcatelogue.web.model.info.BasicInfo;
import com.shopping.productcatelogue.web.utils.WebUtils;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(exposedHeaders = { "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "Location" })
public class ProductController {

        private final ProductService productService;
        private final ProductMapper productMapper;
        private final WebUtils webUtils;

        @PostMapping
        public ResponseEntity<URI> saveProduct(
                        @Validated(BasicInfo.class) @NotNull @RequestBody ProductDto productDto) {
                return Optional.of(productDto)
                                .map(productMapper::productDtoToProduct)
                                .map(productService::saveProduct)
                                .map(Product::getId)
                                .map(id -> ServletUriComponentsBuilder.fromCurrentRequest().path("/{productId}")
                                                .buildAndExpand(id)
                                                .toUri())
                                .map(uri -> ResponseEntity.created(uri).<URI>build())
                                .orElseThrow(productNotValidException(productDto));
        }

        private Supplier<ResponseStatusException> productNotValidException(ProductDto productDto) {
                log.debug("Product Not Valid: {}", productDto.toString());
                return webUtils.notValidException("products.errors.not_valid");
        }

        @PutMapping("/{productId}")
        public ResponseEntity<Void> updateProduct(@PathVariable Long productId,
                        @Validated(AdvanceInfo.class) @NotNull @RequestBody ProductDto productDto) {
                return productService
                                .updateProduct(productId, productDto.getName(), productDto.getQuantity(),
                                                productDto.getSize())
                                .map(pdt -> ResponseEntity.noContent().<Void>build())
                                .orElseThrow(productNotFoundException(productId));
        }

        @DeleteMapping("/{productId}")
        public ResponseEntity<Void> deleteProductById(
                        @PathVariable Long productId) {
                return productService.getProductById(productId)
                                .map(Product::getId)
                                .map(id -> {
                                        productService.deleteProductById(id);
                                        return ResponseEntity.noContent().<Void>build();
                                })
                                .orElseThrow(productNotFoundException(productId));
        }

        @GetMapping("/{productId}")
        public ResponseEntity<ProductDto> getProductById(
                        @PathVariable Long productId) {
                return productService.getProductById(productId).map(productMapper::productToProductDto)
                                .map(ResponseEntity::ok)
                                .orElseThrow(productNotFoundException(productId));
        }

        private Supplier<ResponseStatusException> productNotFoundException(Long productId) {
                log.debug("Product id Not Found: {}", productId.toString());
                return webUtils.notFoundException(
                                "products.errors.not_found", new Object[] { productId });
        }

        @GetMapping
        public ResponseEntity<PagedList<ProductDto>> listProducts(
                        @RequestParam(required = false) String name,
                        @RequestParam(required = false) String size,
                        @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                        @RequestParam(required = false, defaultValue = "25") Integer pageSize,
                        @RequestParam(required = false, defaultValue = "id") String sortBy,
                        @RequestParam(required = false, defaultValue = "ASC") Sort.Direction sortDirection) {
                Page<Product> productPage = productService.listProducts(name, size, pageNumber, pageSize, sortBy,
                                sortDirection);
                PagedList<ProductDto> productPagedList = PagedList.getPagedList(productPage,
                                productMapper::productToProductDto);
                return new ResponseEntity<>(productPagedList, HttpStatus.OK);
        }

}
