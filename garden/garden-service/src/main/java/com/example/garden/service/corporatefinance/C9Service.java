package com.example.garden.service.corporatefinance;

import com.example.garden.enums.StockCashFlowEnum;
import com.example.garden.model.DTO.StockDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * target：对普通股进行股票估值
 */
@Service
@Slf4j
public class C9Service {
    //股票价格等于未来所有预期股利的现值

    public BigDecimal stockPrice(StockDTO stockDTO){
        //初始化加个
        BigDecimal price= new BigDecimal(0, MathContext.UNLIMITED);
        if(stockDTO.getGrowthRateType().equals(StockCashFlowEnum.ZERO.getKey())){
            log.info("\n零增长率 股价计算公式 p=Div/R , Div={},R={} \n",stockDTO.getDiv(),stockDTO.getRate());
            //零增长
            //保留4位小数，结果四舍五入
            price=stockDTO.getDiv().divide(stockDTO.getRate(),4,RoundingMode.HALF_UP);
        }else if(stockDTO.getGrowthRateType().equals(StockCashFlowEnum.UNVARYING.getKey())){
            //固定增长
            //r和g相等。分母无意义
            if(0==stockDTO.getRate().compareTo(stockDTO.getGrowthRate())){
                log.info("\n固定增长，增长率＝折现率 股价计算公式 p=Div/PIVF(r,5), Div={},R={},g={} \n",stockDTO.getDiv(),stockDTO.getRate(),stockDTO.getGrowthRate());
                //年份
                int year=stockDTO.getYearScope()[0];
                BigDecimal base=stockDTO.getRate().add(new BigDecimal(1));
                for(int i=0;i<year;i++){
                    //幂运算
                    BigDecimal numerator=stockDTO.getRate().add(new BigDecimal(1)).pow(i+1);
                    BigDecimal denominator=base.pow(i+1);
                    BigDecimal curPrice=numerator.divide(denominator,4,RoundingMode.HALF_UP);
                    price=price.add(curPrice);
                }
            }else {
                log.info("\n固定增长，增长率≠折现率 ,股价计算公式 p=Div/(R-g) , Div={},R={},g={}\n",stockDTO.getDiv(),stockDTO.getRate(),stockDTO.getGrowthRate());
                BigDecimal cha = stockDTO.getRate().subtract(stockDTO.getGrowthRate());
                price = stockDTO.getDiv().divide(cha, 4, RoundingMode.HALF_UP);
            }
        }else if (stockDTO.getGrowthRateType().equals(StockCashFlowEnum.VARYING.getKey())){
            //变动增长
        }
        log.info("\n股票价格： {} \n",price);
        return price;
    }


    public static void main(String[] args) {
        C9Service c9Service=new C9Service();
        StockDTO stockDTO=new StockDTO();
        stockDTO.setDiv(new BigDecimal(1));
        stockDTO.setRate(new BigDecimal(0.15));
        stockDTO.setGrowthRate(new BigDecimal(0.15));
        int [] yearArray=new int[]{5,6};
        stockDTO.setYearScope(yearArray);
        stockDTO.setGrowthRateType(StockCashFlowEnum.UNVARYING.getKey());
        c9Service.stockPrice(stockDTO);
    }

}
