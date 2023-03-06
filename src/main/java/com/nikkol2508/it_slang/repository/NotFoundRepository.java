package com.nikkol2508.it_slang.repository;

import com.nikkol2508.it_slang.repository.entity.NotFound;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotFoundRepository extends CrudRepository<NotFound, Integer> {

    @Query(value = "SELECT not_found_query AS notFoundQuery, count(id) AS countQuery FROM  not_found nf \n" +
            "GROUP BY notFoundQuery\n" +
            "ORDER BY countQuery DESC", nativeQuery = true)
    List<NotFoundCount> getNotFound();

    long deleteNotFoundsByNotFoundQueryIgnoreCase(String notFoundQuery);
}
