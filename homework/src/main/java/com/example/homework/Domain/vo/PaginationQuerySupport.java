package com.example.homework.Domain.vo;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@ApiModel
@Data
public class PaginationQuerySupportImpl implements PaginationQuerySupport {
	/**
	 * 第几页
	 */
	@ApiParam(value = "第几页", example = "1", required = true)
	@Min(value = 1, message = "最小值1")
	private int page;
	/**
	 * 每页记录数
	 */
	@ApiParam(value = "每页记录数", example = "10", required = true)
	@Min(value = 1, message = "最小值1")
	@Max(value = 100, message = "最小值100")
	private int pageSize;
	/*@ApiParam(value = "排序字段")
	@Pattern(regexp = "[a-zA-Z0-9_]+|", message = "无效的排序字段")
	private String sortname;

	@ApiParam(value = "升序 OR 降序")
	@Pattern(regexp = "ASC|DESC|asc|desc|", message = "无效的排序值")
	private String sortorder;

	public String getOrderBy() {
		return sortname == null ? "" : sortname + " " + sortorder;
	}*/

}
