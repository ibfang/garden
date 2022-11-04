package com.example.garden.service.corporatefinance;

import com.example.garden.enums.StockCashFlowEnum;
import com.example.garden.model.DTO.StockDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
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

    /**
     * 通过股利模型估计股利
     * div是股利，折现的时候要折现股利，而不是折现利润，如果折现利润，会导致股价被高估
     * 因为利润中有一部分会被拿去再投资，这部分不应该被拿来计算股利
     * 另外投资者只能从股票中得到两种收益：股利和最终售价，而最终售价也依赖于投资者未来预期获得的股利
     * @param stockDTO
     * @return
     */
    public BigDecimal evaluateStockPriceByModel(StockDTO stockDTO){
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


    public BigDecimal evaluateStockPriceByCompare(){
        //每股股价
        BigDecimal p=new BigDecimal(0);
        //每股盈利
        BigDecimal eps=new BigDecimal(0);
        //市盈率=每股盈利/每股股价
        BigDecimal shiyinglv=eps.divide(p,4,RoundingMode.HALF_UP);
        return shiyinglv;
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
        c9Service.evaluateStockPriceByModel(stockDTO);
    }

    /**
     * 公司增长率
     * @param shouyi
     * @param liucun
     * @param roe
     * @return
     */
    public BigDecimal getG(BigDecimal shouyi,BigDecimal liucun, BigDecimal roe){
        //明年的盈利=今年的盈利+今年的留存收益*留存收益回报率。两边同除今年的盈利
        //明年的盈利/今年的盈利=1+盈利增长率g。今年的留存收益/今年的盈利=留存收益比率
        //留存收益回报率以往年的权益资本回报率来估计
        BigDecimal liucunRatio=liucun.divide(shouyi,4,RoundingMode.HALF_UP);
        BigDecimal g=liucunRatio.multiply(roe);
        //因为股利与盈余的比例相同，所以盈余增长率g的估计和股利增长率的估计是相同的
        return g;
    }

    /**
     * 估计增长率R

     * @return
     */
    public BigDecimal getR(){
        //TODO 13章
        return new BigDecimal(0);
    }

}
