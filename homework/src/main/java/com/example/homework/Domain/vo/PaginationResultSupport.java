package com.example.homework.Domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class PaginationResultSupport {

	/**
	 * 总记录数
	 */
	@ApiModelProperty(value = "总记录数",example="0")
	private long total = 0;

	/**
	 * 总页数
	 */
	@ApiModelProperty(value = "总页数",example="0")
	private int totalPages = 0;
}
