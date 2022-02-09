package com.zyy.community.strategy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 策略工厂
 */

@Service
public class UserStrategyFactory {

    // 对应类型的都会被自动注入
    @Resource
    private List<UserStrategy> strategies;

    public UserStrategy getStrategy(String type) {
        for (UserStrategy strategy : strategies) {
            if (StringUtils.equals(strategy.getSupportedType(), type)) {
                return strategy;
            }
        }
        return null;
    }

}
