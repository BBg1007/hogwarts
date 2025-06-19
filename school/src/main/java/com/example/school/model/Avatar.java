package com.example.school.model;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

@Entity
public class Avatar {

    @Id
    @GeneratedValue
    private Long id;

    private Long fileSize;
    private String filePath;
    private String mediaType;
    @Lob
    private byte[] data;
    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public Avatar() {
    }

    public Avatar(Long id, Long fileSize, String filePath, String mediaType, Student student) {
        this.id = id;
        this.fileSize = fileSize;
        this.filePath = filePath;
        this.mediaType = mediaType;
        this.student = student;
    }

    public Long getId() {
        return id;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getMediaType() {
        return mediaType;
    }

    public Student getStudent() {
        return student;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }


}
