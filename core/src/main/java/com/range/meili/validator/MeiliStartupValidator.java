package com.range.meili.validator;

import com.range.meili.exception.MeiliNotStartedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Database validator for Meilisearch.
 * Blocks Spring Boot startup until Meilisearch is fully ready,
 * including snapshot import completion.
 */
public class MeiliStartupValidator {
    private final Logger log = LoggerFactory.getLogger(MeiliStartupValidator.class.getName());
    private final MeiliHealthChecker healthChecker;
    private final MeiliTaskChecker taskChecker;
    private final MeiliIndexChecker indexChecker;

    private final int timeoutSeconds;
    private final int intervalSeconds;
    private final boolean loggingEnabled;

    public MeiliStartupValidator(
            MeiliHealthChecker healthChecker,
            MeiliTaskChecker taskChecker,
            MeiliIndexChecker indexChecker,
            int timeoutSeconds,
            int intervalSeconds,
            boolean loggingEnabled
    ) {
        this.healthChecker = healthChecker;
        this.taskChecker = taskChecker;
        this.indexChecker = indexChecker;
        this.timeoutSeconds = timeoutSeconds;
        this.intervalSeconds = intervalSeconds;
        this.loggingEnabled =loggingEnabled;
    }

    public void validate() {
        long deadline = System.currentTimeMillis() + timeoutSeconds * 1000L;

        while (System.currentTimeMillis() < deadline) {

            if (!healthChecker.isHealthy()) {
                logIfEnabled(loggingEnabled, "Health check failed");
                sleep();
                continue;
            }

            if (!taskChecker.isSnapshotFinished()) {
                logIfEnabled(loggingEnabled, "Snapshot import still running");
                sleep();
                continue;
            }

            if (!indexChecker.isQueryable()) {
                logIfEnabled(loggingEnabled, "Indexes are not queryable yet");
                sleep();
                continue;
            }

            return;
        }

        throw new MeiliNotStartedException(
                "MeiliSearch is not ready after " + timeoutSeconds + " seconds"
        );
    }



    private void sleep() {
        try {
            Thread.sleep(intervalSeconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
    private void logIfEnabled(boolean enabled, String message) {
        if (enabled) {
            log.error("Meili startup failed: {}", message);
        }
    }
}
