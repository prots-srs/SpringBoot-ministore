package com.protsdev.ministore.pageCommon;

import java.util.List;

public record ListPagination(
        List<?> list,
        Integer currentPage,
        Long totalElements,
        Integer totalPages,
        Integer pageSize) {

    public static int getPageSize() {
        return 20;
    }
}
