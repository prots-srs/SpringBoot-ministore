package com.protsdev.ministore.pagePanelService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    Page<ServiceEntity> findByTitleContainingOrDescriptionContainingAllIgnoreCase(String title,
            String description, Pageable pageable);

    List<ServiceEntity> findByActiveTrueOrderBySortAsc();
}
