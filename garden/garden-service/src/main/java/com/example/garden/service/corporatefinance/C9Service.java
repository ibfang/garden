package com.example.garden.service.corporatefinance;

import com.example.garden.enums.StockCashFlowEnum;
import com.example.garden.model.DTO.StockDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * target：对普通股进行股票估值
 */
@Service
public class C9Service {
    //股票价格等于未来所有预期股利的现值

    public BigDecimal stockPrice(StockDTO stockDTO){
        if(stockDTO.getGrowthRate().equals(StockCashFlowEnum.ZERO.getKey())){

        }
        return new BigDecimal(0);
    }
}
