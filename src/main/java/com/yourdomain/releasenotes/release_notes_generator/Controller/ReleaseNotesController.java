package com.yourdomain.releasenotes.release_notes_generator.Controller;

import com.yourdomain.releasenotes.release_notes_generator.service.ReleaseNotesService;
import com.yourdomain.releasenotes.release_notes_generator.model.ReleaseNotes;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/release-notes")
public class ReleaseNotesController {

    private final ReleaseNotesService service;

    public ReleaseNotesController(ReleaseNotesService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReleaseNotes> getAllNotes() {
        return service.findAll();
    }
    @PostMapping
    public ReleaseNotes createNote(@RequestBody ReleaseNotes note) {
        return service.save(note);
    }

    public ReleaseNotes getNote(@PathVariable Long id) {
        return service.findById(id).orElseThrow(() -> new RuntimeException("Note not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable Long id) {
        service.deleteById(id);
    }

}
