package com.protsdev.ministore.pageCommon;

import java.util.Optional;

public interface PanelService<T> {
    ListPagination fetch(int page, String search, String editLink, String deleteLink);

    void create(T item);

    void update(T item, Long id);

    Boolean delete(Long id);

    Optional<T> getById(Long id);
}
