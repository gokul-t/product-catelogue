package com.shopping.productcatelogue.web.mappers;

import org.mapstruct.Mapper;

import com.shopping.productcatelogue.domain.Product;
import com.shopping.productcatelogue.web.model.ProductDto;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product productDtoToProduct(ProductDto productDto);

    ProductDto productToProductDto(Product product);
}
