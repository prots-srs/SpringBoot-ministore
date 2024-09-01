package com.protsdev.ministore.dto;

import java.util.List;

public record PaginationList(
        List<?> list,
        Integer currentPage,
        Long totalElements,
        Integer pageSize,
        Integer totalPages) {

    public static int getPageSize() {
        return 2;
    }
}
