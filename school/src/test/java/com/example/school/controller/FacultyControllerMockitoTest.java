package com.example.school.controller;

import com.example.school.model.Faculty;
import com.example.school.model.Student;
import com.example.school.repositories.FacultyRepository;
import com.example.school.service.FacultyService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
@AutoConfigureMockMvc
public class FacultyControllerMockitoTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private FacultyService facultyService;



    private Faculty createTestFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Slowpok");
        faculty.setColor("Purple");
        return faculty;
    }

    private JSONObject createTestJsonFaculty() throws Exception {
        Faculty faculty = createTestFaculty();
        JSONObject jsonFaculty = new JSONObject();
        jsonFaculty.put("id",faculty.getId());
        jsonFaculty.put("name",faculty.getName());
        jsonFaculty.put("color",faculty.getColor());
        return jsonFaculty;
    }

    @Test
    public void shouldCreateFaculty()throws Exception {
        Faculty faculty = createTestFaculty();
        JSONObject jsonFaculty = createTestJsonFaculty();
        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/faculty")
                        .content(jsonFaculty.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));

        verify(facultyService).createFaculty(faculty);
    }

    @Test
    public void shouldFoundFaculty()throws Exception {
        Faculty faculty = createTestFaculty();
        JSONObject jsonFaculty = createTestJsonFaculty();
        when(facultyService.getFaculty(any(Long.class))).thenReturn(faculty);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/"+faculty.getId())
                        .content(jsonFaculty.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
        verify(facultyService).getFaculty(faculty.getId());
    }

    @Test
    public void shouldReturnEditedFaculty()throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Charmonder");
        faculty.setColor("Orange");
        Faculty updatedFaculty = createTestFaculty();
        JSONObject jsonFaculty = createTestJsonFaculty();
        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(updatedFaculty);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(jsonFaculty.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(updatedFaculty.getName()))
                .andExpect(jsonPath("$.color").value(updatedFaculty.getColor()));
        verify(facultyService).editFaculty(updatedFaculty);
    }

    @Test
    public void shouldReturnOkForDeleteRequest()throws Exception {
        Faculty faculty = createTestFaculty();
        doNothing().when(facultyService).deleteFaculty(any(Long.class));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/"+faculty.getId()))
                .andExpect(status().isOk());
        verify(facultyService).deleteFaculty(faculty.getId());
    }

    @Test
    public void shouldReturnRequiredFaculty() throws Exception {
        Faculty faculty = createTestFaculty();
        JSONObject jsonFaculty = createTestJsonFaculty();
        when(facultyService.findByColorIgnoreCaseOrNameIgnoreCase(faculty.getColor(),null)).thenReturn(faculty);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/faculty/byColorOrName")
                        .param("color",faculty.getColor())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));

        verify(facultyService).findByColorIgnoreCaseOrNameIgnoreCase(faculty.getColor(),null);
    }

    @Test
    public void shouldReturnCollection() throws Exception {
        Faculty faculty =createTestFaculty();
        Student student = new Student();
        student.setFaculty(faculty);
        student.setId(1L);
        student.setName("Peter");
        student.setAge(17);
        Student student1 = new Student();
        student1.setFaculty(faculty);
        student1.setId(2L);
        student1.setName("Jane");
        student1.setAge(17);
        Collection<Student> students = List.of(student1,student);
        when(facultyService.getFaculty(faculty.getId())).thenReturn(faculty);
        when(facultyService.getAllStudents(faculty.getId())).thenReturn(students);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/faculty/{id}/students",faculty.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value(student1.getName()))
                .andExpect(jsonPath("$[1].name").value(student.getName()));
        verify(facultyService).getFaculty(faculty.getId());
        verify(facultyService).getAllStudents(faculty.getId());
    }






}
