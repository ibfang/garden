package com.example.garden.service.investment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
@Slf4j
public class C7Service {
    /**
     * 最优风险资产组合
     * 1、资产配置决定了风险敞口
     * 2、最后风险资产配置取决于用户的风险厌恶程度和风险资产收益的预期
     * 7-1：分散化和组合风险
     * (1)无论持有多少种证券都无法避免商业周期的风险敞口
     * (2)无法消除的风险叫市场风险/系统性风险/不可分散的风险
     * (3)可以消除的风险叫公司特有风险/独特风险/可分散的风险
     *
     * 第7章的缺点：
     * 1、模型需要大量的估计数据来计算协方差
     * 2、风险溢价没法预测，这是计算有效边界必须的数据，所以这个缺陷非常的严重
     */


    /**
     * 7-2：两个风险资产的组合
     * target 给定任意期望收益可以构造最低风险的风险资产组合
     */
    public static void main(String[] args) {
        C7Service c7Service = new C7Service();

        double[] wd = new double[]{0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6,0.63,0.64, 0.7, 0.8, 0.9, 1.00,0.82,0.7};
        //债券期望收益
        BigDecimal eDebenture = new BigDecimal(8).setScale(2, RoundingMode.HALF_UP);
        //股票期望收益
        BigDecimal eEquity = new BigDecimal(13).setScale(2, RoundingMode.HALF_UP);

        //债券标准差
        BigDecimal stdDebenture = new BigDecimal(12).setScale(2, RoundingMode.HALF_UP);
        //债券标准差
        BigDecimal stdEquity = new BigDecimal(20).setScale(2, RoundingMode.HALF_UP);

        //债券收益率 15%
        BigDecimal rDebenture = new BigDecimal(0.15).setScale(2, RoundingMode.HALF_UP);
        //股票收益率 30%
        BigDecimal rEquity = new BigDecimal(0.3).setScale(2, RoundingMode.HALF_UP);

        double[] cocos = new double[]{-1, 0, 0.3, 1};

        log.info(" 债券比例 股票比例 债券期望收益 股票期望收益 相关系数 债券标准差 股票标准差 组合期望收益 组合标准差 夏普比率 债券最优比例 股票最优比例 |   A=4风险组合最优头寸    最小方差");
        for (int i = 0; i < cocos.length; i++) {
            for (int j = 0; j < wd.length; j++) {
                BigDecimal coco = new BigDecimal(cocos[i]).setScale(2, RoundingMode.HALF_UP);
                BigDecimal wDebenture = new BigDecimal(wd[j]).setScale(2, RoundingMode.HALF_UP);
                BigDecimal ePortfolio = c7Service.ePortfolio(wDebenture, eDebenture, eEquity).setScale(2, RoundingMode.HALF_UP);
                //组合方差
                BigDecimal vPortfolio = c7Service.vPortfolio(coco, wDebenture, stdDebenture, stdEquity);
                BigDecimal noRiskRatio=new BigDecimal(5).setScale(2,RoundingMode.HALF_UP);
                //夏普比率
                BigDecimal sharpeRatio=c7Service.sharpeRatio(ePortfolio,noRiskRatio,sqrt(vPortfolio,2));
                //最优组合中债券的比例
                BigDecimal bestWd=c7Service.bestWd(coco,noRiskRatio,eDebenture,eEquity,stdDebenture,stdEquity);
                //最优比例分子
                BigDecimal yNu=(ePortfolio.subtract(noRiskRatio)).divide(new BigDecimal(100),6,RoundingMode.HALF_UP);
                //最优比例分母
                BigDecimal yDNu=(new BigDecimal(4)).multiply(vPortfolio.divide(new BigDecimal(100).pow(2),6,RoundingMode.HALF_UP));
                //最优比例
                BigDecimal y=yNu.divide(yDNu,4,RoundingMode.HALF_UP);
                //最小方差
                BigDecimal yVp=c7Service.vPortfolio(coco,bestWd,stdDebenture,stdEquity);
                log.info("  {}   {}     {}       {}    {}    {}    {}    {}      {}     {}     {}     {}     |       {}       {}",
                        wDebenture,
                        (new BigDecimal(1)).subtract(wDebenture).setScale(2,RoundingMode.HALF_UP),
                        eDebenture,
                        eEquity,
                        coco,
                        stdDebenture,
                        stdEquity,
                        ePortfolio,
                        sqrt(vPortfolio,2),
                        sharpeRatio,
                        bestWd,
                        (new BigDecimal(1)).subtract(bestWd).setScale(2,RoundingMode.HALF_UP),
                        y,
                        yVp
                        );
            }
            log.info("\n");
        }

    }

    /**
     * 开根号
     * @param value
     * @param scale
     * @return
     */
    public static BigDecimal sqrt(BigDecimal value, int scale) {
        BigDecimal num2 = BigDecimal.valueOf(2);
        int precision = 100;
        MathContext mc = new MathContext(precision, RoundingMode.HALF_UP);
        BigDecimal deviation = value;
        int cnt = 0;
        while (cnt < precision) {
            deviation = (deviation.add(value.divide(deviation, mc))).divide(num2, mc);
            cnt++;
        }
        deviation = deviation.setScale(scale, RoundingMode.HALF_UP);
        return deviation;
    }

