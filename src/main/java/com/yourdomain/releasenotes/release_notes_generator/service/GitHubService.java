package com.yourdomain.releasenotes.release_notes_generator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.yourdomain.releasenotes.release_notes_generator.dto.git.GitHubCommit;
import com.yourdomain.releasenotes.release_notes_generator.dto.git.GitHubCommit.Author;
import com.yourdomain.releasenotes.release_notes_generator.dto.git.GitHubCommit.CommitDetails;
import com.yourdomain.releasenotes.release_notes_generator.dto.git.GitHubPullRequest;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GitHubService {

    private final WebClient webClient;

    public GitHubService(@Value("${github.token}") String token) {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(cfg -> cfg.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))   // 10 MB buffer
                .build();

        this.webClient = WebClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader("Authorization", "Bearer " + token)
                .exchangeStrategies(strategies)
                .build();
    }

    /* ----------------------------------------------------------------
       COMMITS
       ---------------------------------------------------------------- */
    public List<GitHubCommit> fetchCommits(String owner, String repo, String base, String head) {
        String url = String.format("/repos/%s/%s/compare/%s...%s", owner, repo, base, head);

        JsonNode body = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        return parseCommits(body.get("commits"));
    }

    private List<GitHubCommit> parseCommits(JsonNode commitsNode) {
        if (commitsNode == null || !commitsNode.isArray()) return Collections.emptyList();

        List<GitHubCommit> list = new ArrayList<>(commitsNode.size());
        for (JsonNode n : commitsNode) {
            JsonNode c  = n.get("commit");
            JsonNode au = c.get("author");

            list.add(new GitHubCommit(
                    n.get("sha").asText(),
                    new CommitDetails(
                            c.get("message").asText(),
                            new Author(au.get("name").asText(), au.get("date").asText())
                    ),
                    n.get("html_url").asText()
            ));
        }
        return list;
    }

    /* ----------------------------------------------------------------
       PULL‑REQUESTS
       ---------------------------------------------------------------- */
    public List<GitHubPullRequest> fetchPullRequests(
            String owner,
            String repo,
            Instant since,
            Instant until)
    {
        List<GitHubPullRequest> result = new ArrayList<>();
        int page = 1;
        boolean more = true;

        while (more) {
            String url = String.format(
                    "/repos/%s/%s/pulls?state=closed&sort=updated&direction=desc&per_page=100&page=%d",
                    owner, repo, page);

            GitHubPullRequest[] batch = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(GitHubPullRequest[].class)
                    .block();

            if (batch == null || batch.length == 0) {
                more = false;
                continue;
            }

            for (GitHubPullRequest pr : batch) {
                if (pr.mergedAt() == null) continue;  // skip un‑merged PRs
                Instant merged = Instant.parse(pr.mergedAt());
                if (!merged.isBefore(since) && !merged.isAfter(until)) {
                    result.add(pr);
                }
            }

            // stop early if oldest PR in this page is older than 'since'
            Optional<Instant> oldestInPage = Arrays.stream(batch)
                    .filter(pr -> pr.mergedAt() != null)
                    .map(pr -> Instant.parse(pr.mergedAt()))
                    .min(Comparator.naturalOrder());

            more = oldestInPage.filter(d -> d.isAfter(since)).isPresent();
            page++;
        }
        return result;
    }
}
