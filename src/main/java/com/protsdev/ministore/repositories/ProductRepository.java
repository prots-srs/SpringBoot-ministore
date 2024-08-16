package com.protsdev.ministore.repositories;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.protsdev.ministore.models.ProductEntity;

@Repository
public interface ProductRepository extends ListCrudRepository<ProductEntity, Long> {
    List<ProductEntity> findAllByOrderBySortAsc();

    List<ProductEntity> findByProductTypeCodeAndActiveTrueOrderBySortAsc(String string);
}