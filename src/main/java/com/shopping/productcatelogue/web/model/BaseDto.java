package com.shopping.productcatelogue.web.model;

import java.time.OffsetDateTime;

import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseDto {
    @Null
    private Long id;
    @Null
    private OffsetDateTime createdAt;
    @Null
    private OffsetDateTime updatedAt;
}