package com.range.autoconfig;

import com.range.validator.MeiliStartupValidator;
import com.range.spring.MeiliStartupInitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class MeiliStartupAutoConfiguration {

    @Bean
    public MeiliStartupValidator meiliStartupValidator(
            @Value("${meili.startup.url:localhost:7070}") String url,
            @Value("${meili.startup.timeout:30}") int timeout,
            @Value("${meili.startup.interval:1}") int interval
    ) {
        MeiliStartupValidator validator = new MeiliStartupValidator();
        validator.setDataSourceURL(url);
        validator.setTimeout(timeout);
        validator.setInterval(interval);
        return validator;
    }

    @Bean
    public MeiliStartupInitializingBean meiliStartupInitializingBean(
            MeiliStartupValidator validator
    ) {
        return new MeiliStartupInitializingBean(validator);
    }
}
