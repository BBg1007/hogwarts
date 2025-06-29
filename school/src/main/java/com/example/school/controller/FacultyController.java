package com.example.school.controller;

import com.example.school.model.Faculty;
import com.example.school.model.Student;
import com.example.school.service.FacultyService;
import com.example.school.util.LogHelper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;


@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }


    @GetMapping("{id}")
    public ResponseEntity<Faculty> findFaculty(@PathVariable Long id) {
        try {
          Faculty faculty =  facultyService.getFaculty(id);
            return ResponseEntity.ok(faculty);
        } catch (EntityNotFoundException e) {
            LogHelper.logExceptionsLvlError(e,id);
            return ResponseEntity.notFound().build();
        }
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
        try {
            facultyService.deleteFaculty(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            LogHelper.logExceptionsLvlError(e,id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> getAllFaculty() {
        if (facultyService.getAllFaculties() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyService.getAllFaculties());
    }

    @GetMapping("/byColorOrName")
    public ResponseEntity<Faculty> foundFacultiesByColorOrName(@RequestParam(required = false) String color,
                                                                           @RequestParam(required = false) String name){
        if (((color != null) && !color.isBlank()) || ((name != null) && !name.isBlank())) {
           return ResponseEntity.ok(facultyService.findByColorIgnoreCaseOrNameIgnoreCase(color,name));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<Collection<Student>> getAllStudents(@PathVariable Long id) {
        if (facultyService.getFaculty(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyService.getAllStudents(id));
    }

    @GetMapping("/longestName")
    public ResponseEntity<String> getLongestName() {
        String longestName = facultyService.getLongestFacultyName();
        if (longestName.isEmpty() || longestName.isBlank()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(longestName);
        }


    @GetMapping("/getSum")
    public int getSum(){
        return facultyService.getSumOneToMillion();
    }


}
