package com.protsdev.ministore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.protsdev.ministore.models.FileUpload;

public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {

}
