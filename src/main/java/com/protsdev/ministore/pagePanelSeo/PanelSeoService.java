package com.protsdev.ministore.pagePanelSeo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.protsdev.ministore.pageCommon.ListPagination;
import com.protsdev.ministore.pageCommon.PageSeo;
import com.protsdev.ministore.pageCommon.PanelService;

@Service
public class PanelSeoService implements PanelService<PanelSeoFormFields> {

    private SeoRepository repo;

    public PanelSeoService(SeoRepository repo) {
        this.repo = repo;
    }

    @Override
    public ListPagination fetch(int page, String search, String editLink, String deleteLink) {

        if (search == null)
            search = "";

        Pageable pageable = PageRequest.of(page - 1, ListPagination.getPageSize());
        Page<SeoEntity> resultQuery;
        List<PanelSeoListHeaders> list;

        if (search.length() >= 3) {
            resultQuery = repo.findByPathContaining(search, pageable);
        } else {
            resultQuery = repo.findAll(pageable);
        }

        list = resultQuery.getContent().stream()
                .map(i -> {
                    Map<String, String> actions = new HashMap<>();
                    actions.put("edit", String.format(editLink, i.getId()));
                    actions.put("delete", String.format(deleteLink, i.getId()));

                    return new PanelSeoListHeaders(
                            i.getPath(),
                            i.getTitle(),
                            i.getKeywords(),
                            i.getDescription(),
                            actions);
                })
                .toList();

        return new ListPagination(
                list,
                page,
                resultQuery.getTotalElements(),
                resultQuery.getTotalPages(),
                ListPagination.getPageSize());

    }

    @Override
    public void create(PanelSeoFormFields item) {
        try {
            repo.save(item.getAsTagretEntity());
        } catch (Exception e) {
        }
    }

    @Override
    public void update(PanelSeoFormFields item, Long id) {
        SeoEntity entity = item.getAsTagretEntity();
        entity.setId(id);
        try {
            repo.save(entity);
        } catch (Exception e) {
        }
    }

    @Override
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

    @Override
    public Optional<PanelSeoFormFields> getById(Long id) {

        Optional<PanelSeoFormFields> result = Optional.empty();
        if (id == null)
            return result;

        var entityOp = repo.findById(id);
        if (entityOp.isPresent()) {
            var entity = entityOp.get();
            result = Optional.of(new PanelSeoFormFields(
                    entity.getPath(),
                    entity.getTitle(),
                    entity.getKeywords(),
                    entity.getDescription()));
        }
        return result;
    }

    public PageSeo getForPublicPage(String page) {
        if (page == null) {
            return new PageSeo("title is null", "", "");
        }

        var entity = repo.findFirstByPath(page);
        if (entity != null) {
            return getAsPageSeo(entity);
        } else {
            return new PageSeo("title not found", "", "");
        }
    }

    private PageSeo getAsPageSeo(SeoEntity entity) {
        return new PageSeo(entity.getTitle().replace("\r", "").replace("\n", ""),
                entity.getKeywords().replace("\r", "").replace("\n", ""),
                entity.getDescription().replace("\r", "").replace("\n", ""));
    }
}
