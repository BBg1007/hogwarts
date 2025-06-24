package com.example.school.repositories;

import com.example.school.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Collection<Student> findByAgeBetween(int minAge, int maxAge);

    Collection<Student> findByAge(int age);

    Optional<Student> findById(Long studentId);

    @Query(value = "SELECT COUNT(*) as total FROM student",nativeQuery = true)
    public int getStudentsCount();

    @Query(value = "SELECT AVG(age) as average_age FROM student",nativeQuery = true)
    public int getAverageAge();
    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT :numberOfStudents",nativeQuery = true)
    public List<Student> getLastStudents(@Param("numberOfStudents") int numberOfStudents);

}
