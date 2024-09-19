package com.protsdev.ministore.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.Optional;
import java.time.LocalDateTime;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.protsdev.ministore.localize.LocalizeService;

@Service
public class FileSystemStorageService implements StorageService {

    private LocalizeService localize;

    private final Path location;
    private final FileUploadRepository fileUploadRepository;

    public FileSystemStorageService(
            FileUploadRepository fileUploadRepository,
            StorageProperties properties,
            LocalizeService lS) {

        localize = lS;

        if (properties.getLocation().trim().length() == 0) {
            throw new StorageException(localize.getMessage("storage.file.error.dir.location"));
        }

        this.fileUploadRepository = fileUploadRepository;
        location = Paths.get(properties.getLocation());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(location);
        } catch (IOException e) {
            throw new StorageException(localize.getMessage("storage.file.error.dir.create"), e);
        }
    }

    @Override
    public FileUploadEntity store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException(localize.getMessage("storage.file.error.file.empty"));
            }

            String uniqueFileName = UUID.randomUUID().toString() + "-" + LocalDateTime.now().getNano();

            var ext = getExtensionByStringHandling(file.getOriginalFilename());
            if (ext.isPresent()) {
                uniqueFileName = uniqueFileName + "." + ext.get();
            }

            Path destinationFile = location.resolve(Paths.get(uniqueFileName))
                    .normalize().toAbsolutePath();

            // This is a security check
            if (!destinationFile.getParent().equals(location.toAbsolutePath())) {
                throw new StorageException(localize.getMessage("storage.file.error.file.outside"));
            }

            // save
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);

                var fileEntity = new FileUploadEntity();
                fileEntity.setOriginalName(file.getOriginalFilename());
                fileEntity.setFileType(file.getContentType());
                fileEntity.setFileSize(file.getSize());
                fileEntity.setSavedName(uniqueFileName);

                var savedFile = fileUploadRepository.save(fileEntity);
                return savedFile;
            }
        } catch (IOException e) {
            throw new StorageException(localize.getMessage("storage.file.error.store.file"), e);
        }

    }

    private Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    @Override
    public Path load(String filename) {
        return location.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(localize.getMessage("storage.file.error.read.file") + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException(localize.getMessage("storage.file.error.read.file") + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(location.toFile());
    }

    @Override
    public void deleteById(Long id) {
        if (id == null || id.equals(Long.valueOf(0))) {
            return;
        }

        Optional<FileUploadEntity> entityOp = fileUploadRepository.findById(id);
        if (entityOp.isPresent()) {

            Path file = load(entityOp.get().getSavedName());
            try {
                Files.delete(file);
                fileUploadRepository.deleteById(id);
            } catch (Exception e) {
                throw new StorageException(localize.getMessage("storage.file.error.delete.file"), e);
            }
        }
    }

    @Override
    public Optional<FileUploadEntity> getFileEntityById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return fileUploadRepository.findById(id);
    }

}
