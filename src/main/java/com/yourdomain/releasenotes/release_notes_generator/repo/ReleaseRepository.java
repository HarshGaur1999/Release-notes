package com.yourdomain.releasenotes.release_notes_generator.repo;

import com.yourdomain.releasenotes.release_notes_generator.model.Release;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReleaseRepository extends JpaRepository<Release, Long> {


}
