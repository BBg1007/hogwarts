package com.example.school.service;

import com.example.school.model.Faculty;
import com.example.school.model.Student;
import com.example.school.repositories.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

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
        return studentRepository.findById(id).orElseThrow(()->new EntityNotFoundException());
    }

    @Transactional
    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(long id) {
        Student student = studentRepository.findById(id).orElseThrow(()->new EntityNotFoundException());
        studentRepository.deleteById(id);
    }

    @Transactional
    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }


    public Collection<Student> findByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Collection<Student> findByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public Faculty getFaculty(Long id) {
        return studentRepository.findById(id).get().getFaculty();
    }

    public int getStudentsCount() {
        return studentRepository.getStudentsCount();
    }

    public int getAverageAge() {
        return studentRepository.getAverageAge();
    }

    public List<Student>getLastStudents(int numberOfStudents) {
        return studentRepository.getLastStudents(numberOfStudents);
    }
}
