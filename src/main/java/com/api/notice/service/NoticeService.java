package com.api.notice.service;

import com.api.notice.request.NoticeSaveRequest;
import com.api.notice.request.NoticeSearchRequest;
import com.api.notice.response.NoticeDetailResponse;
import com.api.notice.response.NoticeSearchResponse;

import java.util.List;


public interface NoticeService {
    //조회
    NoticeSearchResponse getNoticeList(NoticeSearchRequest noticeSearchRequest);

    //상세조회
    NoticeDetailResponse getNotice(int noticeNo) throws Exception;

    //등록
    int createNotice(NoticeSaveRequest noticeSaveRequest) throws Exception;


    //수정
    void updateNotice(NoticeSaveRequest noticeSaveRequest) throws Exception;

    //삭제
    void deleteNotices(List<Integer> noticeNoList) throws Exception;

    //상세 클릭 시 viewCount 증가
    void increaseViewCount(int noticeNo) throws Exception;
}
