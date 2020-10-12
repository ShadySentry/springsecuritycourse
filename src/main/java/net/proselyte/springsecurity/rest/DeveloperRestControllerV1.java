package net.proselyte.springsecurity.rest;

import net.proselyte.springsecurity.model.Developer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/developers")
public class DeveloperRestControllerV1 {
    private final List<Developer> DEVELOPERS = Stream.of(
            new Developer(1L, "Ivan", "Ivanov"),
            new Developer(2L, "Kolya", "Ivanov"),
            new Developer(3L, "Sergey", "Sergeev")
    ).collect(Collectors.toList());

    @PreAuthorize("hasAuthority('developers:read')")
    @GetMapping
    public List<Developer> getAll() {
        return DEVELOPERS;
    }

    @PreAuthorize("hasAuthority('developers:read')")
    @GetMapping("/{id}")
    public Developer getById(@PathVariable Long id) {
        return DEVELOPERS.stream()
                .filter(developer -> developer.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PreAuthorize("hasAnyAuthority('developers:write')")
    @PostMapping
    public Developer create(@RequestBody Developer developer) {
        this.DEVELOPERS.add(developer);
        return developer;
    }

    @PreAuthorize("hasAuthority('developers:write')")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        this.DEVELOPERS.removeIf(developer -> developer.getId().equals(id));
    }
}
