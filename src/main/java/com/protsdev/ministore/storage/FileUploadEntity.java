package com.protsdev.ministore.storage;

import com.protsdev.ministore.enums.StorageModules;
import com.protsdev.ministore.pageCommon.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "FILE_UPLOAD")
@Setter
@Getter
public class FileUploadEntity extends BaseEntity {

    private String fileType;
    private Long fileSize;
    private String originalName;
    private String savedName;
    private StorageModules module;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fileType == null) ? 0 : fileType.hashCode());
        result = prime * result + ((fileSize == null) ? 0 : fileSize.hashCode());
        result = prime * result + ((originalName == null) ? 0 : originalName.hashCode());
        result = prime * result + ((savedName == null) ? 0 : savedName.hashCode());
        result = prime * result + ((module == null) ? 0 : module.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FileUploadEntity other = (FileUploadEntity) obj;
        if (fileType == null) {
            if (other.fileType != null)
                return false;
        } else if (!fileType.equals(other.fileType))
            return false;
        if (fileSize == null) {
            if (other.fileSize != null)
                return false;
        } else if (!fileSize.equals(other.fileSize))
            return false;
        if (originalName == null) {
            if (other.originalName != null)
                return false;
        } else if (!originalName.equals(other.originalName))
            return false;
        if (savedName == null) {
            if (other.savedName != null)
                return false;
        } else if (!savedName.equals(other.savedName))
            return false;
        if (module == null) {
            if (other.module != null)
                return false;
        } else if (!module.equals(other.module))
            return false;
        return true;
    }
}
