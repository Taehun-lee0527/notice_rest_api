package com.api.notice.repository.impl;

import com.api.notice.entity.NoticeEntity;
import com.api.notice.entity.QNoticeEntity;
import com.api.notice.repository.CustomNoticeRepository;
import com.api.notice.request.NoticeSaveRequest;
import com.api.notice.request.NoticeSearchRequest;
import com.api.notice.response.NoticeSearchResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class NoticeRepositoryImpl extends QuerydslRepositorySupport implements CustomNoticeRepository {
    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;
    public NoticeRepositoryImpl(EntityManager entityManager){
        super(NoticeEntity.class);
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public NoticeSearchResponse searchNotice(NoticeSearchRequest noticeSearchRequest) throws Exception {
        QNoticeEntity qNoticeEntity = QNoticeEntity.noticeEntity;
        BooleanBuilder builder = new BooleanBuilder();

        if(!StringUtils.isBlank(noticeSearchRequest.getSearchType())){
            if("01".equals(noticeSearchRequest.getSearchType())){
                builder.and(qNoticeEntity.title.startsWithIgnoreCase(noticeSearchRequest.getSearchText()));
            }
            else if("02".equals(noticeSearchRequest.getSearchType())){
                builder.and(qNoticeEntity.content.startsWithIgnoreCase(noticeSearchRequest.getSearchText()));
            }
            else if("03".equals(noticeSearchRequest.getSearchType())){
                builder.and(qNoticeEntity.title.startsWithIgnoreCase(noticeSearchRequest.getSearchText()))
                        .or(qNoticeEntity.content.startsWithIgnoreCase(noticeSearchRequest.getSearchText()));
            }
            else if("04".equals(noticeSearchRequest.getSearchType())){
                builder.and(qNoticeEntity.creator.equalsIgnoreCase(noticeSearchRequest.getSearchText()));
            }
        }

        long offset = (long) (noticeSearchRequest.getPage() - 1) * noticeSearchRequest.getPerPage();

        List<NoticeEntity> noticeList = queryFactory
                .selectFrom(qNoticeEntity)
                .where(builder)
                .offset(offset)
                .limit(noticeSearchRequest.getPerPage())
                .orderBy(qNoticeEntity.createdAt.desc())
                .fetch();

        int totalCount = queryFactory
                .selectFrom(qNoticeEntity)
                .where(builder)
                .fetch()
                .size();


        return NoticeSearchResponse.of(true, noticeList, noticeSearchRequest.getPage(), totalCount);
    }

    @Override
    public int createNotice(NoticeSaveRequest noticeSaveRequest) {
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setCreator(noticeSaveRequest.getCreator());
        noticeEntity.setTitle(noticeSaveRequest.getTitle());
        noticeEntity.setContent(noticeSaveRequest.getContent());
        noticeEntity.setCreatedAt(LocalDateTime.now());
        noticeEntity.setViewCount(0);
        noticeEntity.setNoticeStartDate(noticeSaveRequest.getNoticeStartDate());
        noticeEntity.setNoticeEndDate(noticeSaveRequest.getNoticeEndDate());

        // Save and return the entity
        entityManager.persist(noticeEntity);
        return noticeEntity.getNoticeNo();
    }

    @Override
    public void updateNotice(NoticeSaveRequest noticeSaveRequest) {
        QNoticeEntity noticeEntity = QNoticeEntity.noticeEntity;
        queryFactory.update(noticeEntity)
                .set(noticeEntity.title, noticeSaveRequest.getTitle())
                .set(noticeEntity.content, noticeSaveRequest.getContent())
                .set(noticeEntity.noticeStartDate, noticeSaveRequest.getNoticeStartDate())
                .set(noticeEntity.noticeEndDate, noticeSaveRequest.getNoticeEndDate())
                .set(noticeEntity.updater, noticeSaveRequest.getUpdater())
                .set(noticeEntity.updatedAt, LocalDateTime.now())
                .where(noticeEntity.noticeNo.eq(noticeSaveRequest.getNoticeNo()))
                .execute();
    }

    @Override
    @Transactional
    public void increaseViewCount(int noticeNo) throws Exception {
        QNoticeEntity noticeEntity = QNoticeEntity.noticeEntity;
        queryFactory.update(noticeEntity)
                .set(noticeEntity.viewCount, noticeEntity.viewCount.add(1))
                .where(noticeEntity.noticeNo.eq(noticeNo))
                .execute();
    }
}
