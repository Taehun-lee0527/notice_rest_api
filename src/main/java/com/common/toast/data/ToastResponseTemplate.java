package com.common.toast.data;

import com.common.toast.pagination.Pagination;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ToastResponseTemplate<E> implements Serializable {
    List<E> contents;
    Pagination pagination;
}
