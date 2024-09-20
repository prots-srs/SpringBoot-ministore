package com.protsdev.ministore.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.protsdev.ministore.dto.ContentFile;
import com.protsdev.ministore.enums.StorageModules;

import java.util.Optional;

public interface StorageService {
    // void init();
    // void deleteAll();
    // Path load(String filename, StorageModules module);

    void provideStorePlace(StorageModules module);

    FileUploadEntity store(MultipartFile file, StorageModules module);

    Optional<ContentFile> loadAsResource(String filename);

    void deleteById(Long id);

    Optional<FileUploadEntity> getFileEntityById(Long id);
}
