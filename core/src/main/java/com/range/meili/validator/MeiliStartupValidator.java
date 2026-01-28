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

    public MeiliStartupValidator(
            MeiliHealthChecker healthChecker,
            MeiliTaskChecker taskChecker,
            MeiliIndexChecker indexChecker,
            int timeoutSeconds,
            int intervalSeconds
    ) {
        this.healthChecker = healthChecker;
        this.taskChecker = taskChecker;
        this.indexChecker = indexChecker;
        this.timeoutSeconds = timeoutSeconds;
        this.intervalSeconds = intervalSeconds;
    }

    public void validate() {
        long deadline = System.currentTimeMillis() + timeoutSeconds * 1000L;

        while (System.currentTimeMillis() < deadline) {

            if (!healthChecker.isHealthy()) {
                sleep();
                log.warn("Meili search not healty");
                continue;
            }

            if (!taskChecker.isSnapshotFinished()) {
                sleep();
                continue;
            }

            if (!indexChecker.isQueryable()) {
                sleep();
                continue;
            }

            return;
        }

        throw new MeiliNotStartedException(
                "Meili search is not ready after " + timeoutSeconds + " seconds"
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
}
