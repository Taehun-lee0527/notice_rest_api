package com.api.notice.response;

import com.api.notice.entity.NoticeEntity;
import com.common.toast.data.ToastResponseTemplate;
import com.common.toast.pagination.Pagination;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NoticeSearchResponse implements Serializable {
    boolean result;
    ToastResponseTemplate<NoticeEntity> data;

    public static NoticeSearchResponse of(boolean result, List<NoticeEntity> list, int page, int totalCount){
        NoticeSearchResponse response = new NoticeSearchResponse();
        response.setResult(result);

        ToastResponseTemplate<NoticeEntity> template = new ToastResponseTemplate<>();
        template.setContents(list);

        Pagination pagination = new Pagination();
        pagination.setPage(page);
        pagination.setTotalCount(totalCount);

        template.setPagination(pagination);

        response.setData(template);

        return response;
    }
}
