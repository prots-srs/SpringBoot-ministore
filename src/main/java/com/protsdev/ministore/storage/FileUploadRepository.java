package com.protsdev.ministore.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FileUploadRepository extends JpaRepository<FileUploadEntity, Long> {
    List<FileUploadEntity> findFirstBySavedName(String savedName);
}
