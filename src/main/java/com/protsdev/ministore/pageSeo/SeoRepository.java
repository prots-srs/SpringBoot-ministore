package com.protsdev.ministore.pageSeo;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

// import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface SeoRepository
        extends ListCrudRepository<SeoEntity, Long>, PagingAndSortingRepository<SeoEntity, Long> {

    List<SeoEntity> findByPath(String siteUrl);

    List<SeoEntity> findByPathContaining(String path);
}
