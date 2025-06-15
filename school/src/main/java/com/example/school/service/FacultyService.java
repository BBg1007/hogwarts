package com.example.school.service;

import com.example.school.model.Faculty;
import com.example.school.model.Student;
import com.example.school.repositories.FacultyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Transactional
    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }

    @Transactional
    public Faculty getFaculty(Long id) {
        return facultyRepository.findById(id).get();
    }

    @Transactional
    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Transactional
    public void deleteFaculty(long id) {
        facultyRepository.deleteById(id);
    }

    @Transactional
    public Collection<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    @Transactional
    public Faculty findByColorIgnoreCaseOrNameIgnoreCase(String color, String name) {
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(color, name);
    }

    @Transactional
    public Collection<Student>getAllStudents(Long id) {
        return facultyRepository.findById(id).get().getStudents();
    }




}
