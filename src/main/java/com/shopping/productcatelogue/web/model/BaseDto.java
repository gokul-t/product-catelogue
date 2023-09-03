package com.shopping.productcatelogue.web.model;

import java.time.OffsetDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @Null(groups = CreateInfo.class)
    @Min(value = 1L, groups = UpdateInfo.class) 
    @Max(value = Long.MAX_VALUE, groups = UpdateInfo.class)
    private Long id;
    @Null(groups = CreateInfo.class)
    private OffsetDateTime createdAt;
    @Null(groups = CreateInfo.class)
    private OffsetDateTime updatedAt;
}