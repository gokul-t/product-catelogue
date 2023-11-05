package com.shopping.productcatelogue.model;

import com.shopping.productcatelogue.model.info.AdvanceInfo;
import com.shopping.productcatelogue.model.info.BasicInfo;

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
public class ProductDTO extends BaseDTO {
	@NotBlank(groups = { BasicInfo.class, AdvanceInfo.class })
	private String name;
	@NotNull(groups = { BasicInfo.class, AdvanceInfo.class })
	@Positive(groups = { BasicInfo.class, AdvanceInfo.class })
	private Integer quantity;
	@NotNull(groups = { BasicInfo.class, AdvanceInfo.class })
	@Pattern(regexp = "Large|Medium|Small", groups = { BasicInfo.class, AdvanceInfo.class })
	private String size;
}
