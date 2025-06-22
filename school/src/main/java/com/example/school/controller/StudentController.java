package com.example.school.controller;

import com.example.school.model.Faculty;
import com.example.school.model.Student;
import com.example.school.service.StudentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> findStudent(@PathVariable Long id) {
        try {
            Student student =  studentService.getStudent(id);
            return ResponseEntity.ok(student);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.createStudent(student));
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student changedStudent = studentService.editStudent(student);
        if (changedStudent == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(changedStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getAllStudents() {
        if (studentService.getAllStudents() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/by-age")
    public ResponseEntity<Collection<Student>> foundStudentByAge(@RequestParam(required = false,defaultValue = "0") int age,
                                                                 @RequestParam(required = false,defaultValue = "0") int minAge,
                                                                 @RequestParam(required = false,defaultValue = "0") int maxAge) {

        if (age != 0) {
            return ResponseEntity.ok(studentService.findByAge(age));
        }
        if (minAge != 0 && maxAge != 0) {
            return ResponseEntity.ok(studentService.findByAgeBetween(minAge,maxAge));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/faculty")
    public ResponseEntity<Faculty> getStudentFaculty(@RequestParam(required = false,defaultValue = "0") Long id) {
        try {
            Student student = studentService.getStudent(id);
            return ResponseEntity.ok(studentService.getFaculty(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

    }
}
