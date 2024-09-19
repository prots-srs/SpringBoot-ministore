package com.protsdev.ministore.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
// import java.util.stream.Stream;
import java.util.Optional;

public interface StorageService {
    void init();

    void deleteAll();

    void deleteById(Long id);

    FileUploadEntity store(MultipartFile file);

    Path load(String filename);

    Resource loadAsResource(String filename);

    Optional<FileUploadEntity> getFileEntityById(Long id);
}
