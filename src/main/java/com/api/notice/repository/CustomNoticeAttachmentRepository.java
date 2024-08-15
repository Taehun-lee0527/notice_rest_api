package com.api.notice.repository;

import com.api.notice.entity.NoticeAttachmentEntity;

import java.util.List;

public interface CustomNoticeAttachmentRepository {
    List<NoticeAttachmentEntity> findAllByNoticeNoList(List<Integer> noticeNoList);
}
