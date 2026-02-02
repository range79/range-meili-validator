package com.range.meili.validator;


import com.range.meili.enums.MeiliTaskStatus;
import com.range.meili.exception.MeiliNotStartedException;
import com.range.meili.http.MeiliHttpClient;


public class MeiliTaskChecker {

    private final MeiliHttpClient httpClient;
    private final String baseUrl;

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
            return false;
        }
    }
}
