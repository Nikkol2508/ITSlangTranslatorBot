package com.nikkol2508.it_slang.repository;

import com.nikkol2508.it_slang.repository.entity.SlangTranslator;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SlangRepository extends CrudRepository<SlangTranslator, Integer> {

    List<SlangTranslator> findTop5BySearchQueryContainingIgnoreCase(String searchQuery);
}

