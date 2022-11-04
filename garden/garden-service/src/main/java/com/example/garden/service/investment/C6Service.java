package com.example.garden.service.investment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
@Slf4j
public class C6Service {

    public void test(){
        //无风险收益
        BigDecimal rf=new BigDecimal(0.07);
        //期望风险收益
        BigDecimal rp=new BigDecimal(0.15);
        //方差
        BigDecimal o=new BigDecimal(0.22);
        //风险资产占比
        BigDecimal y=new BigDecimal(0.5);
        log.info("== 风险组合占比：{} ",y);
        //风险组合收益=(1-y)无风险收益+y*期望风险收益
        BigDecimal rc=(new BigDecimal(1).subtract(y).multiply(rf)).add(y.multiply(rp),new MathContext(4,RoundingMode.HALF_UP));
        log.info("== 风险组合收益：{} ",rc);
        //风险组合方差=y*风险资产方差
        BigDecimal oc=y.multiply(o,new MathContext(4,RoundingMode.HALF_UP));
        log.info("== 风险组合方差：{} ",oc);
        //夏普比率
        BigDecimal s=(rp.subtract(rf)).divide(o,4, RoundingMode.HALF_UP);
        log.info("== 夏普比率：{} ",s);

        y=new BigDecimal(1.4,new MathContext(4,RoundingMode.HALF_UP));
        log.info("== 带杠杠风险组合占比：{} ",y);
        rc=(new BigDecimal(1).subtract(y).multiply(rf)).add(y.multiply(rp),new MathContext(4,RoundingMode.HALF_UP));
        log.info("== 带杠杆风险组合收益：{} ",rc);
        oc=y.multiply(o,new MathContext(4,RoundingMode.HALF_UP));
        log.info("== 带杠杆风险组合方差：{} ",oc);
        s=(rp.subtract(rf)).divide(o,4, RoundingMode.HALF_UP);
        log.info("== 带方差夏普比率：{} ",s);
    }


    public static void main(String[] args) {
        C6Service c6Service=new C6Service();
        c6Service.test();
    }
}
