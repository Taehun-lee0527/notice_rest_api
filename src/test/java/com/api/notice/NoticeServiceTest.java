package com.api.notice;

import com.api.notice.entity.NoticeEntity;
import com.api.notice.repository.NoticeRepository;
import com.api.notice.response.NoticeDetailResponse;
import com.api.notice.service.impl.NoticeServiceImpl;
import com.api.user.entity.UserEntity;
import com.api.user.repository.UserRepository;
import com.api.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
//@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {


    private static int NOTICE_NO;
    private final String TITLE = "panda";
    private final String CONTENT = "bear";
    private final String LOGINID = "test";
    private final String PASSWORD = "123";

    @Mock
    private UserServiceImpl userService;
    @InjectMocks
    NoticeServiceImpl noticeService;
    @Mock
    UserRepository userRepository;
    @Mock
    NoticeRepository noticeRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private NoticeEntity noticeEntity;

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.openMocks(this);

        UserEntity user = UserEntity.builder()
                .loginId(LOGINID)
                .password(passwordEncoder.encode(PASSWORD))
                .build();
//        userService.join(user);

        noticeEntity = new NoticeEntity();
        noticeEntity.setNoticeNo(99);
        noticeEntity.setTitle(TITLE);
        noticeEntity.setContent(CONTENT);
        noticeEntity.setCreator(LOGINID);
        noticeEntity.setNoticeStartDate(LocalDateTime.now());
        noticeEntity.setNoticeEndDate(LocalDateTime.of(2022, 12, 31, 3, 2, 4));
//        notice = noticeRepository.save(notice);
//        when(noticeRepository.save(notice)).thenReturn(notice);

//        NOTICE_NO=notice.getNoticeNo();

//        when(noticeRepository.save(notice)).thenReturn(notice);
        NOTICE_NO = noticeEntity.getNoticeNo();

//        List<NoticeEntity> noticeList = new ArrayList<>();
//        noticeList.add(notice);
//
//        Pagination pagination = new Pagination();
//        pagination.setPage(1);
//        pagination.setTotalCount(1);
//
//        ToastResponseTemplate<NoticeEntity> toastResponseTemplate= new ToastResponseTemplate<>();
//        toastResponseTemplate.setContents(noticeList);
//        toastResponseTemplate.setPagination(pagination);
//        NoticeSearchResponse response = new NoticeSearchResponse();
//        response.setResult(true);
//        response.setData(toastResponseTemplate);
//        NoticeSearchRequest noticeSearchRequest = new NoticeSearchRequest();
//        noticeSearchRequest.setPage(1);
//        noticeSearchRequest.setPerPage(20);
//        when(noticeService.getNoticeList(noticeSearchRequest)).thenReturn(response);
    }

    @AfterEach
    void tearDown() {
        noticeRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 다건 조회")
    void getNotices() throws Exception {
        when(noticeRepository.findById(1)).thenReturn(Optional.of(noticeEntity));

        NoticeDetailResponse response = noticeService.getNotice(99);
//        NoticeDetailResponse noticeDetailResponse = noticeService.getNotice(NOTICE_NO);
//
//        NoticeSearchRequest noticeSearchRequest = new NoticeSearchRequest();
//        noticeSearchRequest.setPage(1);
//        noticeSearchRequest.setPerPage(20);
//        NoticeSearchResponse notices = noticeService.getNoticeList(noticeSearchRequest);
//        assertThat(notices.getData().getPagination().getTotalCount()).isEqualTo(1);
    }
}