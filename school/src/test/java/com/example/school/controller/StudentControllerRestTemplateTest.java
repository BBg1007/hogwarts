package com.example.school.controller;

import com.example.school.model.Faculty;
import com.example.school.model.Student;
import com.example.school.repositories.StudentRepository;
import org.assertj.core.api.Assertions;
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

import java.util.Collection;
import java.util.Iterator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

    @ActiveProfiles("test")
    @ExtendWith(SpringExtension.class)
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @Transactional
public class StudentControllerRestTemplateTest {

        @LocalServerPort
        private int port;
        @Autowired
        private StudentController studentController;
        @Autowired
        private TestRestTemplate restTemplate;
        @Autowired
        private StudentRepository studentRepository;

        @AfterEach
        public void clearDb() {
            ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                    getBaseUrl(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Collection<Student>>() {});
            response.getBody().forEach(student -> restTemplate.delete(getBaseUrl()+"/"+student.getId()));
        }

        private String getBaseUrl() {
            return "http://localhost:"+port+"/students";
        }

        private Student createTestStudent(String name,int age) {
            Student student = new Student();
            student.setName(name);
            student.setAge(age);
            ResponseEntity<Student> response = restTemplate.postForEntity(getBaseUrl(),student,Student.class);
            return response.getBody();
        }
        @Test
        public void contextLoads() throws Exception {
            Assertions.assertThat(studentController).isNotNull();
        }
        @Test
        public void shouldCreateStudent() {
            Student newStudent = new Student();
            newStudent.setName("Peter");
            newStudent.setAge(17);
            ResponseEntity<Student> response = restTemplate.postForEntity(getBaseUrl(),
                    newStudent,
                    Student.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Student created = response.getBody();
            assertThat(created.getId()).isNotNull();
            assertThat(created.getName()).isEqualTo("Peter");
        }

        @Test
        public void shouldFindStudent() {
            Student testStudent = createTestStudent("John",20);
            ResponseEntity<Student> response = restTemplate.getForEntity(getBaseUrl()+"/"+testStudent.getId(),
                    Student.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getName()).isEqualTo(testStudent.getName());

        }
        @Test
        public void shouldEditCreatedStudent() {
            Student testStudent = createTestStudent("John",20);
            Student newStudent = new Student();
            newStudent.setName("Peter");
            newStudent.setAge(17);
            newStudent.setId(testStudent.getId());
            ResponseEntity<Student> response = restTemplate.exchange(getBaseUrl(), HttpMethod.PUT,new HttpEntity<>(newStudent),Student.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().getId()).isEqualTo(newStudent.getId());
            assertThat(response.getBody().getName()).isEqualTo(newStudent.getName());
            assertThat(response.getBody().getAge()).isEqualTo(newStudent.getAge());

        }
        @Test
        public void shouldDeleteCreatedStudent() {
            Student testStudent = createTestStudent("John",20);
            ResponseEntity<Void> delResponse = restTemplate.exchange(
                    getBaseUrl() +"/"+ testStudent.getId(),
                    HttpMethod.DELETE,
                    null,
                    Void.class
            );
            assertThat(delResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

            ResponseEntity<Student> response = restTemplate.getForEntity(getBaseUrl()+"/"+testStudent.getId(),
                    Student.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        }
        @Test
        public void ShouldReturnCorrectCollectionOfStudent() {
            Student student1 = createTestStudent("John",20);
            Student student2 = createTestStudent("Peter",17);
            ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                    getBaseUrl(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Collection<Student>>() {});

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().size()).isEqualTo(2);
            Iterator<Student > studentIterator = response.getBody().iterator();
            assertThat(studentIterator.next().getName()).isEqualTo("John");
            assertThat(studentIterator.next().getName()).isEqualTo("Peter");
        }
        @Test
        public void ShouldReturnRequiredStudent() {
            Student student1 = createTestStudent("John",20);
            Student student2 = createTestStudent("Peter",17);
            String urlForAgeParam = "/by-age?age=";
            ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                    getBaseUrl()+urlForAgeParam+student1.getAge(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Collection<Student>>() {});
            assertThat(response.getBody().size()).isEqualTo(1);
            assertThat(response.getBody().contains(student1)).isTrue();
            assertThat(response.getBody().contains((student2))).isFalse();

            response = restTemplate.exchange(
                    getBaseUrl()+urlForAgeParam+"0&minAge="+(student2.getAge()-1)+"&maxAge="+(student2.getAge()+1),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Collection<Student>>() {});
            assertThat(response.getBody().size()).isEqualTo(1);
            assertThat(response.getBody().contains(student2)).isTrue();
            assertThat(response.getBody().contains((student1))).isFalse();
        }
        @Test
        public void shouldReturnRightFaculty() {
            Faculty faculty = new Faculty();
            faculty.setName("Test");
            faculty.setColor("Random");
            ResponseEntity<Faculty> createResponse = restTemplate.postForEntity("http://localhost:"+port+"/faculty",faculty,Faculty.class);
            assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            faculty.setId(createResponse.getBody().getId());
            Student student = new Student();
            student.setName("John Doe");
            student.setAge(33);
            student.setFaculty(faculty);
            student = restTemplate.postForEntity(getBaseUrl(),student,Student.class).getBody();
            ResponseEntity<Faculty>response = restTemplate.exchange(getBaseUrl()+"/faculty?id="+student.getId(),HttpMethod.GET,null,Faculty.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().getName()).isEqualTo(faculty.getName());
        }

    }

