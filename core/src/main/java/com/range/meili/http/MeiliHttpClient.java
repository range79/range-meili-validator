package com.range.meili.http;

import com.range.meili.exception.InvalidMaterKeyException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MeiliHttpClient {
    private final static Logger log = LoggerFactory.getLogger(MeiliHttpClient.class);
    private final OkHttpClient client = new OkHttpClient();
    private final String apiKey;

    public MeiliHttpClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public String get(String url) throws IOException {
        Request.Builder builder = new Request.Builder().url(url).get();


        if (apiKey != null && !apiKey.isBlank()) {
            builder.header("Authorization", "Bearer " + apiKey);
        }

        Request request = builder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                if (response.code() == 401 || response.code() == 403) {
                    log.error("Authentication failed! Status: {}. Response: {}", response.code(), response.body());
                    System.exit(1);
                }
                throw new IOException("HTTP request failed: " + response.code());
            }
            return response.body().string();
        }
    }

    public boolean isOk(String url) {
        try {
            get(url);
            return true;
        } catch (InvalidMaterKeyException e) {
            throw e;
        } catch (IOException e) {
            log.warn("Meilisearch not reachable for URL: {}. {}", url, e.getMessage());
            return false;
        }
    }

}
