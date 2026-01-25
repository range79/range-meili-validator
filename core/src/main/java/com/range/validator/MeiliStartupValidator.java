package com.range.validator;

import com.range.validator.exception.MeiliNotStartedException;
import com.range.validator.exception.MeiliUrlIsNullOREmptyException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Database validator for Meilisearch
 */
public class MeiliStartupValidator {

    /*
     * default timeout in seconds
     */
    private static final int DEFAULT_TIMEOUT = 30;

    /*
     * default interval in seconds
     */
    private static final int DEFAULT_INTERVAL = 1;

    /*
     * Meili search base URL (example: http://localhost:7700)
     */
    private String dataSourceURL;

    private int timeout = DEFAULT_TIMEOUT;
    private int interval = DEFAULT_INTERVAL;

    private final OkHttpClient httpClient = new OkHttpClient();

    public void setInterval(int interval) {
        if (interval > 0) {
            this.interval = interval;
        }
    }

    public void setTimeout(int timeout) {
        if (timeout > 0) {
            this.timeout = timeout;
        }
    }

    public void setDataSourceURL(String dataSourceURL) {
        if (dataSourceURL == null || dataSourceURL.isBlank()) {
            throw new MeiliUrlIsNullOREmptyException(
                    "You can't validate Meili without URL. " +
                            "Validating Meili without URL is like making coffee without coffee."
            );
        }
        this.dataSourceURL = dataSourceURL;
    }

    /**
     * Blocks application startup until Meili search is ready
     */
    public void validateDatabase() {
        if (dataSourceURL == null) {
            throw new MeiliUrlIsNullOREmptyException("Meili datasource URL is not set");
        }

        long deadline = System.currentTimeMillis() + timeout * 1000L;

        while (System.currentTimeMillis() < deadline) {
            if (isHealthy()) {
                return;
            }
            sleep(interval * 1000L);
        }

        throw new MeiliNotStartedException(
                "Meili search is not ready after " + timeout + " seconds"
        );
    }

    /**
     * Calls /health endpoint
     */
    private boolean isHealthy() {
        Request request = new Request.Builder()
                .url(dataSourceURL + "/health")
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            return false;
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
