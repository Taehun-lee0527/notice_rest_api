package com.api.notice.request;

import com.api.notice.entity.NoticeAttachmentEntity;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NoticeSaveRequest {
    private int noticeNo;
    private String creator;
    private String updater;
    private String title;
    private String content;
    private LocalDateTime noticeStartDate;
    private LocalDateTime noticeEndDate;

    List<MultipartFile> attachmentList;
    List<NoticeAttachmentEntity> deleteAttachmentList;

}
