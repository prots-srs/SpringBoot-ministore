package com.protsprog.ministore.repositories;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.protsprog.ministore.models.ProductEntity;

@Repository
public interface ProductRepository extends ListCrudRepository<ProductEntity, Long> {
    List<ProductEntity> findAllByOrderBySortAsc();

    List<ProductEntity> findByProductTypeCodeAndActiveTrueOrderBySortAsc(String string);
}
