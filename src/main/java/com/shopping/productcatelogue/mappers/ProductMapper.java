package com.shopping.productcatelogue.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.shopping.productcatelogue.domain.Product;
import com.shopping.productcatelogue.model.ProductDTO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = { DateMapper.class })
public interface ProductMapper {
    Product productDTOToProduct(ProductDTO productDTO);

    ProductDTO productToProductDTO(Product product);
}
