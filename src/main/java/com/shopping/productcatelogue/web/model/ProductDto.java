package com.shopping.productcatelogue.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductDto extends BaseDto {
	@NotBlank(groups = { CreateInfo.class, UpdateInfo.class })
	private String name;
	@NotNull(groups = { CreateInfo.class, UpdateInfo.class })
	@Positive(groups = { CreateInfo.class, UpdateInfo.class })
	private Integer quantity;
	@NotNull(groups = { CreateInfo.class, UpdateInfo.class })
	@Pattern(regexp = "Large|Medium|Small", groups = { CreateInfo.class, UpdateInfo.class })
	private String size;
}
