package config;

import aspect.CacheAspect;
import aspect.ExeTimeAspect;
import chap07.Calculator;
import chap07.RecCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AppCtx {
    @Bean
    public ExeTimeAspect exeTimeAspect() {
        return new ExeTimeAspect();
    }

    @Bean
    public RecCalculator calculator() {
        return new RecCalculator();
    }

}
