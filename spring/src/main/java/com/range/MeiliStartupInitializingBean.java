package com.range.spring;

import com.range.validator.MeiliStartupValidator;
import org.springframework.beans.factory.InitializingBean;

public class MeiliStartupInitializingBean implements InitializingBean {

    private final MeiliStartupValidator validator;

    public MeiliStartupInitializingBean(MeiliStartupValidator validator) {
        this.validator = validator;
    }

    @Override
    public void afterPropertiesSet() {
        validator.validateDatabase();
    }
}
