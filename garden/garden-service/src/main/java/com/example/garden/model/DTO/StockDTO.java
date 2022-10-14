package com.example.garden.model.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StockDTO {
    @ApiModelProperty(value = "股票增长率类型",example = "zero,varying,unvarying")
    private String  growthRate;
}
