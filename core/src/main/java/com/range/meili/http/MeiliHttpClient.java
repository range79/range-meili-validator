package com.range.meili.http;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class MeiliHttpClient {

    private final OkHttpClient client = new OkHttpClient();
    private final String apiKey;

    public MeiliHttpClient() {
        this.apiKey = null;
    }

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
                throw new IOException("HTTP request failed: " + response.code());
            }
            return response.body().string();
        }
    }

    public boolean isOk(String url) {
        try {
            get(url);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
