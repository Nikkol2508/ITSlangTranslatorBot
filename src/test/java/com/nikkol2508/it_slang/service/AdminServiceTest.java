package com.nikkol2508.it_slang.service;

import com.nikkol2508.it_slang.dto.FormData;
import com.nikkol2508.it_slang.dto.NotFoundResult;
import com.nikkol2508.it_slang.repository.NotFoundCount;
import com.nikkol2508.it_slang.repository.NotFoundRepository;
import com.nikkol2508.it_slang.repository.SlangRepository;
import com.nikkol2508.it_slang.repository.entity.SlangTranslator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    SlangRepository slangRepository;

    @Mock
    NotFoundRepository notFoundRepository;

    @InjectMocks
    private AdminService adminService;

    @Value("${telegram-bot.max_displayed_values}")
    private int MAX_DISPLAYED_VALUES;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        this.adminService = new AdminService(notFoundRepository, slangRepository);
    }

    @Test
    void getNotFoundResultListTest() {
        NotFoundCount notFoundCount1 = mock(NotFoundCount.class);
        List<NotFoundCount> notFoundCountList = new ArrayList<>();
        notFoundCountList.add(notFoundCount1);
        notFoundCountList.add(notFoundCount1);
        when(notFoundRepository.getNotFound()).thenReturn(notFoundCountList);

        NotFoundResult notFoundResult1 = new NotFoundResult(notFoundCount1.getNotFoundQuery(), notFoundCount1.getCountQuery());
        NotFoundResult notFoundResult2 = new NotFoundResult(notFoundCount1.getNotFoundQuery(), notFoundCount1.getCountQuery());
        List<NotFoundResult> notFoundResultList = new ArrayList<>();
        notFoundResultList.add(notFoundResult1);
        notFoundResultList.add(notFoundResult2);

        assertEquals(notFoundResultList, adminService.getNotFoundResultList());
    }

    @Test
    void saveToSlangTranslatorTest() {
        FormData formData = new FormData();
        formData.setQuery("query1");
        formData.setDescription("desc1");
        SlangTranslator slangTranslator = new SlangTranslator();
        slangTranslator.setSearchQuery(formData.getQuery());
        slangTranslator.setDescription(formData.getDescription());

        adminService.saveToSlangTranslator(formData);

        verify(slangRepository, times(1)).save(slangTranslator);
    }

    @Test
    void deleteFromNotFoundWhenQueryExists() {
        FormData formData = mock(FormData.class);
        when(formData.getQuery()).thenReturn("query1");

        adminService.deleteFromNotFound(formData);

        verify(notFoundRepository, times(1)).deleteNotFoundsByNotFoundQueryIgnoreCase("query1");
    }

    @Test
    void deleteFromNotFoundWhenQueryNotExists() {
        FormData formData = mock(FormData.class);
        when(formData.getQuery()).thenReturn("");

        adminService.deleteFromNotFound(formData);

        verify(notFoundRepository, never()).deleteNotFoundsByNotFoundQueryIgnoreCase(anyString());
    }

    @Test
    void deleteAllFromNotFoundTest() {

        adminService.deleteAllFromNotFound();

        verify(notFoundRepository, times(1)).deleteAll();
    }

    @Test
    void getDescriptionsWhenQueryExists() {
        String query = "ru";
        SlangTranslator slangTranslator1 = new SlangTranslator(1, "ru1", "desc1");
        SlangTranslator slangTranslator2 = new SlangTranslator(2, "ru2", "desc2");
        List<SlangTranslator> slangTranslatorList = new ArrayList<>();
        slangTranslatorList.add(slangTranslator1);
        slangTranslatorList.add(slangTranslator2);
        when(slangRepository.findTop5BySearchQueryContainingIgnoreCase(query))
                .thenReturn(slangTranslatorList);
        List<String> descriptionList = new ArrayList<>();
        descriptionList.add("desc1");
        descriptionList.add("desc2");

        assertEquals(descriptionList, adminService.getDescriptions(query));
        verify(slangRepository, times(1))
                .findTop5BySearchQueryContainingIgnoreCase(query);
    }

    @Test
    void getDescriptionsWhenQueryNotExists() {
        String query = "not found";
        List<SlangTranslator> slangTranslatorList = new ArrayList<>();
        when(slangRepository.findTop5BySearchQueryContainingIgnoreCase(query))
                .thenReturn(slangTranslatorList);
        List<String> descriptionList = new ArrayList<>();
        descriptionList.add("Такого слова пока нет в нашей базе, но скоро мы его добавим. Попробуйте позже.");

        assertEquals(descriptionList, adminService.getDescriptions(query));
        verify(notFoundRepository, times(1)).save(any());
    }

    @Test
    void getDescriptionsWhenQueryExistsMore5() {
        String query = "ru";
        SlangTranslator slangTranslator1 = new SlangTranslator(1, "ru1", "desc1");
        List<SlangTranslator> slangTranslatorListSpy = spy(new ArrayList<>());
        slangTranslatorListSpy.add(slangTranslator1);
        when(slangTranslatorListSpy.size()).thenReturn(MAX_DISPLAYED_VALUES);

        when(slangRepository.findTop5BySearchQueryContainingIgnoreCase(query))
                .thenReturn(slangTranslatorListSpy);
        List<String> descriptionList = new ArrayList<>();
        descriptionList.add("desc1");
        descriptionList.add("Найдено много совпадений! Уточните запрос.");

        assertEquals(descriptionList, adminService.getDescriptions(query));
    }
}