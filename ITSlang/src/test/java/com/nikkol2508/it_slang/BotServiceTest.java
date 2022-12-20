package com.nikkol2508.it_slang;

import com.nikkol2508.it_slang.service.BotService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
class BotServiceTest {

    @Autowired
    private BotService botService;

    @Test
    void getBotUsername() {
        Assertions.assertEquals("ITSlangTranslatorBot", botService.getBotUsername());
    }

    @Test
    void getBotToken() {
        Assertions.assertEquals("5280871293:AAHtabr2es4ylSP1TKOs-xDPlsdVuscNLT8", botService.getBotToken());
    }

    @Test
    void onUpdateReceived() {
    }
}