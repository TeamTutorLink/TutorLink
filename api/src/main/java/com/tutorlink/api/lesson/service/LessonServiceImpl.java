package com.tutorlink.api.lesson.service;

import com.tutorlink.api.common.util.Encryption;
import com.tutorlink.api.lesson.domain.ImageFile;
import com.tutorlink.api.lesson.domain.Lesson;
import com.tutorlink.api.lesson.dto.request.AddLessonReq;
import com.tutorlink.api.lesson.repository.ImageFileRepository;
import com.tutorlink.api.lesson.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final ImageFileRepository imageFileRepository;
    private final Encryption encryption;

    @Value("${image.file.dir}")
    private String imageFileDir;

    @Override
    public Lesson addLesson(AddLessonReq req, MultipartFile imageFile) throws IOException, NoSuchAlgorithmException {
        Lesson lesson = new Lesson();
        BeanUtils.copyProperties(req, lesson);
        lesson.setPassword(encryption.SHA256(lesson.getPassword()));
        lesson.setCreateTime(new Date());

        if (imageFile != null) {
            String uploadFileName = imageFile.getOriginalFilename();
            String ext = extractExt(uploadFileName);
            String storeFileName = UUID.randomUUID().toString() + "." + ext;

            imageFileRepository.save(new ImageFile(storeFileName, uploadFileName, ext, new Date()));

            String fullPath = imageFileDir + storeFileName;
            imageFile.transferTo(new File(fullPath));

            lesson.setImage(storeFileName);
        }

        return lessonRepository.save(lesson);
    }

    private String extractExt(String uploadFileName) {
        int index = uploadFileName.lastIndexOf('.');
        String ext = null;
        if (index != -1) {
            ext = uploadFileName.substring(index + 1);
        }
        return ext;
    }
}
