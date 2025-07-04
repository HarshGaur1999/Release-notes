package com.yourdomain.releasenotes.release_notes_generator.service;


import com.yourdomain.releasenotes.release_notes_generator.model.ReleaseNotes;
import com.yourdomain.releasenotes.release_notes_generator.repo.ReleaseNotesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReleaseNotesService {

    private final ReleaseNotesRepository repository;


    public ReleaseNotesService(ReleaseNotesRepository repository) {
        this.repository = repository;
    }

    public List<ReleaseNotes> findAll()
    {
        return repository.findAll();
    }

    public Optional<ReleaseNotes> findById(Long id) { return repository.findById(id); }

    public ReleaseNotes save(ReleaseNotes notes) { return repository.save(notes); }

    public void deleteById(Long id) { repository.deleteById(id); }
}
