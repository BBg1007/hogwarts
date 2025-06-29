package com.example.school.service;

import com.example.school.model.Faculty;
import com.example.school.model.Student;
import com.example.school.repositories.StudentRepository;
import com.example.school.util.LogHelper;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    private final Object flag = new Object();
    Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        student.setId(null);
        return studentRepository.save(student);
    }

    @Transactional
    public Student getStudent(Long id) {
        LogHelper.logMethodAndArgsLvlDebug("getStudent", id);
        return studentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
    }

    @Transactional
    public Student editStudent(Student student) {
        LogHelper.logMethodAndArgsLvlDebug("editStudent", student);
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(long id) {
        LogHelper.logMethodAndArgsLvlDebug("deleteStudent", id);
        Student student = studentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        studentRepository.deleteById(id);
    }

    @Transactional
    public Collection<Student> getAllStudents() {
        LogHelper.logMethodAndArgsLvlDebug("getAllStudents");
        return studentRepository.findAll();
    }


    public Collection<Student> findByAgeBetween(int minAge, int maxAge) {
        LogHelper.logMethodAndArgsLvlDebug("findByAgeBetween", minAge, maxAge);
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Collection<Student> findByAge(int age) {
        LogHelper.logMethodAndArgsLvlDebug("findByAge", age);
        return studentRepository.findByAge(age);
    }

    public Faculty getFaculty(Long id) {
        LogHelper.logMethodAndArgsLvlDebug("getFaculty", id);
        return studentRepository.findById(id).get().getFaculty();
    }

    public int getStudentsCount() {
        LogHelper.logMethodAndArgsLvlDebug("getStudentsCount");
        return studentRepository.getStudentsCount();
    }

    public int getAverageAge() {
        LogHelper.logMethodAndArgsLvlDebug("getAverageAge");
        return studentRepository.getAverageAge();
    }

    public List<Student> getLastStudents(int numberOfStudents) {
        LogHelper.logMethodAndArgsLvlDebug("getLastStudents");
        return studentRepository.getLastStudents(numberOfStudents);
    }

    public List<String> filterNamesByFirstLetterToUpperCaseAndNaturalOrder(String firstLetter) {
        return getAllStudents().stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(s -> s.startsWith(firstLetter.toUpperCase()))
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

    public OptionalDouble getAverageAgeOfStudents() {
        return getAllStudents().stream()
                .mapToInt(Student::getAge)
                .average();
    }

    public void printStudents() {
        List<Student> students = new ArrayList<>(getAllStudents());
        int thirdPart = students.size() / 3;

        students.subList(0, thirdPart).forEach(s -> System.out.println(s.getName() + " firstThread"));

        new Thread(() -> students.subList(thirdPart, thirdPart * 2)
                .forEach(s -> System.out.println(s.getName() + " secondThread"))).start();

        new Thread(() -> students.subList(thirdPart * 2, students.size())
                .forEach(s -> System.out.println(s.getName() + " thirdThread"))).start();
    }

    public synchronized void printStudentsSynchronized() {
        List<Student> students = new ArrayList<>(getAllStudents());
        int thirdPart = students.size() / 3;
        int count = 0;


        synchronized (flag) {
            students.subList(0, thirdPart).forEach(s -> System.out.println(s.getName() + " firstThread"));
        }

        new Thread(() -> {
            synchronized (flag) {
                students.subList(thirdPart, thirdPart * 2)
                        .forEach(s -> System.out.println(s.getName() + " secondThread"));
            }
        }).start();

        new Thread(() -> {
            synchronized (flag) {
                students.subList(thirdPart * 2, students.size())
                        .forEach(s -> System.out.println(s.getName() + " thirdThread"));
            }
        }).start();
    }


}
