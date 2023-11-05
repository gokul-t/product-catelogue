package com.shopping.productcatelogue.web.controllers;

import java.net.URI;
import java.util.Optional;
import java.util.function.Supplier;

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

import com.shopping.productcatelogue.model.PagedList;
import com.shopping.productcatelogue.model.ProductDTO;
import com.shopping.productcatelogue.model.info.AdvanceInfo;
import com.shopping.productcatelogue.model.info.BasicInfo;
import com.shopping.productcatelogue.services.ProductService;
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
        private final WebUtils webUtils;

        @PostMapping
        public ResponseEntity<URI> saveProduct(@NotNull @Validated(BasicInfo.class)
                        @RequestBody ProductDTO productDTO) {
                return Optional.of(productDTO)
                                .map(productService::saveProduct)
                                .map(ProductDTO::getId)
                                .map(id -> ServletUriComponentsBuilder.fromCurrentRequest().path("/{productId}")
                                                .buildAndExpand(id)
                                                .toUri())
                                .map(uri -> ResponseEntity.created(uri).<URI>build())
                                .orElseThrow(productNotValidException(productDTO));
        }

        private Supplier<ResponseStatusException> productNotValidException(ProductDTO productDTO) {
                log.debug("Product Not Valid: {}", productDTO.toString());
                return webUtils.notValidException("products.errors.not_valid");
        }

        @PutMapping("/{productId}")
        public ResponseEntity<Void> updateProduct(@PathVariable Long productId, @NotNull @Validated(AdvanceInfo.class) @RequestBody ProductDTO productDTO) {
                productDTO.setId(productId);
                return productService
                                .updateProduct(productDTO)
                                .map(pdt -> ResponseEntity.noContent().<Void>build())
                                .orElseThrow(productNotFoundException(productId));
        }

        @DeleteMapping("/{productId}")
        public ResponseEntity<Void> deleteProductById(
                        @PathVariable Long productId) {
                return productService.deleteProductById(productId)
                                .map(pdt -> ResponseEntity.noContent().<Void>build())
                                .orElseThrow(productNotFoundException(productId));
        }

        @GetMapping("/{productId}")
        public ResponseEntity<ProductDTO> getProductById(
                        @PathVariable Long productId) {
                return productService.getProductById(productId)
                                .map(ResponseEntity::ok)
                                .orElseThrow(productNotFoundException(productId));
        }

        private Supplier<ResponseStatusException> productNotFoundException(Long productId) {
                log.debug("Product id Not Found: {}", productId.toString());
                return webUtils.notFoundException(
                                "products.errors.not_found", new Object[] { productId });
        }

        @GetMapping
        public ResponseEntity<PagedList<ProductDTO>> listProducts(
                        @RequestParam(required = false) String name,
                        @RequestParam(required = false) String size,
                        @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                        @RequestParam(required = false, defaultValue = "25") Integer pageSize,
                        @RequestParam(required = false, defaultValue = "id") String sortBy,
                        @RequestParam(required = false, defaultValue = "ASC") Sort.Direction sortDirection) {
                PagedList<ProductDTO> productPagedList = productService.listProducts(name, size, pageNumber, pageSize,
                                sortBy,
                                sortDirection);
                return new ResponseEntity<>(productPagedList, HttpStatus.OK);
        }

}
