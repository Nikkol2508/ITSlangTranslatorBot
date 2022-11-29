package com.nikkol2508.ITSlang.service;

import com.nikkol2508.ITSlang.dto.FormData;
import com.nikkol2508.ITSlang.dto.NotFoundResult;
import com.nikkol2508.ITSlang.repository.NotFoundRepository;
import com.nikkol2508.ITSlang.repository.SlangRepository;
import com.nikkol2508.ITSlang.repository.entity.SlangTranslator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        slangTranslator.setSearchQueryEn(formData.getQueryEn());
        slangTranslator.setSearchQueryRu(formData.getQueryRu());
        slangTranslator.setDescription(formData.getDescription());
        slangRepository.save(slangTranslator);
    }

    @Transactional
    public void deleteFromNotFound(FormData formData) {
        if (!formData.getQueryRu().isBlank()) {
            notFoundRepository.deleteNotFoundsByNotFoundQuery(formData.getQueryRu());
        }
        if (!formData.getQueryEn().isBlank()) {
            notFoundRepository.deleteNotFoundsByNotFoundQuery(formData.getQueryEn());
        }
    }

    @Transactional
    public void deleteAllFromNotFound() {
        notFoundRepository.deleteAll();
    }

}
