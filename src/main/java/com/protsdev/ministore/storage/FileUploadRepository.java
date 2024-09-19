package com.protsdev.ministore.storage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileUploadRepository extends JpaRepository<FileUploadEntity, Long> {

}
