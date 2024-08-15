package com.api.notice.response;

import com.api.notice.entity.NoticeAttachmentEntity;
import com.api.notice.entity.NoticeEntity;
import com.common.toast.data.ToastResponseTemplate;
import com.common.toast.pagination.Pagination;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NoticeDetailResponse {
    NoticeEntity notice;
    List<NoticeAttachmentEntity> attachmentList;

    public static NoticeDetailResponse of(NoticeEntity notice, List<NoticeAttachmentEntity> attachmentList){
        NoticeDetailResponse noticeDetailResponse = new NoticeDetailResponse();
        noticeDetailResponse.setNotice(notice);
        if(!attachmentList.isEmpty()){
            noticeDetailResponse.setAttachmentList(attachmentList);
        }

        return noticeDetailResponse;
    }
}
