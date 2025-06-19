package com.example.school.controller;

import com.example.school.model.Faculty;
import com.example.school.model.Student;
import com.example.school.service.StudentService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc
public class StudentControllerMockitoTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private StudentService studentService;


    private Student createTestStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setName("Jane");
        student.setAge(20);
        return student;
    }

    private JSONObject createTestJsonStudent() throws Exception {
        Student student = createTestStudent();
        JSONObject jsonStudent = new JSONObject();
        jsonStudent.put("id", student.getId());
        jsonStudent.put("name", student.getName());
        jsonStudent.put("age", student.getAge());
        return jsonStudent;
    }

    @Test
    public void shouldCreateStudent() throws Exception {
        Student student = createTestStudent();
        JSONObject jsonStudent = createTestJsonStudent();
        when(studentService.createStudent(any(Student.class))).thenReturn(student);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/students")
                        .content(jsonStudent.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()));

        verify(studentService).createStudent(student);
    }

    @Test
    public void shouldFoundStudent() throws Exception {
        Student student = createTestStudent();
        JSONObject jsonStudent = createTestJsonStudent();
        when(studentService.getStudent(any(Long.class))).thenReturn(student);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/{id}", student.getId())
                        .content(jsonStudent.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()));
        verify(studentService).getStudent(student.getId());
    }

    @Test
    public void shouldReturnEditedStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Peter");
        student.setAge(20);
        Student updatedStudent = createTestStudent();
        JSONObject jsonStudent = createTestJsonStudent();
        when(studentService.editStudent(any(Student.class))).thenReturn(updatedStudent);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/students")
                        .content(jsonStudent.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(updatedStudent.getName()))
                .andExpect(jsonPath("$.age").value(updatedStudent.getAge()));
        verify(studentService).editStudent(updatedStudent);
    }

    @Test
    public void shouldReturnOkForDeleteRequest() throws Exception {
        Student student = createTestStudent();
        doNothing().when(studentService).deleteStudent(any(Long.class));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/students/{id}", student.getId()))
                .andExpect(status().isOk());
        verify(studentService).deleteStudent(student.getId());
    }

    @Test
    public void shouldReturnRequiredStudent() throws Exception {
        Student student = createTestStudent();
        Student student1 = createTestStudent();
        student1.setName("Peter");
        student1.setAge(22);
        student1.setId(2L);
        Collection<Student> exactAge = List.of(student);
        Collection<Student> betweenAge = List.of(student1);
        when(studentService.findByAge(student.getAge())).thenReturn(exactAge);
        when(studentService.findByAgeBetween(student1.getAge() - 1, student1.getAge() + 1)).thenReturn(betweenAge);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/by-age")
                        .param("age", student.getAge() + "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(student.getName()))
                .andExpect(jsonPath("$.[0].age").value(student.getAge()));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/by-age")
                        .param("minAge", student1.getAge() - 1 + "")
                        .param("maxAge", student1.getAge() + 1 + "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(student1.getName()))
                .andExpect(jsonPath("$[0].age").value(student1.getAge()));
        verify(studentService).findByAge(student.getAge());
        verify(studentService).findByAgeBetween(student1.getAge() - 1, student1.getAge() + 1);
    }

    @Test
    public void shouldReturnFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Slowpok");
        faculty.setColor("Purple");
        Student student = createTestStudent();
        student.setFaculty(faculty);

        when(studentService.getStudent(any(Long.class))).thenReturn(student);
        when(studentService.getFaculty(any(Long.class))).thenReturn(student.getFaculty());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/faculty")
                        .param("id", student.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
        verify(studentService).getStudent(student.getId());
        verify(studentService).getFaculty(student.getId());
    }
}
