package com.protsprog.ministore.repositories;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.protsprog.ministore.models.SEOs;

@Repository
public interface SeoRepository extends ListCrudRepository<SEOs, Long>, PagingAndSortingRepository<SEOs, Long> {

    List<SEOs> findByPath(String siteUrl);

    List<SEOs> findByPathContaining(String path);
}
