package com.range.validator;

import com.range.validator.exception.DatabaseUrlIsNullOREmptyException;

/**
 * database validator for Meili
 */
public class MeiliStartupValidator {
    /*
    the default timeout ( this is not chatgpt comment)
     */
    private static final int DEFAULT_TIMEOUT = 30;
    /*
    the default interval
     */
    private static final int DEFAULT_INTERVAL = 1;


    /*
    the datasource URL of Meili
         */
    private String dataSourceURL;


    private int timeout = DEFAULT_TIMEOUT;
    private int interval = DEFAULT_INTERVAL;

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setDataSourceURL(String dataSourceURL) {
        this.dataSourceURL = dataSourceURL;
    }

    public void validateDatabase() {
if (dataSourceURL == null || dataSourceURL.isBlank()){
    throw new DatabaseUrlIsNullOREmptyException(
            "You can't validate Meili without url" +
            "validating some service without url like making cofee without coffee")
}




    }
}
