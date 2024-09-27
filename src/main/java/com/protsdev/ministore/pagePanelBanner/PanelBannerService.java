package com.protsdev.ministore.pagePanelBanner;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.protsdev.ministore.dto.FileView;
import com.protsdev.ministore.dto.PageBannerView;
import com.protsdev.ministore.dto.PageProductView;
import com.protsdev.ministore.enums.ProductTypes;
import com.protsdev.ministore.enums.StorageModules;
import com.protsdev.ministore.pageCommon.ListPagination;
import com.protsdev.ministore.pageCommon.PanelService;
import com.protsdev.ministore.pagePanelProduct.ProductEntity;
import com.protsdev.ministore.storage.StorageException;
import com.protsdev.ministore.storage.StorageService;

@Service
public class PanelBannerService implements PanelService<PanelBannerFormFields> {

    private BannerRepository repo;
    private StorageService storageService;

    private Map<Long, FileView> savedFiles = new HashMap<>();

    public PanelBannerService(BannerRepository repo, StorageService storageService) {
        this.repo = repo;
        this.storageService = storageService;
    }

    @Override
    public ListPagination fetch(int page, String search, String editLink, String deleteLink) {
        if (search == null) {
            search = "";
        }

        Pageable pageable = PageRequest.of(page - 1,
                ListPagination.getPageSize(),
                Sort.by("sort").ascending());

        Page<BannerEntity> resultQuery;
        List<PanelBannerListHeaders> list;

        // if (search.length() >= 3) {
        // resultQuery = repo.findByNameContainsIgnoreCase(search, pageable);
        // } else {
        resultQuery = repo.findAll(pageable);
        // }
        list = resultQuery.getContent().stream()
                .map(i -> {
                    Map<String, String> actions = new HashMap<>();
                    actions.put("edit", String.format(editLink, i.getId()));
                    actions.put("delete", String.format(deleteLink, i.getId()));

                    return new PanelBannerListHeaders(
                            i.getSort(),
                            i.getActive(),
                            i.getTitle(),
                            i.getLink(),
                            i.getPicture() != null ? i.getPicture().getSavedName() : "",
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

    public Map<Long, FileView> getFiles() {
        return savedFiles;
    }

    @Override
    public void create(PanelBannerFormFields item) {

        try {
            var entity = item.getAsTagretEntity();

            if (!item.file().isEmpty()) {
                var fileSaved = storageService.store(item.file(), StorageModules.BANNER);
                if (fileSaved != null) {
                    entity.setPicture(fileSaved);
                }
            }

            repo.save(entity);
        } catch (Exception e) {
        }
    }

    @Override
    public Boolean delete(Long id) {
        if (id == null) {
            return false;
        }

        if (repo.existsById(id)) {
            try {
                repo.deleteById(id);
            } catch (Exception e) {
                throw new StorageException("can't delete entity: ", e);
            }
        }

        return true;
    }

    @Override
    public Optional<PanelBannerFormFields> getById(Long id) {

        Optional<PanelBannerFormFields> result = Optional.empty();

        if (id == null) {
            return result;
        }

        var entityOp = repo.findById(id);
        if (entityOp.isPresent()) {
            BannerEntity entity = entityOp.get();

            if (entity.getPicture() != null) {
                savedFiles.put(entity.getPicture().getId(), new FileView(
                        entity.getPicture().getSavedName(),
                        entity.getTitle(),
                        entity.getPicture().getFileType()));
            }

            result = Optional.of(new PanelBannerFormFields(
                    entity.getSort(),
                    entity.getActive(),
                    entity.getTitle(),
                    entity.getLink(),
                    entity.getPicture() != null ? entity.getPicture().getId() : null,
                    null));
        }
        return result;
    }

    @Override
    public void update(PanelBannerFormFields item, Long id) {
        BannerEntity entity = item.getAsTagretEntity();
        entity.setId(id);
        Boolean deleteOldFile = false;

        try {
            // save new picture from form
            if (!item.file().isEmpty()) {
                var fileSaved = storageService.store(item.file(), StorageModules.BANNER);
                if (fileSaved != null) {
                    entity.setPicture(fileSaved);
                    deleteOldFile = true;
                }
            }

        } catch (Exception e) {
        }

        if (!deleteOldFile && item.fileSaved() != null) {
            var file = storageService.getFileEntityById(item.fileSaved());
            if (file.isPresent()) {
                entity.setPicture(file.get());
            }
        }

        repo.save(entity);
        if (deleteOldFile) {
            storageService.deleteById(item.fileSaved());
        }
    }

    public List<PageBannerView> getForPublicPage() {

        var list = repo.findByActiveTrueOrderBySortAsc();
        List<PageBannerView> viewList = list.stream().map(i -> new PageBannerView(
                i.getTitle(),
                i.getLink(),
                i.getPicture() != null ? i.getPicture().getSavedName() : "")).toList();

        return viewList;
    }
}
