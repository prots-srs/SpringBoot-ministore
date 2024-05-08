package com.protsprog.ministore.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.protsprog.ministore.models.ServiceItem;

import java.util.ArrayList;

@Repository
public interface ServiceRepository extends CrudRepository<ServiceItem, Long> {

    ArrayList<ServiceItem> findAllByOrderBySortAsc();

    ArrayList<ServiceItem> findByActiveOrderBySortAsc(Boolean active);
}
