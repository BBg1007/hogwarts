package com.example.school.controller;

import com.example.school.model.Faculty;
import com.example.school.service.FacultyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        Faculty faculty = facultyService.getFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        return ResponseEntity.ok(facultyService.createFaculty(faculty));
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty changedFaculty = facultyService.editFaculty(faculty);
        if (changedFaculty == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(changedFaculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> getAllFaculty() {
        if (facultyService.getAllFaculties() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyService.getAllFaculties());
    }

    @GetMapping("/by-color/{color}")
    public ResponseEntity<Collection<Faculty>> foundFacultiesByColor(@PathVariable String color) {
        List<Faculty> result = facultyService.getAllFaculties().stream()
                .filter(faculty -> faculty.getColor().equalsIgnoreCase(color))
                .toList();
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
}
