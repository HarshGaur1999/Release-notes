package com.yourdomain.releasenotes.release_notes_generator.Controller;

import com.yourdomain.releasenotes.release_notes_generator.service.GitHubService;
import com.yourdomain.releasenotes.release_notes_generator.dto.git.GitHubCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/github")
public class GitHubController {

    private final GitHubService gitHubService;

    @Autowired
    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/compare")
    public ResponseEntity<List<GitHubCommit>> compareCommits(
            @RequestParam String owner,
            @RequestParam String repo,
            @RequestParam String base,
            @RequestParam String head
    ) {
        List<GitHubCommit> commits = gitHubService.fetchCommits(owner, repo, base, head);
        return ResponseEntity.ok(commits);
    }
}
