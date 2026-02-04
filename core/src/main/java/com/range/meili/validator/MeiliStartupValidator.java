package com.range.meili.validator;

import com.range.meili.enums.LogMode;
import com.range.meili.exception.MeiliNotStartedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;


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
    private final LogMode logMode;

    public MeiliStartupValidator(
            MeiliHealthChecker healthChecker,
            MeiliTaskChecker taskChecker,
            MeiliIndexChecker indexChecker,
            int timeoutSeconds,
            int intervalSeconds,
            LogMode logMode
    ) {
        this.healthChecker = healthChecker;
        this.taskChecker = taskChecker;
        this.indexChecker = indexChecker;
        this.timeoutSeconds = timeoutSeconds;
        this.intervalSeconds = intervalSeconds;
        this.logMode = logMode;
    }

    public void validate() {
        Instant deadline = Instant.now().plusSeconds(timeoutSeconds);

        while (Instant.now().isBefore(deadline)) {

            if (!healthChecker.isHealthy()) {
                log(LogMode.INFO, "Health check failed");
            } else if (!taskChecker.isSnapshotFinished()) {
            log(LogMode.WARN, "Snapshot import still running");
            } else if (!indexChecker.isQueryable()) {
                log(LogMode.INFO, "Indexes are not queryable yet");
            } else {
                log(LogMode.INFO, "MeiliSearch is fully ready");
                return;
            }

            sleep();
        }

        log(LogMode.ERROR, "Startup timeout reached");
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
    private void log(LogMode level, String message) {
        if (logMode == null || logMode == LogMode.NONE) return;

        switch (level) {
            case DEBUG -> {
                if (logMode == LogMode.DEBUG) log.debug(message);
            }
            case INFO -> {
                if (logMode == LogMode.DEBUG || logMode == LogMode.INFO) log.info(message);
            }
            case WARN -> {
                if (logMode == LogMode.DEBUG || logMode == LogMode.INFO || logMode == LogMode.WARN)
                    log.warn(message);
            }
            case ERROR -> log.error(message);
        }
    }


}
