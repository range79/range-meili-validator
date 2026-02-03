package com.range.properties;

import com.range.meili.enums.LogMode;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "meili.startup")
public class MeiliStartupProperties {


    private String url = "http://localhost:7700";

    /**
     * Max wait time (seconds)
     */
    private int timeout = 30;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /** Meili api key for connecting Meili if you start your app with --master-key
 * add here apikey
 */
    private String apiKey;
    /**
     * Poll interval (seconds)
     */
    private int interval = 1;

    public LogMode getLogMode() {
        return logMode;
    }

    public void setLogMode(LogMode logMode) {
        this.logMode = logMode;
    }

    private LogMode logMode= LogMode.INFO;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
