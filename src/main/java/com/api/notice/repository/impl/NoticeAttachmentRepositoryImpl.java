package com.api.notice.repository.impl;

import com.api.notice.entity.NoticeAttachmentEntity;
import com.api.notice.entity.QNoticeAttachmentEntity;
import com.api.notice.repository.CustomNoticeAttachmentRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoticeAttachmentRepositoryImpl extends QuerydslRepositorySupport implements CustomNoticeAttachmentRepository {
    private final JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager entityManager;
    public NoticeAttachmentRepositoryImpl(EntityManager entityManager){
        super(NoticeAttachmentEntity.class);
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<NoticeAttachmentEntity> findAllByNoticeNoList(List<Integer> noticeNoList) {
        QNoticeAttachmentEntity noticeAttachmentEntity = QNoticeAttachmentEntity.noticeAttachmentEntity;

        return queryFactory.selectFrom(noticeAttachmentEntity)
                .where(noticeAttachmentEntity.noticeNo.in(noticeNoList))
                .fetch();
    }
}
