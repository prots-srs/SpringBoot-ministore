package com.protsdev.ministore.pagePanelService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.ScrollPosition.Direction;
import org.springframework.stereotype.Service;

import com.protsdev.ministore.dto.PageServiceView;
import com.protsdev.ministore.pageCommon.ListPagination;
import com.protsdev.ministore.pageCommon.PanelService;
import com.protsdev.ministore.pagePanelSeo.PanelSeoFormFields;
import com.protsdev.ministore.pagePanelSeo.PanelSeoListHeaders;

@Service
public class PanelServiceService implements PanelService<PanelServiceFormFields> {
    private ServiceRepository repo;

    public PanelServiceService(ServiceRepository repo) {
        this.repo = repo;
    }

    @Override
    public ListPagination fetch(int page, String search, String editLink, String deleteLink) {

        if (search == null)
            search = "";

        Pageable pageable = PageRequest.of(page - 1,
                ListPagination.getPageSize(),
                Sort.by("sort").ascending());

        Page<ServiceEntity> resultQuery;
        List<PanelServiceListHeaders> list;

        if (search.length() >= 3) {
            resultQuery = repo.findByTitleContainingOrDescriptionContainingAllIgnoreCase(search, search, pageable);
        } else {
            resultQuery = repo.findAll(pageable);
        }

        list = resultQuery.getContent().stream().map(i -> {
            Map<String, String> actions = new HashMap<>();
            actions.put("edit", String.format(editLink, i.getId()));
            actions.put("delete", String.format(deleteLink, i.getId()));

            return new PanelServiceListHeaders(
                    i.getSort(),
                    i.getActive(),
                    i.getTitle(),
                    i.getDescription(),
                    i.getIconClass(),
                    actions);
        }).toList();

        return new ListPagination(
                list,
                page,
                resultQuery.getTotalElements(),
                resultQuery.getTotalPages(),
                ListPagination.getPageSize());
    }

    @Override
    public void create(PanelServiceFormFields item) {
        try {
            repo.save(item.getAsTagretEntity());
        } catch (Exception e) {
        }
    }

    @Override
    public void update(PanelServiceFormFields item, Long id) {
        ServiceEntity entity = item.getAsTagretEntity();
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
    public Optional<PanelServiceFormFields> getById(Long id) {
        Optional<PanelServiceFormFields> result = Optional.empty();
        if (id == null)
            return result;

        var entityOp = repo.findById(id);
        if (entityOp.isPresent()) {
            var entity = entityOp.get();
            result = Optional.of(new PanelServiceFormFields(
                    entity.getSort(),
                    entity.getActive(),
                    entity.getTitle(),
                    entity.getDescription(),
                    entity.getIconClass()));
        }
        return result;
    }

    public List<PageServiceView> getForPublicPage() {
        var services = repo.findByActiveTrueOrderBySortAsc();
        if (services.size() == 0) {
            return null;
        }
        return services.stream().map(i -> new PageServiceView(
                i.getTitle(),
                i.getDescription(),
                i.getIconClass().getIco())).toList();
    }
}
