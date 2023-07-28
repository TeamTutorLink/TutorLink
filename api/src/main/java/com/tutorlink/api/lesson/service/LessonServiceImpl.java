package com.tutorlink.api.lesson.service;

import com.tutorlink.api.common.domain.ImageFile;
import com.tutorlink.api.common.repository.ImageFileRepository;
import com.tutorlink.api.common.util.Encryption;
import com.tutorlink.api.lesson.domain.Lesson;
import com.tutorlink.api.lesson.domain.UserLessonLike;
import com.tutorlink.api.lesson.dto.request.AddLessonReq;
import com.tutorlink.api.lesson.dto.request.UpdateLessonReq;
import com.tutorlink.api.lesson.dto.response.GetLessonListLoginRes;
import com.tutorlink.api.lesson.dto.response.GetLessonListRes;
import com.tutorlink.api.lesson.dto.response.SearchLessonLoginRes;
import com.tutorlink.api.lesson.dto.response.SearchLessonRes;
import com.tutorlink.api.lesson.exception.ImageNotFoundException;
import com.tutorlink.api.lesson.exception.LessonNotFoundException;
import com.tutorlink.api.lesson.exception.NotTeacherException;
import com.tutorlink.api.lesson.exception.UserNotMatchingException;
import com.tutorlink.api.lesson.repository.LessonRepository;
import com.tutorlink.api.lesson.repository.UserLessonLikeRepository;
import com.tutorlink.api.user.domain.User;
import com.tutorlink.api.user.enumeration.UserType;
import com.tutorlink.api.user.exception.UserNotFoundException;
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

    @Value("${lesson.image.file.dir}")
    private String lessonImageFileDir;

    @Override
    @Transactional
    public Lesson addLesson(User user, AddLessonReq req, MultipartFile imageFile) throws IOException, NoSuchAlgorithmException, NotTeacherException {
        if (user.getUserType() != UserType.TEACHER) {
            throw new NotTeacherException();
        }
        Lesson lesson = new Lesson();
        BeanUtils.copyProperties(req, lesson);
        if (lesson.getPassword() != null) {
            lesson.setPassword(encryption.SHA256(lesson.getPassword()));
        }
        lesson.setCreateTime(new Date());
        lesson.setUser(user);

        if (imageFile != null) {
            String uploadFileName = imageFile.getOriginalFilename();
            String ext = extractExt(uploadFileName);
            String storeFileName = UUID.randomUUID().toString() + "." + ext;

            imageFileRepository.save(new ImageFile(storeFileName, uploadFileName, ext, new Date()));

            String fullPath = lessonImageFileDir + storeFileName;
            imageFile.transferTo(new File(fullPath));

            lesson.setImage(storeFileName);
        }

        return lessonRepository.save(lesson);
    }

    @Override
    public List<GetLessonListLoginRes> getLessonListLogin(User user, int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 8, Sort.Direction.DESC, "lessonId");
        Page<Lesson> lessonPage = lessonRepository.findAll(pageRequest);

        List<GetLessonListLoginRes> resList = new ArrayList<>();
        List<Lesson> lessonList = new ArrayList<>();
        for (Lesson lesson : lessonPage) {
            GetLessonListLoginRes res = new GetLessonListLoginRes();
            BeanUtils.copyProperties(lesson, res);
            res.setUserName(lesson.getUser().getUserName());
            resList.add(res);
            lessonList.add(lesson);
        }
        List<UserLessonLike> userLessonLikeList = userLessonLikeRepository.findAllByUserAndLessonIn(user, lessonList);
        for (UserLessonLike userLessonLike : userLessonLikeList) {
            int lessonId = userLessonLike.getLesson().getLessonId();
            for (GetLessonListLoginRes res : resList) {
                if (res.getLessonId() == lessonId) {
                    res.setLikeLesson(true);
                }
            }
        }

        return resList;
    }

    @Override
    public List<GetLessonListRes> getLessonList(int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 8, Sort.Direction.DESC, "lessonId");
        Page<Lesson> lessonPage = lessonRepository.findAll(pageRequest);

        List<GetLessonListRes> resList = new ArrayList<>();
        for (Lesson lesson : lessonPage) {
            GetLessonListRes res = new GetLessonListRes();
            BeanUtils.copyProperties(lesson, res);
            res.setUserName(lesson.getUser().getUserName());
            resList.add(res);
        }

        return resList;
    }

    @Override
    public HashMap<String, Object> downloadImageFile(int lessonId) throws MalformedURLException, ImageNotFoundException, LessonNotFoundException {
        Optional<Lesson> lesson = lessonRepository.findById(lessonId);
        if (lesson.isEmpty()) {
            throw new LessonNotFoundException();
        }
        if (lesson.get().getImage() == null) {
            throw new ImageNotFoundException();
        }

        HashMap<String, Object> res = new HashMap<>();
        res.put("urlResource", new UrlResource("file:" + lessonImageFileDir + lesson.get().getImage()));
        res.put("ext", extractExt(lesson.get().getImage()));

        return res;
    }

    @Override
    public List<SearchLessonLoginRes> searchLessonLogin(User user, int type, String keyword, int page) throws UserNotFoundException {
        PageRequest pageRequest = PageRequest.of(page - 1, 8, Sort.Direction.DESC, "lessonId");
        Page<Lesson> lessonPage = null;
        //0:제목, 1:강사이름
        if (type == 0) {
            lessonPage = lessonRepository.findByTitleContaining(keyword, pageRequest);
        } else if (type == 1) {
            Optional<User> userOpt = userRepository.findByUserName(keyword);
            if (userOpt.isEmpty()) {
                throw new UserNotFoundException();
            }
            lessonPage = lessonRepository.findByUser(userOpt.get(), pageRequest);
        }

        List<SearchLessonLoginRes> resList = new ArrayList<>();
        List<Lesson> lessonList = new ArrayList<>();
        for (Lesson lesson : lessonPage) {
            SearchLessonLoginRes res = new SearchLessonLoginRes();
            BeanUtils.copyProperties(lesson, res);
            res.setUserName(lesson.getUser().getUserName());
            resList.add(res);
            lessonList.add(lesson);
        }

        List<UserLessonLike> userLessonLikeList = userLessonLikeRepository.findAllByUserAndLessonIn(user, lessonList);
        for (UserLessonLike userLessonLike : userLessonLikeList) {
            int lessonId = userLessonLike.getLesson().getLessonId();
            for (SearchLessonLoginRes res : resList) {
                if (res.getLessonId() == lessonId) {
                    res.setLikeLesson(true);
                }
            }
        }

        return resList;
    }

    @Override
    public List<SearchLessonRes> searchLesson(int type, String keyword, int page) throws UserNotFoundException {
        PageRequest pageRequest = PageRequest.of(page - 1, 8, Sort.Direction.DESC, "lessonId");
        Page<Lesson> lessonPage = null;
        //0:제목, 1:강사이름
        if (type == 0) {
            lessonPage = lessonRepository.findByTitleContaining(keyword, pageRequest);
        } else if (type == 1) {
            Optional<User> userOpt = userRepository.findByUserName(keyword);
            if (userOpt.isEmpty()) {
                throw new UserNotFoundException();
            }
            lessonPage = lessonRepository.findByUser(userOpt.get(), pageRequest);
        }

        List<SearchLessonRes> resList = new ArrayList<>();
        for (Lesson lesson : lessonPage) {
            SearchLessonRes res = new SearchLessonRes();
            BeanUtils.copyProperties(lesson, res);
            res.setUserName(lesson.getUser().getUserName());
            resList.add(res);
        }

        return resList;
    }

    @Override
    @Transactional
    public Lesson updateLesson(User user, int lessonId, UpdateLessonReq req, MultipartFile imageFile) throws UserNotMatchingException, IOException, NoSuchAlgorithmException, LessonNotFoundException {
        Optional<Lesson> lessonOpt = lessonRepository.findById(lessonId);
        if (lessonOpt.isEmpty()) {
            throw new LessonNotFoundException();
        }
        if (lessonOpt.get().getUser().getUserId() != user.getUserId()) {
            throw new UserNotMatchingException();
        }
        BeanUtils.copyProperties(req, lessonOpt.get());
        if (lessonOpt.get().getPassword() != null) {
            lessonOpt.get().setPassword(encryption.SHA256(lessonOpt.get().getPassword()));
        }

        if (imageFile != null) {
            String uploadFileName = imageFile.getOriginalFilename();
            String ext = extractExt(uploadFileName);
            String storeFileName = UUID.randomUUID().toString() + "." + ext;

            imageFileRepository.save(new ImageFile(storeFileName, uploadFileName, ext, new Date()));

            String fullPath = lessonImageFileDir + storeFileName;
            imageFile.transferTo(new File(fullPath));

            lessonOpt.get().setImage(storeFileName);
        }

        return lessonOpt.get();
    }

    @Override
    public void deleteLesson(User user, int lessonId) throws UserNotMatchingException, LessonNotFoundException {
        Optional<Lesson> lessonOpt = lessonRepository.findById(lessonId);
        if (lessonOpt.isEmpty()) {
            throw new LessonNotFoundException();
        }
        if (lessonOpt.get().getUser().getUserId() != user.getUserId()) {
            throw new UserNotMatchingException();
        }
        lessonRepository.deleteById(lessonId);
    }

    @Override
    @Transactional
    public void likeLesson(User user, int lessonId) throws LessonNotFoundException {
        Optional<Lesson> lessonOpt = lessonRepository.findById(lessonId);
        if (lessonOpt.isEmpty()) {
            throw new LessonNotFoundException();
        }
        Optional<UserLessonLike> userLessonLikeOpt = userLessonLikeRepository.findByUserAndLesson(user, lessonOpt.get());
        if (userLessonLikeOpt.isEmpty()) {
            userLessonLikeOpt = Optional.of(new UserLessonLike(user, lessonOpt.get()));
            userLessonLikeRepository.save(userLessonLikeOpt.get());

            lessonOpt.get().setLikeCount(lessonOpt.get().getLikeCount() + 1);
        }
    }

    @Override
    @Transactional
    public void cancelLikeLesson(User user, int lessonId) throws LessonNotFoundException {
        Optional<Lesson> lessonOpt = lessonRepository.findById(lessonId);
        if (lessonOpt.isEmpty()) {
            throw new LessonNotFoundException();
        }
        Optional<UserLessonLike> userLessonLikeOpt = userLessonLikeRepository.findByUserAndLesson(user, lessonOpt.get());
        if (userLessonLikeOpt.isPresent()) {
            userLessonLikeRepository.deleteByUserAndLesson(user, lessonOpt.get());

            lessonOpt.get().setLikeCount(lessonOpt.get().getLikeCount() - 1);
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
