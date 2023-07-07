package com.tutorlink.api.lesson.service;

import com.tutorlink.api.common.util.Encryption;
import com.tutorlink.api.lesson.domain.ImageFile;
import com.tutorlink.api.lesson.domain.Lesson;
import com.tutorlink.api.lesson.dto.request.AddLessonReq;
import com.tutorlink.api.lesson.dto.request.GetLessonListReq;
import com.tutorlink.api.lesson.dto.request.SearchLessonReq;
import com.tutorlink.api.lesson.dto.request.UpdateLessonReq;
import com.tutorlink.api.lesson.dto.response.GetLessonListRes;
import com.tutorlink.api.lesson.dto.response.SearchLessonRes;
import com.tutorlink.api.lesson.repository.ImageFileRepository;
import com.tutorlink.api.lesson.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

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
        // TODO 강사 이름 저장하는것도 추가
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

    @Override
    public List<GetLessonListRes> getLessonList(GetLessonListReq req) {
        PageRequest pageRequest = PageRequest.of(req.getPage() - 1, 8, Sort.Direction.DESC, "lessonId");
        Page<Lesson> lessonPage = lessonRepository.findAll(pageRequest);

        List<GetLessonListRes> resList = new ArrayList<>();
        for (Lesson lesson : lessonPage) {
            GetLessonListRes res = new GetLessonListRes();
            BeanUtils.copyProperties(lesson, res);
            // TODO likeLesson 추가
            resList.add(res);
        }

        return resList;
    }

    @Override
    public List<SearchLessonRes> searchLesson(SearchLessonReq req) {
        int type = req.getType();
        int page = req.getPage();
        String keyword = req.getKeyword();

        PageRequest pageRequest = PageRequest.of(page - 1, 8, Sort.Direction.DESC, "lessonId");
        Page<Lesson> lessonPage = null;
        //0:제목, 1:강사이름
        if (type == 0) {
            lessonPage = lessonRepository.findByTitleContaining(keyword, pageRequest);
        } else if (type == 1) {
            lessonPage = lessonRepository.findByUserName(keyword, pageRequest);
        }

        List<SearchLessonRes> resList = new ArrayList<>();
        for (Lesson lesson : lessonPage) {
            SearchLessonRes res = new SearchLessonRes();
            BeanUtils.copyProperties(lesson, res);
            // TODO likeLesson 추가
            resList.add(res);
        }

        return resList;
    }

    @Override
    public void updateLesson(int lessonId, UpdateLessonReq req, MultipartFile imageFile) {
//        if (lessonRepository.findByLessonId(lessonId).getUserId() == userId) {
//            changePost(postId, req);
//
//            deleteFiles(attachedFileRepository, attachedFileDir, postId);
//            deleteFiles(imgFileRepository, imgFileDir, postId);
//            surveyRepository.deleteByPostId(postId);
//
//            if (attachedFiles != null) {
//                saveFiles(attachedFiles, attachedFileRepository, attachedFileDir, postId);
//            }
//            if (imgFiles != null) {
//                saveFiles(imgFiles, imgFileRepository, imgFileDir, postId);
//            }
//            if (surveyReq != null) {
//                saveSurveyAndSurveyQuestion(surveyReq, postId, true);
//            }
//        }
    }

    @Override
    public HashMap<String, Object> downloadImageFile(int lessonId) throws MalformedURLException {
        Lesson lesson = lessonRepository.findByLessonId(lessonId);

        HashMap<String, Object> res = new HashMap<>();
        res.put("urlResource", new UrlResource("file:" + imageFileDir + lesson.getImage()));
        res.put("ext", extractExt(lesson.getImage()));

        return res;
    }

    @Override
    public void deleteLesson(int lessonId) {
        lessonRepository.deleteById(lessonId);
    }

    @Override
    public void likeLesson(int lessonId) {

    }

    @Override
    public void cancelLikeLesson(int lessonId) {

    }

    public String extractExt(String uploadFileName) {
        int index = uploadFileName.lastIndexOf('.');
        String ext = null;
        if (index != -1) {
            ext = uploadFileName.substring(index + 1);
        }
        return ext;
    }
}
