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

            if (!body.contains("\"results\":") || body.contains("\"results\":[]")) {
                return true;
            }
            int statusIndex = body.indexOf("\"status\":\"");
            if (statusIndex == -1) return false;

            int start = statusIndex + "\"status\":\"".length();
            int end = body.indexOf("\"", start);
            if (end == -1) return false;

            String statusString = body.substring(start, end);

            MeiliTaskStatus status = MeiliTaskStatus.from(statusString);

            return switch (status) {
                case SUCCEEDED -> true;
                case ENQUEUED, PROCESSING -> false;
                case FAILED, CANCELED, UNKNOWN ->
                        throw new MeiliNotStartedException("Snapshot import " + status);
            };

        } catch (Exception e) {
            return false;
        }
    }
}
