package com.range.meili.validator;


import com.range.meili.http.MeiliHttpClient;

public class MeiliHealthChecker {

    private final MeiliHttpClient httpClient;
    private final String baseUrl;

    public MeiliHealthChecker(MeiliHttpClient httpClient, String baseUrl) {
        this.httpClient = httpClient;
        this.baseUrl = baseUrl;
    }

    public boolean isHealthy() {
        return httpClient.isOk(baseUrl + "/health");
    }
}

