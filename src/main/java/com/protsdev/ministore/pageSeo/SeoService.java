package com.protsdev.ministore.pageSeo;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.protsdev.ministore.dto.PaginationList;

@Service
public class SeoService {

    private SeoRepository repo;

    public SeoService(SeoRepository repo) {
        this.repo = repo;
    }

    public PaginationList fetch(int page, String search, String editLink, String deleteLink) {

        if (search == null)
            search = "";

        List<ListSeo> list;
        Long totalElements = 0l;
        Integer totalPages = 0;

        if (search.length() > 3) {
            list = repo.findByPathContaining(search).stream()
                    .map(i -> new ListSeo(
                            i.getPath(),
                            i.getTitle(),
                            i.getKeywords(),
                            i.getDescription(),
                            String.format(editLink, i.getId()),
                            String.format(deleteLink, i.getId())))
                    .toList();

            totalElements = Long.valueOf(list.size());
            totalPages = list.size();
        } else {
            Pageable pageable = PageRequest.of(page - 1, PaginationList.getPageSize());
            Page<SeoEntity> result = repo.findAll(pageable);

            list = result.getContent().stream()
                    .map(i -> new ListSeo(i.getPath(), i.getTitle(), i.getKeywords(), i.getDescription(),
                            String.format(editLink, i.getId()),
                            String.format(deleteLink, i.getId())))
                    .toList();

            totalElements = result.getTotalElements();
            totalPages = result.getTotalPages();
        }

        return new PaginationList(
                list,
                page,
                totalElements,
                PaginationList.getPageSize(),
                totalPages);

    }

    private SeoEntity convertFromForm(FormSeo item) {
        SeoEntity entity = new SeoEntity();
        entity.setPath(item.path() != null ? item.path() : "");
        entity.setTitle(item.title() != null ? item.title() : "");
        entity.setKeywords(item.keywords() != null ? item.keywords() : "");
        entity.setDescription(item.description() != null ? item.description() : "");
        return entity;
    }

    public void create(FormSeo item) {
        try {
            repo.save(convertFromForm(item));
        } catch (Exception e) {
        }
    }

    public void update(Long id, FormSeo item) {
        SeoEntity entity = convertFromForm(item);
        entity.setId(id);
        try {
            repo.save(entity);
        } catch (Exception e) {
        }
    }

    public Boolean delete(Long id) {
        if (id == null)
            return false;

        if (repo.existsById(id)) {
            try {
                repo.deleteById(id);
            } catch (Exception e) {
                return false;
            }
        }

        return true;
    }

    public Optional<SeoEntity> getById(Long id) {
        if (id == null)
            return Optional.empty();

        var list = repo.findAllById(Arrays.asList(id));
        if (!list.isEmpty()) {
            return Optional.of(list.get(0));
        }
        return Optional.empty();
    }
}
