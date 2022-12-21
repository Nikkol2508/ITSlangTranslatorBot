package com.nikkol2508.it_slang.repository;

import com.nikkol2508.it_slang.repository.entity.NotFound;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotFoundRepository extends CrudRepository<NotFound, Integer> {

    @Query(value = "SELECT not_found_query AS notFoundQuery, count(id) AS countQuery FROM  not_found nf \n" +
            "GROUP BY notFoundQuery\n" +
            "ORDER BY countQuery DESC", nativeQuery = true)
    List<NotFoundCount> getNotFound();

    long deleteNotFoundsByNotFoundQuery(String notFoundQuery);
}
