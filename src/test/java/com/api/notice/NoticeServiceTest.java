package com.api.notice;

import com.api.notice.entity.NoticeAttachmentEntity;
import com.api.notice.entity.NoticeEntity;
import com.api.notice.repository.NoticeAttachmentRepository;
import com.api.notice.repository.NoticeRepository;
import com.api.notice.request.NoticeSaveRequest;
import com.api.notice.request.NoticeSearchRequest;
import com.api.notice.response.NoticeDetailResponse;
import com.api.notice.response.NoticeSearchResponse;
import com.api.notice.service.impl.NoticeServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NoticeServiceTest {


    private final String LOGINID = "test";

    @InjectMocks
    NoticeServiceImpl noticeService;
    @Mock
    NoticeRepository noticeRepository;

    @Mock
    NoticeAttachmentRepository noticeAttachmentRepository;

    @BeforeEach
    void init() {

        MockitoAnnotations.openMocks(this);
        List<NoticeEntity> noticeEntityList = new ArrayList<>();

        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setNoticeNo(100);
        noticeEntity.setTitle("test_title1");
        noticeEntity.setContent("test_content1");
        noticeEntity.setCreator(LOGINID);
        noticeEntity.setNoticeStartDate(LocalDateTime.now());
        noticeEntity.setNoticeEndDate(LocalDateTime.of(2022, 12, 31, 3, 2, 4));
        noticeEntity.setCreatedAt(LocalDateTime.now());
        noticeEntityList.add(noticeEntity);

        when(noticeRepository.findById(100)).thenReturn(Optional.of(noticeEntity));

        NoticeAttachmentEntity noticeAttachmentEntity = new NoticeAttachmentEntity();
        noticeAttachmentEntity.setNoticeAttachmentNo(1);
        noticeAttachmentEntity.setNoticeNo(100);
        noticeAttachmentEntity.setFileName("test.jpg");
        noticeAttachmentEntity.setFilePath("/user/home/test.jpg");
        noticeAttachmentEntity.setFileSize(100L);
        noticeAttachmentEntity.setCreator(LOGINID);
        noticeAttachmentEntity.setCreatedAt(LocalDateTime.now());

        List<NoticeAttachmentEntity> noticeAttachmentEntityList = new ArrayList<>();
        noticeAttachmentEntityList.add(noticeAttachmentEntity);

        when(noticeAttachmentRepository.findAllByNoticeNo(100)).thenReturn(noticeAttachmentEntityList);

        NoticeEntity noticeEntity2 = new NoticeEntity();
        noticeEntity2.setNoticeNo(101);
        noticeEntity2.setTitle("test_title2");
        noticeEntity2.setContent("test_content2");
        noticeEntity2.setCreator(LOGINID);
        noticeEntity2.setNoticeStartDate(LocalDateTime.now());
        noticeEntity2.setNoticeEndDate(LocalDateTime.of(2022, 12, 31, 3, 2, 4));
        noticeEntityList.add(noticeEntity2);

        // Mocking repository method
        when(noticeRepository.searchNotice(any(NoticeSearchRequest.class)))
                .thenReturn(NoticeSearchResponse.of(true, noticeEntityList, 1, 2));
    }
    @Test
    @DisplayName("공지사항 목록 조회")
    void getNotices() {
        NoticeSearchRequest noticeSearchRequest = new NoticeSearchRequest();
        noticeSearchRequest.setPerPage(20);
        noticeSearchRequest.setPage(1);

        NoticeSearchResponse response = noticeService.getNoticeList(noticeSearchRequest);

        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getData(), "Data should not be null");
        assertEquals(2, response.getData().getContents().size(), "There should be 2 notices");
    }

    @Test
    @DisplayName("공지사항 단건 조회")
    void getNotice() throws Exception {
        NoticeDetailResponse noticeDetailResponse = noticeService.getNotice(100);

        assertNotNull(noticeDetailResponse, "Response should not be null");
    }

    @Test
    @DisplayName("공지사항 단건 조회 - 공지사항 없음")
    void getNotice_notFound() {
        when(noticeRepository.findById(999)).thenThrow(new EntityNotFoundException("존재하지 않는 공지사항입니다."));

        EntityNotFoundException thrownException = assertThrows(EntityNotFoundException.class, () -> {
            // This line should throw the exception
            noticeService.getNotice(999);
        });

        System.out.println("Exception message: " + thrownException.getMessage());
    }

    @Test
    @DisplayName("공지사항 추가")
    void create() throws Exception {
        NoticeSaveRequest noticeSaveRequest = new NoticeSaveRequest();
        noticeSaveRequest.setNoticeNo(102);
        noticeSaveRequest.setTitle("test_title3");
        noticeSaveRequest.setContent("test_content3");
        noticeSaveRequest.setNoticeStartDate(LocalDateTime.now());
        noticeSaveRequest.setNoticeEndDate(LocalDateTime.of(2031, 12, 22, 3, 4, 5));

        when(noticeRepository.createNotice(any(NoticeSaveRequest.class))).thenReturn(102);
        int noticeNo = noticeService.createNotice(noticeSaveRequest);

        assertThat(noticeNo).isEqualTo(102);
    }

    @Test
    @DisplayName("공지사항 수정")
    void edit() throws Exception {
        NoticeSaveRequest noticeSaveRequest = new NoticeSaveRequest();
        noticeSaveRequest.setNoticeNo(100);
        noticeSaveRequest.setTitle("updated_title1");
        noticeSaveRequest.setContent("updated_content1");
        noticeSaveRequest.setNoticeStartDate(LocalDateTime.now());
        noticeSaveRequest.setNoticeEndDate(LocalDateTime.of(2031, 12, 22, 3, 4, 5));

        // Mocking repository method
        doNothing().when(noticeRepository).updateNotice(noticeSaveRequest);

        // When
        noticeService.updateNotice(noticeSaveRequest);

        // Then
        verify(noticeRepository, times(1)).updateNotice(noticeSaveRequest);

        verifyNoMoreInteractions(noticeRepository);
    }

    @Test
    @DisplayName("공지사항 삭제")
    void delete() throws Exception {
        List<Integer> noticeNoList = new ArrayList<>();
        noticeNoList.add(100);

        doNothing().when(noticeRepository).deleteAllById(noticeNoList);

        noticeService.deleteNotices(noticeNoList);

        verify(noticeRepository, times(1)).deleteAllById(noticeNoList);

        verifyNoMoreInteractions(noticeRepository);
    }
}