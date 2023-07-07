package com.tutorlink.api.lesson.repository;

import com.tutorlink.api.lesson.domain.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile, String> {

    ImageFile findByStoreFileName(String storeFileName);
}
