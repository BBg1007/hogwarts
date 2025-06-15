package com.example.school.service;

import com.example.school.model.Student;
import com.example.school.repositories.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        student.setId(null);
        return studentRepository.save(student);
    }

    @Transactional
    public Student getStudent(Long id) {
        return studentRepository.findById(id).get();
    }

    @Transactional
    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
    }

    @Transactional
    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
