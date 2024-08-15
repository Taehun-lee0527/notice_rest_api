package com.api.notice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "notice_attachment")
public class NoticeAttachmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="notice_attachment_no")
    private int noticeAttachmentNo;

    @Column(name="notice_no")
    private int noticeNo;

    @Column(name="file_name")
    private String fileName;

    @Column(name="file_path")
    private String filePath;

    @Column(name="file_size")
    private Long fileSize;

    @Column(name="creator")
    private String creator;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updater")
    private String updater;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;
}
