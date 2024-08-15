package com.api.notice.controller;

import com.api.notice.entity.NoticeAttachmentEntity;
import com.api.notice.request.NoticeSaveRequest;
import com.api.notice.request.NoticeSearchRequest;
import com.api.notice.response.NoticeDetailResponse;
import com.api.notice.response.NoticeSearchResponse;
import com.api.notice.service.NoticeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class NoticeRestController {
    private final NoticeService noticeService;

    @GetMapping("/api/notices")
    @ResponseBody
    public NoticeSearchResponse getNoticeList(
            @ModelAttribute NoticeSearchRequest noticeSearchRequest
    ) {
        return noticeService.getNoticeList(noticeSearchRequest);
    }

    @GetMapping("/api/notices/{noticeNo}")
    @ResponseBody
    public NoticeDetailResponse getNotice(
            @PathVariable int noticeNo
    ) throws Exception {
        try {
            return noticeService.getNotice(noticeNo);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping("/api/notices")
    public int createNotice(
            @RequestPart("creator") String creator,
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart(value = "noticeStartDate", required = false) String noticeStartDate,
            @RequestPart(value = "noticeEndDate", required = false) String noticeEndDate,
            @RequestPart(value = "attachmentList", required = false) List<MultipartFile> attachmentList
    ) throws Exception {
        NoticeSaveRequest noticeSaveRequest = new NoticeSaveRequest();
        noticeSaveRequest.setCreator(creator);
        noticeSaveRequest.setTitle(title);
        noticeSaveRequest.setContent(content);
        if(StringUtils.isNotEmpty(noticeStartDate)) noticeSaveRequest.setNoticeStartDate(LocalDateTime.parse(noticeStartDate));
        if(StringUtils.isNotEmpty(noticeEndDate)) noticeSaveRequest.setNoticeEndDate(LocalDateTime.parse(noticeEndDate));
        noticeSaveRequest.setAttachmentList(attachmentList);
        return noticeService.createNotice(noticeSaveRequest);
    }

    @PutMapping("/api/notices/{noticeNo}")
    public void updateNotice(
            @PathVariable("noticeNo") int noticeNo,
            @RequestPart("updater") String updater,
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart(value = "noticeStartDate", required = false) String noticeStartDate,
            @RequestPart(value = "noticeEndDate", required = false) String noticeEndDate,
            @RequestPart(value = "deleteAttachmentList", required = false) String deleteAttachmentListStr,
            @RequestPart(value = "attachmentList", required = false) List<MultipartFile> attachmentList
    ) throws Exception {
        NoticeSaveRequest noticeSaveRequest = new NoticeSaveRequest();
        noticeSaveRequest.setNoticeNo(noticeNo);
        noticeSaveRequest.setTitle(title);
        noticeSaveRequest.setContent(content);
        if(StringUtils.isNotEmpty(noticeStartDate)) noticeSaveRequest.setNoticeStartDate(LocalDateTime.parse(noticeStartDate));
        if(StringUtils.isNotEmpty(noticeEndDate)) noticeSaveRequest.setNoticeEndDate(LocalDateTime.parse(noticeEndDate));
        noticeSaveRequest.setCreator(updater);
        noticeSaveRequest.setUpdater(updater);
        noticeSaveRequest.setAttachmentList(attachmentList);

        if(StringUtils.isNotEmpty(deleteAttachmentListStr)){
            ObjectMapper objectMapper = new ObjectMapper();
            List<NoticeAttachmentEntity> deleteAttachmentList = objectMapper.readValue(deleteAttachmentListStr, new TypeReference<List<NoticeAttachmentEntity>>() {});
            noticeSaveRequest.setDeleteAttachmentList(deleteAttachmentList);
        }
        noticeService.updateNotice(noticeSaveRequest);
    }

    @DeleteMapping("/api/notices")
    public void deleteNotices(
            @RequestParam List<Integer> noticeNoList
    ) throws Exception {
        noticeService.deleteNotices(noticeNoList);
    }

    @PatchMapping("/api/notices/{noticeNo}/increase-view-count")
    public void increaseViewCount(
            @PathVariable("noticeNo") int noticeNo
    ) throws Exception {
        try {
            noticeService.increaseViewCount(noticeNo);
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }
}
