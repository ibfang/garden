package com.example.garden.service.investment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class C8Service {
    /**
     * 指数模型 1、简化协方差的计算 2、强化风险溢价的估算
     * 方法：分散风险系统风险和公司特有风险
     */


    /**
     * 任何证券i的收益率分解为期望收益率和非期望部分之和 ri=E(ri)+ei+betaI*m
     * @param betaI 证券i对宏观经济条件的敏感性系数
     * @param eRi 期望收益率
     * @param ei  非期望收益率 公司层面的，和影响整个经济的宏观因素m独立
     * @return
     */
    public BigDecimal ri(BigDecimal betaI,BigDecimal eRi,BigDecimal ei){
        //宏观经济因素m用来度量未预期的宏观突发事件，它的均值为0，标准差为detaM
        BigDecimal m=new BigDecimal(0);
        return eRi.add(ei).add(m.multiply(betaI));
    }

    /**
     * ri的方差= detaM^2+detaEi^2
     * @param betaI 证券i对宏观经济条件的敏感性系数
     * @param detaM 系统的
     * @param detaEi 公司的
     * @return
     */
    public BigDecimal detaSquared(BigDecimal betaI,BigDecimal detaM,BigDecimal detaEi){
        BigDecimal temp=detaM.multiply(betaI);
        return (temp.pow(2)).add(detaEi.pow(2));
    }

    /**
     * 两个证券证券质检的协方差=相关系数*detaI*detaJ
     * @param m
     * @param ei
     * @param ej
     * @return
     */
    public BigDecimal covarianceIJ(BigDecimal m,BigDecimal ei,BigDecimal ej){
        return new BigDecimal(0);
    }
}
