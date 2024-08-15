package com.api.notice.service.impl;

import com.api.notice.entity.NoticeAttachmentEntity;
import com.api.notice.entity.NoticeEntity;
import com.api.notice.repository.NoticeAttachmentRepository;
import com.api.notice.repository.NoticeRepository;
import com.api.notice.request.NoticeSaveRequest;
import com.api.notice.request.NoticeSearchRequest;
import com.api.notice.response.NoticeDetailResponse;
import com.api.notice.response.NoticeSearchResponse;
import com.api.notice.service.NoticeService;
import com.common.service.FileService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
    private final FileService fileService;
    private final NoticeRepository noticeRepository;
    private final NoticeAttachmentRepository noticeAttachmentRepository;

    private static final Path UPLOAD_FILE_PATH = Paths.get(System.getProperty("user.home"), "uploadFile").toAbsolutePath().normalize();

    @Override
    @Cacheable("notice-list")
    public NoticeSearchResponse getNoticeList(NoticeSearchRequest noticeSearchRequest) throws Exception {
        return noticeRepository.searchNotice(noticeSearchRequest);
    }

    @Override
    public NoticeDetailResponse getNotice(int noticeNo) throws Exception {
        NoticeEntity noticeEntity = noticeRepository.findById(noticeNo)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 공지사항입니다."));

        List<NoticeAttachmentEntity> attachmentList = noticeAttachmentRepository.findAllByNoticeNo(noticeEntity.getNoticeNo());
        return NoticeDetailResponse.of(noticeEntity, attachmentList);
    }

    @Override
    @Transactional
    public int createNotice(NoticeSaveRequest noticeSaveRequest) throws Exception {
        int noticeNo = noticeRepository.createNotice(noticeSaveRequest);
        if(!ObjectUtils.isEmpty(noticeSaveRequest.getAttachmentList())){
            uploadFile(noticeSaveRequest, noticeNo);
        }
        return noticeNo;
    }

    @Override
    @Transactional
    public void updateNotice(NoticeSaveRequest noticeSaveRequest) throws Exception {
        noticeRepository.updateNotice(noticeSaveRequest);
        if(!ObjectUtils.isEmpty(noticeSaveRequest.getAttachmentList())){
            uploadFile(noticeSaveRequest, noticeSaveRequest.getNoticeNo());
        }
        if(!ObjectUtils.isEmpty(noticeSaveRequest.getDeleteAttachmentList())){
            noticeSaveRequest.getDeleteAttachmentList()
                    .forEach(noticeAttachmentEntity -> {
                        try {
                            fileService.deleteFile(noticeAttachmentEntity.getFilePath());
                            noticeAttachmentRepository.deleteById(noticeAttachmentEntity.getNoticeAttachmentNo());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    @Override
    @Transactional
    public void deleteNotices(List<Integer> noticeNoList) throws Exception {
        noticeRepository.deleteAllById(noticeNoList);
    }

    @Override
    @Transactional
    public void increaseViewCount(int noticeNo) throws Exception {
        try{
            noticeRepository.increaseViewCount(noticeNo);
        } catch(Exception e){
            throw new Exception("데이터베이스 작업 중 오류가 발생했습니다. 다시 시도해 주세요.");
        }
    }

    private void uploadFile(NoticeSaveRequest noticeSaveRequest, int noticeNo) throws Exception{
        noticeSaveRequest.getAttachmentList().stream()
                .filter(file -> !file.isEmpty())
                .map(file -> {
                    try{
                        // 서버 업로드용 랜덤 파일명 생성
                        String randomName = UUID.randomUUID().toString(); // Generates a unique random string
                        // 파일 확장자
                        String extension = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));

                        Path destinationFile = UPLOAD_FILE_PATH.resolve(Paths.get(randomName+extension)).normalize().toAbsolutePath();
                        fileService.uploadFile(file, UPLOAD_FILE_PATH, destinationFile);

                        NoticeAttachmentEntity noticeAttachmentEntity = new NoticeAttachmentEntity();
                        noticeAttachmentEntity.setNoticeNo(noticeNo);
                        noticeAttachmentEntity.setFileName(file.getOriginalFilename());
                        noticeAttachmentEntity.setFilePath(destinationFile.toString());
                        noticeAttachmentEntity.setFileSize(file.getSize());
                        noticeAttachmentEntity.setCreator(noticeSaveRequest.getCreator());
                        noticeAttachmentEntity.setCreatedAt(LocalDateTime.now());

                        return noticeAttachmentEntity;
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .forEach(noticeAttachmentRepository::save);
        ;
    }
}
