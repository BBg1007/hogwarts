package com.example.school.service;

import com.example.school.model.Avatar;
import com.example.school.model.Student;
import com.example.school.repositories.AvatarRepository;
import com.example.school.repositories.StudentRepository;
import com.example.school.util.LogHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static io.swagger.v3.core.util.AnnotationsUtils.getExtensions;
import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {
    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;
    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    public AvatarService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;

    }

    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        LogHelper.logMethodAndArgsLvlDebug("uploadAvatar",studentId);
        Student student = studentRepository.findById(studentId).get();
        Path filePath = Path.of(avatarsDir,student+"."+getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is,1024);
                BufferedOutputStream bos = new BufferedOutputStream(os,1024);
                ){
            bis.transferTo(bos);
        }

        Avatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        avatarRepository.save(avatar);

    }

    public String getExtensions(String fileName) {
        LogHelper.logMethodAndArgsLvlDebug("uploadAvatar",fileName);
        return fileName.substring(fileName.lastIndexOf(".")+1);
    }

    public Avatar findAvatar(Long studentId) {
        LogHelper.logMethodAndArgsLvlDebug("uploadAvatar",studentId);
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

    public List<Avatar> getAllAvatars (Integer pageNumber, Integer pageSize) {
        LogHelper.logMethodAndArgsLvlDebug("uploadAvatar",pageNumber,pageSize);
        PageRequest pageRequest = PageRequest.of(pageNumber-1,pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }
}
