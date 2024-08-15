package com.common.toast.pagination;

import lombok.Data;

import java.io.Serializable;

@Data
public class Pagination implements Serializable {
    private int page;
    private int totalCount;
}
