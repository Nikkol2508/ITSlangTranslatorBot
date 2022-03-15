package com.nikkol2508.ITSlang;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SlangRepository extends CrudRepository<SlangTranslator, Integer> {

    Optional<SlangTranslator> findBySearchQueryEnContainingOrSearchQueryRuContaining(String searchQueryEn, String searchQueryRu);
}
