package com.protsdev.ministore.pagePanelProduct;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.protsdev.ministore.dto.FileView;
import com.protsdev.ministore.dto.PageProductView;
import com.protsdev.ministore.enums.ProductTypes;
import com.protsdev.ministore.pageCommon.ListPagination;
import com.protsdev.ministore.pageCommon.PanelService;
import com.protsdev.ministore.pagePanelSeo.PanelSeoFormFields;
import com.protsdev.ministore.pagePanelService.PanelServiceListHeaders;
import com.protsdev.ministore.storage.FileUploadEntity;
import com.protsdev.ministore.storage.StorageService;

@Service
public class PanelProductService implements PanelService<PanelProductFormFields> {
    private ProductRepository repo;
    private StorageService storageService;

    private Map<Long, FileView> savedFiles = new HashMap<>();

    public PanelProductService(ProductRepository repo, StorageService storageService) {
        this.repo = repo;
        this.storageService = storageService;
    }

    @Override
    public ListPagination fetch(int page, String search, String editLink, String deleteLink) {

        if (search == null)
            search = "";

        Pageable pageable = PageRequest.of(page - 1,
                ListPagination.getPageSize(),
                Sort.by("sort").ascending());

        Page<ProductEntity> resultQuery;
        List<PanelProductListHeaders> list;

        if (search.length() >= 3) {
            resultQuery = repo.findByNameContainsIgnoreCase(search, pageable);
        } else {
            resultQuery = repo.findAll(pageable);
        }
        list = resultQuery.getContent().stream()
                .map(i -> {
                    Map<String, String> actions = new HashMap<>();
                    actions.put("edit", String.format(editLink, i.getId()));
                    actions.put("delete", String.format(deleteLink, i.getId()));

                    return new PanelProductListHeaders(
                            i.getSort(),
                            i.getActive(),
                            i.getName(),
                            i.getPrice(),
                            i.getPicture() != null ? i.getPicture().getSavedName() : "",
                            i.getType(),
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
    public void create(PanelProductFormFields item) {
        try {
            var entity = item.getAsTagretEntity();
            if (item.file() != null) {
                var fileSaved = storageService.store(item.file());
                if (fileSaved != null) {
                    entity.setPicture(fileSaved);
                }
            }
            repo.save(entity);
        } catch (Exception e) {
        }
    }

    @Override
    public void update(PanelProductFormFields item, Long id) {
        ProductEntity entity = item.getAsTagretEntity();
        entity.setId(id);
        Boolean deleteOldFile = false;

        try {
            System.out.println("--> file: " + item.file());
            // save new picture from form
            if (item.file() != null) {
                var fileSaved = storageService.store(item.file());
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
    public Optional<PanelProductFormFields> getById(Long id) {
        Optional<PanelProductFormFields> result = Optional.empty();

        if (id == null)
            return result;

        var entityOp = repo.findById(id);
        if (entityOp.isPresent()) {
            ProductEntity entity = entityOp.get();

            if (entity.getPicture() != null) {
                savedFiles.put(entity.getPicture().getId(), new FileView(
                        entity.getPicture().getSavedName(),
                        entity.getName(),
                        entity.getPicture().getFileType()));
            }

            result = Optional.of(new PanelProductFormFields(
                    entity.getSort(),
                    entity.getActive(),
                    entity.getName(),
                    entity.getPrice(),
                    entity.getPicture() != null ? entity.getPicture().getId() : null,
                    null,
                    entity.getType()));
        }
        return result;
    }

    public Map<Long, FileView> getFiles() {
        return savedFiles;
    }

    public List<PageProductView> getForPublicPage(ProductTypes productTypes) {
        if (productTypes == null) {
            productTypes = ProductTypes.MOBILE;
        }

        NumberFormat formatter = new DecimalFormat("0");

        var list = repo.findByTypeAndActiveTrueOrderBySortAsc(productTypes);
        List<PageProductView> viewList = list.stream().map(i -> new PageProductView(
                i.getId(),
                i.getName(),
                formatter.format(i.getPrice()),
                i.getPicture().getSavedName(),
                i.getPicture().getOriginalName())).toList();

        return viewList;
    }
}
