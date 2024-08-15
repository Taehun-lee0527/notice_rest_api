package com.api.notice.repository;

import com.api.notice.entity.NoticeAttachmentEntity;
import com.api.notice.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeAttachmentRepository extends JpaRepository<NoticeAttachmentEntity, Integer> {
    List<NoticeAttachmentEntity> findAllByNoticeNo(int noticeNo);

}
