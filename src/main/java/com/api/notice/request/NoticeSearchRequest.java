package com.api.notice.request;

import lombok.Data;

@Data
public class NoticeSearchRequest {
    private String searchType;
    private String searchText;
    private int page;
    private int perPage;

}
