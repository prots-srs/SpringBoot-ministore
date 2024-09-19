package com.protsdev.ministore.pagePanelProduct;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.protsdev.ministore.enums.ProductTypes;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Page<ProductEntity> findByNameContainsIgnoreCase(String name, Pageable pageable);

    List<ProductEntity> findByTypeAndActiveTrueOrderBySortAsc(ProductTypes type);
}
