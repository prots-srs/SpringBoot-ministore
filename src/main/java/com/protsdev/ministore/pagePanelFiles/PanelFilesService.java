package com.protsdev.ministore.pagePanelFiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.protsdev.ministore.localize.LocalizeService;
import com.protsdev.ministore.pageCommon.ListPagination;
import com.protsdev.ministore.pageCommon.PanelService;
import com.protsdev.ministore.storage.FileUploadEntity;
import com.protsdev.ministore.storage.FileUploadRepository;
import com.protsdev.ministore.storage.StorageException;
import com.protsdev.ministore.storage.StorageService;

@Service
public class PanelFilesService implements PanelService<String> {

    private LocalizeService localize;

    private StorageService storageService;
    private final FileUploadRepository repo;

    public PanelFilesService(StorageService storageService, FileUploadRepository fileUploadRepository,
            LocalizeService lS) {
        localize = lS;
        this.storageService = storageService;
        repo = fileUploadRepository;
    }

    @Override
    public void create(String item) {
        // TODO Auto-generated method stub

    }

    @Override
    public Boolean delete(Long id) {
        if (id == null)
            return false;

        if (repo.existsById(id)) {
            try {
                storageService.deleteById(id);
            } catch (Exception e) {
                throw new StorageException(localize.getMessage("storage.file.error.delete.file"), e);
            }
        }

        return true;
    }

    @Override
    public ListPagination fetch(int page, String search, String editLink, String deleteLink) {

        Sort sort = Sort.by("module").ascending()
                .and(Sort.by("originalName").ascending());

        Pageable pageable = PageRequest.of(page - 1,
                ListPagination.getPageSize(),
                sort);

        Page<FileUploadEntity> resultQuery;
        List<PanelFilesListHeaders> list;

        resultQuery = repo.findAll(pageable);

        list = resultQuery.getContent().stream()
                .map(i -> {
                    Map<String, String> actions = new HashMap<>();
                    actions.put("delete", String.format(deleteLink, i.getId()));

                    return new PanelFilesListHeaders(
                            i.getOriginalName(),
                            i.getFileType(),
                            i.getFileSize().toString(),
                            i.getSavedName(),
                            i.getModule() != null ? i.getModule().getDir() : "",
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
    public Optional<String> getById(Long id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public void update(String item, Long id) {
        // TODO Auto-generated method stub

    }

}
