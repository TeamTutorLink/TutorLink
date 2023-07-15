package com.tutorlink.api.lesson.service;

import com.tutorlink.api.common.domain.ImageFile;
import com.tutorlink.api.common.repository.ImageFileRepository;
import com.tutorlink.api.common.util.Encryption;
import com.tutorlink.api.lesson.domain.Lesson;
import com.tutorlink.api.lesson.domain.UserLessonLike;
import com.tutorlink.api.lesson.dto.request.*;
import com.tutorlink.api.lesson.dto.response.GetLessonListLoginRes;
import com.tutorlink.api.lesson.dto.response.GetLessonListRes;
import com.tutorlink.api.lesson.dto.response.SearchLessonLoginRes;
import com.tutorlink.api.lesson.dto.response.SearchLessonRes;
import com.tutorlink.api.lesson.exception.NotTeacherException;
import com.tutorlink.api.lesson.exception.UserNotMatchingException;
import com.tutorlink.api.lesson.repository.LessonRepository;
import com.tutorlink.api.lesson.repository.UserLessonLikeRepository;
import com.tutorlink.api.user.domain.User;
import com.tutorlink.api.user.enumeration.UserType;
import com.tutorlink.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final UserRepository userRepository;
    private final UserLessonLikeRepository userLessonLikeRepository;
    private final ImageFileRepository imageFileRepository;
    private final Encryption encryption;

    @Value("${image.file.dir}")
    private String imageFileDir;

    @Override
    @Transactional
    public Lesson addLesson(int userId, AddLessonReq req, MultipartFile imageFile) throws IOException, NoSuchAlgorithmException, NotTeacherException {
        User user = userRepository.findById(userId).get();
        if (user.getUserType() != UserType.TEACHER) {
            throw new NotTeacherException();
        }
        Lesson lesson = new Lesson();
        BeanUtils.copyProperties(req, lesson);
        lesson.setPassword(encryption.SHA256(lesson.getPassword()));
        lesson.setCreateTime(new Date());
        lesson.setUserId(userId);
        lesson.setUserName(user.getUserName());

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
    public List<GetLessonListLoginRes> getLessonListLogin(int userId, GetLessonListLoginReq req) {
        PageRequest pageRequest = PageRequest.of(req.getPage() - 1, 8, Sort.Direction.DESC, "lessonId");
        Page<Lesson> lessonPage = lessonRepository.findAll(pageRequest);

        List<GetLessonListLoginRes> resList = new ArrayList<>();
        List<Integer> lessonIdList = new ArrayList<>();
        for (Lesson lesson : lessonPage) {
            GetLessonListLoginRes res = new GetLessonListLoginRes();
            BeanUtils.copyProperties(lesson, res);
            resList.add(res);
            lessonIdList.add(lesson.getLessonId());
        }
        List<UserLessonLike> userLessonLikeList = userLessonLikeRepository.findAllByUserIdAndLessonIdIn(userId, lessonIdList);
        for (UserLessonLike userLessonLike : userLessonLikeList) {
            int lessonId = userLessonLike.getLessonId();
            for (GetLessonListLoginRes res : resList) {
                if (res.getLessonId() == lessonId) {
                    res.setLikeLesson(true);
                }
            }
        }

        return resList;
    }

    @Override
    public List<GetLessonListRes> getLessonList(GetLessonListReq req) {
        PageRequest pageRequest = PageRequest.of(req.getPage() - 1, 8, Sort.Direction.DESC, "lessonId");
        Page<Lesson> lessonPage = lessonRepository.findAll(pageRequest);

        List<GetLessonListRes> resList = new ArrayList<>();
        for (Lesson lesson : lessonPage) {
            GetLessonListRes res = new GetLessonListRes();
            BeanUtils.copyProperties(lesson, res);
            resList.add(res);
        }

        return resList;
    }

    @Override
    public HashMap<String, Object> downloadImageFile(int lessonId) throws MalformedURLException {
        Lesson lesson = lessonRepository.findById(lessonId).get();

        HashMap<String, Object> res = new HashMap<>();
        res.put("urlResource", new UrlResource("file:" + imageFileDir + lesson.getImage()));
        res.put("ext", extractExt(lesson.getImage()));

        return res;
    }

    @Override
    public List<SearchLessonLoginRes> searchLessonLogin(int userId, SearchLessonLoginReq req) {
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

        List<SearchLessonLoginRes> resList = new ArrayList<>();
        List<Integer> lessonIdList = new ArrayList<>();
        for (Lesson lesson : lessonPage) {
            SearchLessonLoginRes res = new SearchLessonLoginRes();
            BeanUtils.copyProperties(lesson, res);
            resList.add(res);
            lessonIdList.add(lesson.getLessonId());
        }

        List<UserLessonLike> userLessonLikeList = userLessonLikeRepository.findAllByUserIdAndLessonIdIn(userId, lessonIdList);
        for (UserLessonLike userLessonLike : userLessonLikeList) {
            int lessonId = userLessonLike.getLessonId();
            for (SearchLessonLoginRes res : resList) {
                if (res.getLessonId() == lessonId) {
                    res.setLikeLesson(true);
                }
            }
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
            resList.add(res);
        }

        return resList;
    }

    @Override
    @Transactional
    public Lesson updateLesson(int userId, int lessonId, UpdateLessonReq req, MultipartFile imageFile) throws UserNotMatchingException, IOException, NoSuchAlgorithmException {
        Lesson lesson = lessonRepository.findById(lessonId).get();
        if (lesson.getUserId() != userId) {
            throw new UserNotMatchingException();
        }
        BeanUtils.copyProperties(req, lesson);
        lesson.setPassword(encryption.SHA256(lesson.getPassword()));

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
    public void deleteLesson(int userId, int lessonId) throws UserNotMatchingException {
        Lesson lesson = lessonRepository.findById(lessonId).get();
        if (lesson.getUserId() != userId) {
            throw new UserNotMatchingException();
        }
        lessonRepository.deleteById(lessonId);
    }

    @Override
    @Transactional
    public void likeLesson(int userId, int lessonId) {
        UserLessonLike userLessonLike = userLessonLikeRepository.findByUserIdAndLessonId(userId, lessonId);
        if (userLessonLike == null) {
            userLessonLike = new UserLessonLike(userId, lessonId);
            userLessonLikeRepository.save(userLessonLike);

            Lesson lesson = lessonRepository.findById(lessonId).get();
            lesson.setLikeCount(lesson.getLikeCount() + 1);
            lessonRepository.save(lesson);
        }
    }

    @Override
    @Transactional
    public void cancelLikeLesson(int userId, int lessonId) {
        UserLessonLike userLessonLike = userLessonLikeRepository.findByUserIdAndLessonId(userId, lessonId);
        if (userLessonLike != null) {
            userLessonLikeRepository.deleteByUserIdAndLessonId(userId, lessonId);

            Lesson lesson = lessonRepository.findById(lessonId).get();
            lesson.setLikeCount(lesson.getLikeCount() - 1);
            lessonRepository.save(lesson);
        }
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
