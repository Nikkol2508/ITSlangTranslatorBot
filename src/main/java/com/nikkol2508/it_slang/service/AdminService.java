package com.nikkol2508.it_slang.service;

import com.nikkol2508.it_slang.dto.FormData;
import com.nikkol2508.it_slang.dto.NotFoundResult;
import com.nikkol2508.it_slang.repository.NotFoundRepository;
import com.nikkol2508.it_slang.repository.SlangRepository;
import com.nikkol2508.it_slang.repository.entity.NotFound;
import com.nikkol2508.it_slang.repository.entity.SlangTranslator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final NotFoundRepository notFoundRepository;
    private final SlangRepository slangRepository;

    public AdminService(NotFoundRepository notFoundRepository, SlangRepository slangRepository) {
        this.notFoundRepository = notFoundRepository;
        this.slangRepository = slangRepository;
    }

    public List<NotFoundResult> getNotFoundResultList() {

        return notFoundRepository.getNotFound()
                .stream()
                .map(e -> new NotFoundResult(e.getNotFoundQuery(), e.getCountQuery()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveToSlangTranslator(FormData formData) {
        SlangTranslator slangTranslator = new SlangTranslator();
        slangTranslator.setSearchQueryEn(formData.getQueryEn().toLowerCase());
        slangTranslator.setSearchQueryRu(formData.getQueryRu().toLowerCase());
        slangTranslator.setDescription(formData.getDescription());
        slangRepository.save(slangTranslator);
    }

    @Transactional
    public void deleteFromNotFound(FormData formData) {
        if (!formData.getQueryRu().isBlank()) {
            notFoundRepository.deleteNotFoundsByNotFoundQuery(formData.getQueryRu().toLowerCase());
        }
        if (!formData.getQueryEn().isBlank()) {
            notFoundRepository.deleteNotFoundsByNotFoundQuery(formData.getQueryEn().toLowerCase());
        }
    }

    @Transactional
    public void deleteAllFromNotFound() {
        notFoundRepository.deleteAll();
    }

    public List<String> getDescriptions(String query) {

        List<String> descriptionList = new ArrayList<>();

        List<SlangTranslator> slangTranslatorList = slangRepository
                .findTop5BySearchQueryEnContainingOrSearchQueryRuContaining(query.toLowerCase(),
                        query.toLowerCase());
        if (slangTranslatorList.isEmpty()) {
            descriptionList.add("Такого слова пока нет в нашей базе, но скоро мы его добавим. Попробуйте позже.");
            NotFound notFoundQuery = new NotFound();
            notFoundQuery.setNotFoundQuery(query.toLowerCase());
            notFoundRepository.save(notFoundQuery);
            return descriptionList;
        } else {
            for (SlangTranslator slangTranslator : slangTranslatorList) {
                descriptionList.add(slangTranslator.getDescription());
            }
            if (slangTranslatorList.size() == 5) {
                descriptionList.add("Найдено много совпадений! Уточните запрос.");
            }
            return descriptionList;
        }
    }

}
