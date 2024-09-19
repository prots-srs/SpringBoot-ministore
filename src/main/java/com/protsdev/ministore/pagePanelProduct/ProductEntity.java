package com.protsdev.ministore.pagePanelProduct;

import com.protsdev.ministore.enums.ProductTypes;
import com.protsdev.ministore.pageCommon.BaseEntity;
import com.protsdev.ministore.storage.FileUploadEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "goods")
@Getter
@Setter
public class ProductEntity extends BaseEntity {
    private Integer sort;
    private Boolean active;
    private String name;
    private Float price;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pic_id", referencedColumnName = "id")
    private FileUploadEntity picture;

    private ProductTypes type;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((sort == null) ? 0 : sort.hashCode());
        result = prime * result + ((active == null) ? 0 : active.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        result = prime * result + ((picture == null) ? 0 : picture.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        ProductEntity other = (ProductEntity) obj;
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
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (price == null) {
            if (other.price != null)
                return false;
        } else if (!price.equals(other.price))
            return false;
        if (picture == null) {
            if (other.picture != null)
                return false;
        } else if (!picture.equals(other.picture))
            return false;
        if (type != other.type)
            return false;
        return true;
    }
}
