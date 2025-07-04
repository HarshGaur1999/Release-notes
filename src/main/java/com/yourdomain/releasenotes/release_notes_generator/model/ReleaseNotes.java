package com.yourdomain.releasenotes.release_notes_generator.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "release_notes")
public class ReleaseNotes {

        // Unique identifier for each release note
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "note_id")
        private Long id;  // Uses separate ID sequence from Release

        // Link to manual release (optional)
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "release_id")
        private Release release;  // Nullable relationship

        // GitHub repository reference (format: "owner/repo")
        @Column(name = "repo_url", nullable = false)
        private String repoUrl;

        // Version range being documented
        @Column(name = "from_version", length = 50)
        private String fromVersion;  // e.g. "v1.2.0"

        @Column(name = "to_version", length = 50, nullable = false)
        private String toVersion;    // e.g. "v1.3.0"

        // AI-generated content (Markdown/HTML)
        @Lob  // For large text content
        @Column(name = "content", nullable = false)
        private String content;

        // Generation metadata
        @Column(name = "generated_at", nullable = false)
        private LocalDateTime generatedAt;

        @Column(name = "ai_model", length = 50)
        private String aiModel;  // e.g. "GPT-4", "Claude-3"

        // Status tracking
        @Enumerated(EnumType.STRING)
        @Column(length = 20)
        private GenerationStatus status = GenerationStatus.DRAFT;

        // Categories count for analytics
        @Column(name = "feature_count")
        private int featureCount;

        @Column(name = "fix_count")
        private int fixCount;

        // Default constructor required by JPA
        public ReleaseNotes() {
            this.generatedAt = LocalDateTime.now();
        }

        // Constructor for new note generation
        public ReleaseNotes(String repoUrl, String toVersion) {
            this();
            this.repoUrl = repoUrl;
            this.toVersion = toVersion;
        }

        // Enum for status tracking
        public enum GenerationStatus {
            DRAFT, PUBLISHED, ARCHIVED
        }

        /* Getters and Setters */
        public Long getId() {
            return id;
        }

        public Release getRelease() {
            return release;
        }

        public void setRelease(Release release) {
            this.release = release;
        }

        public String getRepoUrl() {
            return repoUrl;
        }

        public void setRepoUrl(String repoUrl) {
            this.repoUrl = repoUrl;
        }

        // ... (other getters/setters)

        /* Business Methods */
        public void markPublished() {
            this.status = GenerationStatus.PUBLISHED;
        }

        // Helper to set version range
        public void setVersionRange(String fromVersion, String toVersion) {
            this.fromVersion = fromVersion;
            this.toVersion = toVersion;
        }
    }

