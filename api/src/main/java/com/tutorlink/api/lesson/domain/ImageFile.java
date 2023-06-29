package com.tutorlink.api.lesson.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "image_file")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ImageFile {
    @Id
    @NonNull
    @Column(name = "store_file_name", nullable = false)
    String storeFileName;

    @NonNull
    @Column(name = "upload_file_name", nullable = false)
    String uploadFileName;

    @Column(name = "ext")
    String ext;

    @Column(name = "save_time")
    @Temporal(TemporalType.TIMESTAMP)
    Date saveTime;
}
