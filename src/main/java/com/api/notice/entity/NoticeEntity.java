package com.api.notice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "notice")
public class NoticeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="notice_no")
    private int noticeNo;

    @Column(name="title")
    private String title;

    @Column(name="content")
    private String content;

    @Column(name="notice_start_date")
    private LocalDateTime noticeStartDate;

    @Column(name="notice_end_date")
    private LocalDateTime noticeEndDate;

    @Column(name="view_count")
    private int viewCount;

    @Column(name="creator")
    private String creator;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updater")
    private String updater;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;
}
