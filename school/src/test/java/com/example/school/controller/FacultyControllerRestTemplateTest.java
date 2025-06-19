package com.example.school.controller;
import com.example.school.model.Student;
import com.example.school.repositories.FacultyRepository;
import com.jayway.jsonpath.JsonPath;
import org.assertj.core.api.Assertions;
import com.example.school.model.Faculty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.HTMLDocument;
import java.util.Collection;
import java.util.Iterator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class FacultyControllerRestTemplateTest {
    @LocalServerPort
    private int port;
    @Autowired
    private FacultyController facultyController;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FacultyRepository facultyRepository;

    @AfterEach
    public void clearDb() {
        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {});
        response.getBody().forEach(faculty -> restTemplate.delete(getBaseUrl()+"/"+faculty.getId()));
    }

    private String getBaseUrl() {
       return "http://localhost:"+port+"/faculty";
    }

    private Faculty createTestFaculty(String name,String color) {
        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setColor(color);
        ResponseEntity<Faculty> response = restTemplate.postForEntity(getBaseUrl(),faculty,Faculty.class);
        return response.getBody();
    }
    @Test
     public void contextLoads() throws Exception {
        Assertions.assertThat(facultyController).isNotNull();
    }
    @Test
    public void shouldCreateFaculty() {
        Faculty newFaculty = new Faculty();
        newFaculty.setName("Charmonder");
        newFaculty.setColor("Orange");
        ResponseEntity<Faculty> response = restTemplate.postForEntity(getBaseUrl(),
                newFaculty,
                Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Faculty created = response.getBody();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("Charmonder");
    }

    @Test
    public void shouldFindFacultyTest() {
        Faculty testFaculty = createTestFaculty("Slowpok","Purple");
        ResponseEntity<Faculty> response = restTemplate.getForEntity(getBaseUrl()+"/"+testFaculty.getId(),
                Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(testFaculty.getName());

    }
    @Test
    public void shouldEditCreatedFaculty() {
        Faculty testFaculty = createTestFaculty("Slowpok","Purple");
        Faculty newFaculty = new Faculty();
        newFaculty.setName("Charmonder");
        newFaculty.setColor("Orange");
        newFaculty.setId(testFaculty.getId());
        ResponseEntity<Faculty> response = restTemplate.exchange(getBaseUrl(), HttpMethod.PUT,new HttpEntity<>(newFaculty),Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(newFaculty.getId());
        assertThat(response.getBody().getName()).isEqualTo(newFaculty.getName());
        assertThat(response.getBody().getColor()).isEqualTo(newFaculty.getColor());

    }
    @Test
    public void shouldDeleteCreatedFaculty() {
        Faculty testFaculty = createTestFaculty("Slowpok","Purple");
        ResponseEntity<Void> delResponse = restTemplate.exchange(
                getBaseUrl() +"/"+ testFaculty.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertThat(delResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(getBaseUrl()+"/"+testFaculty.getId(),
                Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }
    @Test
    public void ShouldReturnCorrectCollectionOfFaculty() {
        Faculty faculty1 = createTestFaculty("Slowpok","Purple");
        Faculty faculty2 = createTestFaculty("Charmonder","Orange");
        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
        Iterator<Faculty> facultyIterator = response.getBody().iterator();
        assertThat(facultyIterator.next().getName()).isEqualTo("Slowpok");
        assertThat(facultyIterator.next().getName()).isEqualTo("Charmonder");
    }
    @Test
    public void shouldReturnRequiredFacultyByColorOrName() {
        String urlForColorParam = "/byColorOrName?color=";
        String urlForNameParam = "/byColorOrName?name=";
        assertThat(facultyRepository.findAll().size()).isEqualTo(0);
        Faculty faculty = createTestFaculty("Slowpok","Purple");
        ResponseEntity<Faculty> response = restTemplate.getForEntity(getBaseUrl()+urlForColorParam+faculty.getColor(),Faculty.class);
        assertThat("Slowpok").isEqualTo(response.getBody().getName());
        assertThat("Purple").isEqualTo(response.getBody().getColor());
        response = restTemplate.getForEntity(getBaseUrl()+urlForNameParam+faculty.getName(),Faculty.class);
        assertThat("Slowpok").isEqualTo(response.getBody().getName());
        assertThat("Purple").isEqualTo(response.getBody().getColor());
    }

    @Test
    public void ShouldReturnEmptyCollectionIfStudentNotExist() {
        Faculty faculty = createTestFaculty("Slowpok","Purple");
        ResponseEntity<Collection<Student>> response = restTemplate.exchange(getBaseUrl() + "/" + faculty.getId() + "/students", HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Student>>() {});
        assertThat(response.getBody().isEmpty());
    }

    @Test
    public void ShouldReturnCorrectCollectionOfStudents() {
        Faculty faculty = createTestFaculty("Slowpok","Purple");
        Student student = new Student();
        student.setName("Test Student");
        student.setAge(20);
        student.setFaculty(faculty);
        ResponseEntity<Student>createResponse = restTemplate.postForEntity("http://localhost:"+port+"/students",student,Student.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        ResponseEntity<Collection<Student>> response = restTemplate.exchange(getBaseUrl() + "/" + faculty.getId() + "/students", HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Student>>() {});
        assertThat(response.getBody().size()).isNotEqualTo(0);
        assertThat(response.getBody().stream().findFirst().get().getName()).isEqualTo("Test Student");
        assertThat(response.getBody().stream().findFirst().get().getAge()).isEqualTo(20);
    }
}
