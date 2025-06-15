package com.example.school.repositories;

import com.example.school.model.Faculty;
import com.example.school.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Collection<Student> findByAgeBetween(int minAge, int maxAge);

    Collection<Student> findByAge(int age);

}
