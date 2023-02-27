package com.nikkol2508.it_slang.service;

import com.nikkol2508.it_slang.dto.FormData;
import com.nikkol2508.it_slang.dto.NotFoundResult;
import com.nikkol2508.it_slang.repository.NotFoundRepository;
import com.nikkol2508.it_slang.repository.SlangRepository;
import com.nikkol2508.it_slang.repository.entity.NotFound;
import com.nikkol2508.it_slang.repository.entity.SlangTranslator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final NotFoundRepository notFoundRepository;
    private final SlangRepository slangRepository;

    @Value("${telegram-bot.max_displayed_values}")
    private int MAX_DISPLAYED_VALUES;

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
        slangTranslator.setSearchQuery(formData.getQuery().toLowerCase());
        slangTranslator.setDescription(formData.getDescription());
        slangRepository.save(slangTranslator);
    }

    @Transactional
    public void deleteFromNotFound(FormData formData) {
        if (!formData.getQuery().isBlank()) {
            notFoundRepository.deleteNotFoundsByNotFoundQueryIgnoreCase(formData.getQuery());
        }
    }

    @Transactional
    public void deleteAllFromNotFound() {
        notFoundRepository.deleteAll();
    }

    public List<String> getDescriptions(String query) {
        List<String> descriptionList = new ArrayList<>();
        List<SlangTranslator> slangTranslatorList = slangRepository
                .findTop5BySearchQueryContainingIgnoreCase(query.toLowerCase());
        if (slangTranslatorList.isEmpty()) {
            descriptionList.add("Такого слова пока нет в нашей базе, но скоро мы его добавим. Попробуйте позже.");
            NotFound notFoundQuery = new NotFound();
            notFoundQuery.setNotFoundQuery(query.toLowerCase());
            notFoundRepository.save(notFoundQuery);
        } else {
            for (SlangTranslator slangTranslator : slangTranslatorList) {
                descriptionList.add(slangTranslator.getDescription());
            }
            if (slangTranslatorList.size() == MAX_DISPLAYED_VALUES) {
                descriptionList.add("Найдено много совпадений! Уточните запрос.");
            }
        }
        return descriptionList;
    }
}
