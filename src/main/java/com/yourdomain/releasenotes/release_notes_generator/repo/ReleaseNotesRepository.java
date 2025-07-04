package com.yourdomain.releasenotes.release_notes_generator.repo;

import com.yourdomain.releasenotes.release_notes_generator.model.ReleaseNotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


    @Repository
    public interface ReleaseNotesRepository extends JpaRepository<ReleaseNotes, Long> {
        // Optional: Add custom query methods later if needed
    }


