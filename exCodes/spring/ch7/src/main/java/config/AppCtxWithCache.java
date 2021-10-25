package config;

import aspect.CacheAspect;
import aspect.ExeTimeAspect;
import chap07.Calculator;
import chap07.RecCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;

@Configuration
@EnableAspectJAutoProxy
public class AppCtxWithCache {
    @Bean
    public ExeTimeAspect exeTimeAspect() {
        return new ExeTimeAspect();
    }

    @Bean
    public CacheAspect cacheAspect() {
        return new CacheAspect();
    }



    @Bean
    public Calculator calculator() {
        return new RecCalculator();
    }


}