    /**
     * 风险资产中的债券的最优比例
     * @param coco
     * @param noRiskRatio
     * @param eDebenture
     * @param eEquity
     * @param stdDebenture
     * @param stdEquity
     * @return
     */
    public BigDecimal bestWd(BigDecimal coco,BigDecimal noRiskRatio,BigDecimal eDebenture,BigDecimal eEquity, BigDecimal stdDebenture, BigDecimal stdEquity){
        BigDecimal covariance = covariance(coco, stdDebenture, stdEquity);
//        log.info(" covariance {} ",covariance);
        BigDecimal superD=(eDebenture.subtract(noRiskRatio)).multiply(stdEquity.pow(2));
//        log.info(" superD {} ",superD);
        BigDecimal superE=(eEquity.subtract(noRiskRatio)).multiply(stdDebenture.pow(2));
//        log.info(" superE {} ",superE);
        BigDecimal dCovariance=(eDebenture.subtract(noRiskRatio)).multiply(covariance);
//        log.info(" dCovariance {} ",dCovariance);
        BigDecimal eCovariance=(eEquity.subtract(noRiskRatio)).multiply(covariance);
//        log.info(" eCovariance {} ",eCovariance);
        BigDecimal numerator=superD.subtract(eCovariance);
//        log.info(" numerator {} ",numerator);
        BigDecimal denominator=superD.add(superE).subtract(dCovariance).subtract(eCovariance);
//        log.info(" denominator {} ",denominator);
        return numerator.divide(denominator,2,RoundingMode.HALF_UP);
    }


    /**
     * 组合收益率
     *
     * @param wDebenture
     * @param rDebenture
     * @param wEquity
     * @param rEquity
     * @return
     */
    public BigDecimal rPortfolio(BigDecimal wDebenture, BigDecimal rDebenture, BigDecimal wEquity, BigDecimal rEquity) {
        return (wDebenture.multiply(rDebenture)).add(wEquity.multiply(rEquity));
    }


    /**
     * 组合期望收益
     *
     * @param wDebenture
     * @param eDebenture
     * @param eEquity
     * @return
     */
    public BigDecimal ePortfolio(BigDecimal wDebenture, BigDecimal eDebenture, BigDecimal eEquity) {
        BigDecimal wEquity = (new BigDecimal(1)).subtract(wDebenture);
//        log.info(" 债券比例：{}  股票比例：{} == 债券期望收益：{}  股票期望收益：{} == ", wDebenture, wEquity, eDebenture, eEquity);
        return (wDebenture.multiply(eDebenture)).add(wEquity.multiply(eEquity));
    }

    /**
     * 组合方差 德塔^2
     *
     * @param coco         相关系数
     * @param wDebenture
     * @param stdDebenture
     * @param stdEquity
     * @return 组合的标准差：
     * 当相关系数=1的时候，组合的标准差就是两个资产的标准差的加权平均
     * 当相关系数=-1的时候，可以通过另标准差=0得到完全对冲的头寸比例，这一权重使得组合的标准差=0
     */
    public BigDecimal vPortfolio(BigDecimal coco, BigDecimal wDebenture, BigDecimal stdDebenture, BigDecimal stdEquity) {
        BigDecimal wEquity = (new BigDecimal(1)).subtract(wDebenture);
        //计算协方差=相关系数*德塔债券*德塔股票
        BigDecimal covariance = covariance(coco, stdDebenture, stdEquity);
        //计算方差=w1^2*德塔债券^+w2^2*股票债券^+2*德塔债券^2*德塔股票^2*协方差
//        log.info(" 债券比例：{}  股票比例：{} == 债券德塔：{}  股票德塔：{} == 相关系数：{} == 协方差：{} == ", wDebenture, wEquity, stdDebenture, stdEquity,coco, covariance);

        BigDecimal temp = (new BigDecimal(2)).multiply(wDebenture).multiply(wEquity).multiply(covariance);
        BigDecimal dSq=(wDebenture.pow(2)).multiply(stdDebenture.pow(2));
        BigDecimal eSq=(wEquity.pow(2)).multiply(stdEquity.pow(2));
//        log.info(" temp {}  dSq {} eSq {}", temp.setScale(2, RoundingMode.HALF_UP),dSq.setScale(2, RoundingMode.HALF_UP),eSq.setScale(2, RoundingMode.HALF_UP));
        return dSq.add(eSq).add(temp);
    }

    /**
     * 协方差
     *
     * @param coco         相关系数
     * @param stdDebenture 标准差 德塔
     * @param stdEquity    标准差
     * @return
     */
    public BigDecimal covariance(BigDecimal coco, BigDecimal stdDebenture, BigDecimal stdEquity) {
        return coco.multiply(stdDebenture).multiply(stdEquity);
    }

    /**
     * 计算夏普比率=风险溢价/组合标准差
     * @param ePortfolio
     * @param noRiskRatio
     * @param stdPortfolio
     * @return
     */
    public BigDecimal sharpeRatio(BigDecimal ePortfolio,BigDecimal noRiskRatio,BigDecimal stdPortfolio){
        return (ePortfolio.subtract(noRiskRatio)).divide(stdPortfolio,RoundingMode.HALF_UP);
    }
}
