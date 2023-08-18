package com.shopping.productcatelogue.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.shopping.productcatelogue.domain.Product;
import com.shopping.productcatelogue.web.model.ProductDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    Product productDtoToProduct(ProductDto productDto);

    ProductDto productToProductDto(Product product);
}
