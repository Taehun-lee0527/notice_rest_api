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
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class NoticeServiceIntegrationTest {

    @Autowired
    private NoticeServiceImpl noticeService;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private NoticeAttachmentRepository noticeAttachmentRepository;

    @Autowired
    private EntityManager entityManager;

    private static int NOTICE_NO;
    private static String LOGIN_ID = "test";

    @BeforeEach
    void setUp() throws Exception {
        // 초기 데이터 삽입
        NoticeSaveRequest noticeEntity = new NoticeSaveRequest();
        noticeEntity.setTitle("test_title1");
        noticeEntity.setContent("test_content1");
        noticeEntity.setCreator(LOGIN_ID);
        noticeEntity.setNoticeStartDate(LocalDateTime.now());
        noticeEntity.setNoticeEndDate(LocalDateTime.of(2022, 12, 31, 3, 2, 4));
        NOTICE_NO = noticeRepository.createNotice(noticeEntity);

        NoticeAttachmentEntity noticeAttachmentEntity = new NoticeAttachmentEntity();
        noticeAttachmentEntity.setNoticeAttachmentNo(100);
        noticeAttachmentEntity.setNoticeNo(NOTICE_NO);
        noticeAttachmentEntity.setFileName("test.jpg");
        noticeAttachmentEntity.setFilePath("/user/home/test.jpg");
        noticeAttachmentEntity.setFileSize(100L);
        noticeAttachmentEntity.setCreator(LOGIN_ID);
        noticeAttachmentEntity.setCreatedAt(LocalDateTime.now());
        noticeAttachmentRepository.save(noticeAttachmentEntity);
    }

    @AfterEach
    void tearDown() {
        // 데이터 정리
        noticeRepository.deleteAll();
        noticeAttachmentRepository.deleteAll();
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
        assertEquals(1, response.getData().getContents().size(), "There should be 1 notice");
    }

    @Test
    @DisplayName("공지사항 단건 조회")
    void getNotice() throws Exception {
        NoticeDetailResponse noticeDetailResponse = noticeService.getNotice(1);

        assertNotNull(noticeDetailResponse, "Response should not be null");
    }

    @Test
    @DisplayName("공지사항 단건 조회 - 공지사항 없음")
    void getNotice_notFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            noticeService.getNotice(999);
        });
    }

    @Test
    @DisplayName("공지사항 추가")
    void create() throws Exception {
        NoticeSaveRequest noticeSaveRequest = new NoticeSaveRequest();
        noticeSaveRequest.setTitle("test_title2");
        noticeSaveRequest.setContent("test_content2");
        noticeSaveRequest.setNoticeStartDate(LocalDateTime.now());
        noticeSaveRequest.setNoticeEndDate(LocalDateTime.of(2031, 12, 22, 3, 4, 5));
        noticeSaveRequest.setCreator(LOGIN_ID);

        int noticeNo = noticeService.createNotice(noticeSaveRequest);
        NoticeDetailResponse noticeDetailResponse = noticeService.getNotice(noticeNo);

        assertThat(noticeNo).isEqualTo(2);
        assertThat(noticeDetailResponse.getNotice().getTitle()).isEqualTo("test_title2");
        assertThat(noticeDetailResponse.getNotice().getContent()).isEqualTo("test_content2");
    }

    @Test
    @DisplayName("공지사항 수정")
    void edit() throws Exception {
        NoticeDetailResponse noticeDetailResponse = noticeService.getNotice(1);

        NoticeSaveRequest noticeSaveRequest = new NoticeSaveRequest();
        noticeSaveRequest.setNoticeNo(1);
        noticeSaveRequest.setTitle("updated_title2");
        noticeSaveRequest.setContent("updated_content2");
        noticeSaveRequest.setNoticeStartDate(LocalDateTime.now());
        noticeSaveRequest.setNoticeEndDate(LocalDateTime.of(2031, 12, 22, 3, 4, 5));
        noticeSaveRequest.setUpdater(LOGIN_ID);

        noticeService.updateNotice(noticeSaveRequest);

        // 업데이트 적용을 위해 명시
        entityManager.flush();
        entityManager.clear();

        NoticeEntity updatedNotice = noticeRepository.findById(1).orElse(null);
        assertNotNull(updatedNotice);
        assertEquals("updated_title2", updatedNotice.getTitle());
        assertEquals("updated_content2", updatedNotice.getContent());
    }

    @Test
    @DisplayName("공지사항 삭제")
    void delete() throws Exception {
        NoticeSearchRequest noticeSearchRequest = new NoticeSearchRequest();
        noticeSearchRequest.setPage(1);
        noticeSearchRequest.setPerPage(20);
        NoticeSearchResponse noticeSearchResponse = noticeService.getNoticeList(noticeSearchRequest);

        noticeService.deleteNotices(List.of(1));

        NoticeSearchResponse result = noticeService.getNoticeList(noticeSearchRequest);

        assertEquals(result.getData().getPagination().getTotalCount(), noticeSearchResponse.getData().getPagination().getTotalCount()-1);
    }
}
