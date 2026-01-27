package com.range.meili.validator;


import com.range.meili.http.MeiliHttpClient;

public class MeiliIndexChecker {

    private final MeiliHttpClient httpClient;
    private final String baseUrl;

    public MeiliIndexChecker(MeiliHttpClient httpClient, String baseUrl) {
        this.httpClient = httpClient;
        this.baseUrl = baseUrl;
    }

    public boolean isQueryable() {
        return httpClient.isOk(baseUrl + "/indexes");
    }
}
