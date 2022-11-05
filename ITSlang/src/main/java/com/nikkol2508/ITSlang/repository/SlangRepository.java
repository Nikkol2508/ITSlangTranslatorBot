package com.nikkol2508.ITSlang.repository;

import com.nikkol2508.ITSlang.repository.entity.SlangTranslator;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SlangRepository extends CrudRepository<SlangTranslator, Integer> {

    List<SlangTranslator> findTop5BySearchQueryEnContainingOrSearchQueryRuContaining(String searchQueryEn, String searchQueryRu);
}
