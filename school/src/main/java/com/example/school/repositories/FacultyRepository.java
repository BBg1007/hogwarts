package com.example.school.repositories;

import com.example.school.model.Faculty;
import com.example.school.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

     Faculty findByColorIgnoreCaseOrNameIgnoreCase(String color,String Name);

}
