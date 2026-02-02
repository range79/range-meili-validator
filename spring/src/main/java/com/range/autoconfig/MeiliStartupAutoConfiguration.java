package com.range.autoconfig;

import com.range.MeiliStartupLifecycle;
import com.range.meili.http.MeiliHttpClient;
import com.range.meili.validator.MeiliHealthChecker;
import com.range.meili.validator.MeiliIndexChecker;
import com.range.meili.validator.MeiliStartupValidator;
import com.range.meili.validator.MeiliTaskChecker;
import com.range.properties.MeiliStartupProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(MeiliStartupProperties.class)
@ConditionalOnClass(MeiliStartupValidator.class)
@ConditionalOnProperty(
        prefix = "meili.startup",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class MeiliStartupAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MeiliHttpClient meiliHttpClient(MeiliStartupProperties properties) {
        return new MeiliHttpClient(properties.getApiKey());
    }

    @Bean
    @ConditionalOnMissingBean
    public MeiliStartupValidator meiliStartupValidator(
            MeiliHttpClient httpClient,
            MeiliStartupProperties properties
    ) {
        return new MeiliStartupValidator(
                new MeiliHealthChecker(httpClient, properties.getUrl()),
                new MeiliTaskChecker(httpClient, properties.getUrl()),
                new MeiliIndexChecker(httpClient, properties.getUrl()),
                properties.getTimeout(),
                properties.getInterval(),
                properties.getLogMode()
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public MeiliStartupLifecycle meiliStartupRunner(
            MeiliStartupValidator validator
    ) {
        return new MeiliStartupLifecycle(validator);
    }
}
