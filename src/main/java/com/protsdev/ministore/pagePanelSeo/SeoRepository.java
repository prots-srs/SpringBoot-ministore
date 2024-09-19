package com.protsdev.ministore.pagePanelSeo;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface SeoRepository extends JpaRepository<SeoEntity, Long> {

    SeoEntity findFirstByPath(String siteUrl);

    Page<SeoEntity> findByPathContaining(String path, Pageable pageable);
}
