package com.example.garden.runner;

import com.example.garden.enums.StockCashFlowEnum;
import com.example.garden.model.DTO.StockDTO;
import com.example.garden.service.corporatefinance.C9Service;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.math.BigDecimal;

/**
 * ApplicationRunner,一个spring容器启动完成执行的类
 *
 * 在我们的web程序中，用spring来管理各个实例(bean), 有时在程序中为了使用已被实例化的bean, 通常会用到这样的代码：
 * ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext-common.xml");
 * AbcService abcService = (AbcService)appContext.getBean("abcService");
 * 但是每一次这样使用都要初始化一遍，造成线程过载，带来冗余。
 * 所以，ApplicationContextAware就解决了这个问题。。
 */
public class FirstRunner implements ApplicationRunner, ApplicationContextAware {
    ApplicationContext context;
    @Autowired
    C9Service c9Service;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        StockDTO stockDTO=new StockDTO();
        stockDTO.setDiv(new BigDecimal(3));
        stockDTO.setRate(new BigDecimal(0.15));
        stockDTO.setGrowthRate(new BigDecimal(0.1));
        stockDTO.setGrowthRateType(StockCashFlowEnum.UNVARYING.getKey());
        c9Service.stockPrice(stockDTO);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context=applicationContext;
    }
}
