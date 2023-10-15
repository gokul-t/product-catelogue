package com.shopping.productcatelogue.web.model;

import com.shopping.productcatelogue.web.model.info.AdvanceInfo;
import com.shopping.productcatelogue.web.model.info.BasicInfo;

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
	@NotBlank(groups = { BasicInfo.class, AdvanceInfo.class })
	private String name;
	@NotNull(groups = { BasicInfo.class, AdvanceInfo.class })
	@Positive(groups = { BasicInfo.class, AdvanceInfo.class })
	private Integer quantity;
	@NotNull(groups = { BasicInfo.class, AdvanceInfo.class })
	@Pattern(regexp = "Large|Medium|Small", groups = { BasicInfo.class, AdvanceInfo.class })
	private String size;
}
