package com.yourdomain.releasenotes.release_notes_generator.dto.git;
import com.fasterxml.jackson.annotation.JsonProperty;

    public record GitHubCommit(
            String sha,
            @JsonProperty("commit") CommitDetails details,
            @JsonProperty("html_url") String htmlUrl
    ) {
        public record CommitDetails(
                String message,
                @JsonProperty("author") Author author
        ) {}

        public record Author(
                String name,
                String date
        ) {}
    }



