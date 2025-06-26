package com.example.school.service;

import com.example.school.model.Faculty;
import com.example.school.model.Student;
import com.example.school.repositories.FacultyRepository;
import com.example.school.util.LogHelper;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    @Transactional
    public Faculty createFaculty(Faculty faculty) {
        LogHelper.logMethodAndArgsLvlDebug("createFaculty",faculty);
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }

    @Transactional
    public Faculty getFaculty(Long id) {
        LogHelper.logMethodAndArgsLvlDebug("getFaculty",id);
        return facultyRepository.findById(id).orElseThrow(() ->  new EntityNotFoundException());
    }

    @Transactional
    public Faculty editFaculty(Faculty faculty) {
        LogHelper.logMethodAndArgsLvlDebug("editFaculty",faculty);
        return facultyRepository.save(faculty);
    }

    @Transactional
    public void deleteFaculty(long id) {
        LogHelper.logMethodAndArgsLvlDebug("deleteFaculty",id);
        Faculty faculty = getFaculty(id);
        facultyRepository.delete(faculty);
    }

    @Transactional
    public Collection<Faculty> getAllFaculties() {
        LogHelper.logMethodAndArgsLvlDebug("getAllFaculties");
        return facultyRepository.findAll();
    }

    @Transactional
    public Faculty findByColorIgnoreCaseOrNameIgnoreCase(String color, String name) {
        LogHelper.logMethodAndArgsLvlDebug("getAllFaculties", color,name);
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(color, name);
    }

    @Transactional
    public Collection<Student>getAllStudents(Long id) {
        LogHelper.logMethodAndArgsLvlDebug("deleteFaculty",id);
        return facultyRepository.findById(id).get().getStudents();
    }




}
