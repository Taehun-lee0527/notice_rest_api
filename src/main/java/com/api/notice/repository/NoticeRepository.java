package com.api.notice.repository;

import com.api.notice.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Integer>, CustomNoticeRepository {

}
