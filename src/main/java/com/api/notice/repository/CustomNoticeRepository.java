package com.api.notice.repository;

import com.api.notice.request.NoticeSaveRequest;
import com.api.notice.request.NoticeSearchRequest;
import com.api.notice.response.NoticeSearchResponse;

public interface CustomNoticeRepository {
    NoticeSearchResponse searchNotice(NoticeSearchRequest noticeSearchRequest);

    int createNotice(NoticeSaveRequest noticeSaveRequest) throws Exception;

    void updateNotice(NoticeSaveRequest noticeSaveRequest) throws Exception;

    void increaseViewCount(int noticeNo) throws Exception;
}
