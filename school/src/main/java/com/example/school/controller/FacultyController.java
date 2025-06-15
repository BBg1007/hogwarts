package com.example.school.controller;

import com.example.school.model.Faculty;
import com.example.school.model.Student;
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

    @GetMapping("/by-color")
    public ResponseEntity<Faculty> foundFacultiesByColorOrName(@RequestParam(required = false) String color,
                                                                           @RequestParam(required = false) String name){
        if (((color != null) && !color.isBlank()) || ((name != null) && !name.isBlank())) {
           return ResponseEntity.ok(facultyService.findByColorIgnoreCaseOrNameIgnoreCase(color,name));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Collection<Student>> getAllStudents(@PathVariable Long id) {
        if (facultyService.getFaculty(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyService.getAllStudents(id));
    }

}
