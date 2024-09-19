package com.protsdev.ministore.pagePanelService;

import com.protsdev.ministore.enums.IconConfigurator;
import com.protsdev.ministore.pageCommon.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Services
 */
@Entity
@Table(name = "services")
@Getter
@Setter
public class ServiceEntity extends BaseEntity {
    private Integer sort;
    private Boolean active;
    private String title;
    private String description;
    private IconConfigurator iconClass;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((sort == null) ? 0 : sort.hashCode());
        result = prime * result + ((active == null) ? 0 : active.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((iconClass == null) ? 0 : iconClass.hashCode());
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
        ServiceEntity other = (ServiceEntity) obj;
        if (sort == null) {
            if (other.sort != null)
                return false;
        } else if (!sort.equals(other.sort))
            return false;
        if (active == null) {
            if (other.active != null)
                return false;
        } else if (!active.equals(other.active))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (iconClass != other.iconClass)
            return false;
        return true;
    }

}