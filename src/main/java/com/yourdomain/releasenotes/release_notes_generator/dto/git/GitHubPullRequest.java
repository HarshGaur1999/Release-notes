package com.yourdomain.releasenotes.release_notes_generator.dto.git;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubPullRequest(
        int number,
        String title,
        String body,
        @JsonProperty("user") Author author,
        @JsonProperty("merged_at") String mergedAt,
        @JsonProperty("html_url") String htmlUrl
) {
    public record Author(String login) {}
}
