package com.range.meili.validator;



import com.range.meili.enums.MeiliTaskStatus;
import com.range.meili.exception.MeiliNotStartedException;
import com.range.meili.http.MeiliHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MeiliTaskChecker {

    private final MeiliHttpClient httpClient;
    private final String baseUrl;
    private final Logger log =LoggerFactory.getLogger(MeiliTaskChecker.class);

    public MeiliTaskChecker(MeiliHttpClient httpClient, String baseUrl) {
        this.httpClient = httpClient;
        this.baseUrl = baseUrl;
    }

    public boolean isSnapshotFinished() {
        try {
            String body = httpClient.get(baseUrl + "/tasks?types=snapshotCreation");


            if (!body.contains("snapshotImport")) {
                return true;
            }

            MeiliTaskStatus status = MeiliTaskStatus.from(body);

            return switch (status) {
                case SUCCEEDED -> true;
                case ENQUEUED, PROCESSING -> false;
                case FAILED, CANCELED,UNKNOWN ->
                        throw new MeiliNotStartedException("Snapshot import " + status);
            };

        } catch (Exception e) {
            log.error("Task check failed: " + e.getMessage());
            return false;
        }
    }
}
