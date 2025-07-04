package com.yourdomain.releasenotes.release_notes_generator.Controller;

import com.yourdomain.releasenotes.release_notes_generator.model.Release;
import com.yourdomain.releasenotes.release_notes_generator.repo.ReleaseRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/releases")
public class ReleaseController {

    private final ReleaseRepository releaseRepository;

    public ReleaseController(ReleaseRepository releaseRepository) {
        this.releaseRepository = releaseRepository;

    }
    @GetMapping
    public List<Release> getAllReleases() {
        return releaseRepository.findAll();
    }

    @PostMapping
    public Release addRelease(@RequestBody Release release) {
        return releaseRepository.save(release);
    }
}
