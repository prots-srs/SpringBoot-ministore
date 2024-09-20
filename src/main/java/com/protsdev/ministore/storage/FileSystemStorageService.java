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
import org.springframework.web.multipart.MultipartFile;

import com.protsdev.ministore.dto.ContentFile;
import com.protsdev.ministore.enums.StorageModules;
import com.protsdev.ministore.localize.LocalizeService;

@Service
public class FileSystemStorageService implements StorageService {

    private LocalizeService localize;

    private final Path location;
    private final FileUploadRepository repo;

    public FileSystemStorageService(
            FileUploadRepository fileUploadRepository,
            StorageProperties properties,
            LocalizeService lS) {

        localize = lS;

        if (properties.getLocation().trim().length() == 0) {
            throw new StorageException(localize.getMessage("storage.file.error.dir.location"));
        }

        this.repo = fileUploadRepository;
        location = Paths.get(properties.getLocation());
    }

    // @Override
    // public void init() {
    // try {
    // Files.createDirectories(location);
    // } catch (IOException e) {
    // throw new
    // StorageException(localize.getMessage("storage.file.error.dir.create"), e);
    // }
    // }

    @Override
    public void provideStorePlace(StorageModules module) {
        try {
            Path moduleLocation = location.resolve(module.getDir());
            Files.createDirectories(moduleLocation);
        } catch (IOException e) {
            throw new StorageException(localize.getMessage("storage.file.error.dir.create"), e);
        }
    }

    @Override
    public FileUploadEntity store(MultipartFile file, StorageModules module) {

        provideStorePlace(module);

        try {
            if (file.isEmpty()) {
                throw new StorageException(localize.getMessage("storage.file.error.file.empty"));
            }

            Path moduleLocation = location.resolve(module.getDir());
            String uniqueFileName = getUniqueFileName(file.getOriginalFilename());
            Path destinationFile = moduleLocation.resolve(Paths.get(uniqueFileName))
                    .normalize().toAbsolutePath();

            // This is a security check
            if (!destinationFile.getParent().equals(moduleLocation.toAbsolutePath())) {
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
                fileEntity.setModule(module);

                return repo.save(fileEntity);
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

    private String getUniqueFileName(String originalFilename) {
        String u = UUID.randomUUID().toString() + "-" + LocalDateTime.now().getNano();

        var ext = getExtensionByStringHandling(originalFilename);
        if (ext.isPresent()) {
            u = u + "." + ext.get();
        }
        return u;
    }

    // @Override
    // public Path load(String filename) {
    // return location.resolve(filename);
    // }

    @Override
    public Optional<ContentFile> loadAsResource(String filename) {

        var files = repo.findFirstBySavedName(filename);
        Optional<ContentFile> result = Optional.empty();
        if (files.size() > 0) {
            var entity = files.get(0);
            try {
                StorageModules module = entity.getModule();
                Path file;
                if (module == null) {
                    file = location.resolve(entity.getSavedName());
                } else {
                    file = location.resolve(module.getDir()).resolve(entity.getSavedName());
                }

                Resource resource = new UrlResource(file.toUri());
                if (resource.exists() || resource.isReadable()) {
                    result = Optional.of(new ContentFile(resource, entity.getFileType()));
                    // } else {
                    // throw new StorageFileNotFoundException(
                    // localize.getMessage("storage.file.error.read.file") + filename);

                }
            } catch (MalformedURLException e) {
                // throw new
                // StorageFileNotFoundException(localize.getMessage("storage.file.error.read.file")
                // + filename,
                // e);
            }
        }

        return result;
    }

    // @Override
    // public void deleteAll() {
    // FileSystemUtils.deleteRecursively(location.toFile());
    // }

    @Override
    public void deleteById(Long id) {
        if (id == null || id.equals(Long.valueOf(0))) {
            return;
        }

        Optional<FileUploadEntity> entityOp = repo.findById(id);
        if (entityOp.isPresent()) {

            StorageModules module = entityOp.get().getModule();

            Path file;
            if (module == null) {
                file = location.resolve(entityOp.get().getSavedName());
            } else {
                file = location.resolve(module.getDir()).resolve(entityOp.get().getSavedName());
            }

            try {
                repo.deleteById(id);
                Files.deleteIfExists(file);
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
        return repo.findById(id);
    }

}
