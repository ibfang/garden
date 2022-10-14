package com.example.garden.model.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockDTO {
    @ApiModelProperty(value = "股票增长率类型",example = "zero,varying,unvarying")
    private String  growthRateType;
    @ApiModelProperty(value = "股票增长率")
    private BigDecimal growthRate;
    @ApiModelProperty(value = "折现率")
    private BigDecimal rate;
    @ApiModelProperty(value = "未来股利")
    private BigDecimal div;
    private int[] yearScope;
}
