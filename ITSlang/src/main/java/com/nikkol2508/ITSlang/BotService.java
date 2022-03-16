package com.nikkol2508.ITSlang;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@Service
public class BotService extends TelegramLongPollingBot {

    private final String  botUsername;
    private final String botToken;
    private final SlangRepository slangRepository;

    public BotService(@Value("${telegram-bot.name}") String botUsername,
                      @Value("${telegram-bot.token}") String botToken,
                      SlangRepository slangRepository) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.slangRepository = slangRepository;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken ;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            //проверяем есть ли сообщение и текстовое ли оно
            if (update.hasMessage() && update.getMessage().hasText()) {
                Message inMessage = update.getMessage();
                SendMessage outMessage = new SendMessage();
                //Указываем в какой чат будем отправлять сообщение
                outMessage.setChatId(inMessage.getChatId().toString());
                if (inMessage.getText().equals("/start")) {
                    outMessage.setText("Введите слово из сферы IT");
                    execute(outMessage);
                }
                else {
                    getDescriptions(inMessage, outMessage);
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void getDescriptions(Message inMessage, SendMessage outMessage) throws TelegramApiException {
        List<SlangTranslator> slangTranslatorList = slangRepository
                .findTop5BySearchQueryEnContainingOrSearchQueryRuContaining(inMessage.getText().toLowerCase(),
                        inMessage.getText().toLowerCase());
        if (slangTranslatorList.isEmpty()) {
            outMessage.setText("Такого слова пока нет в нашей базе, но скоро мы его добавим. Попробуйте позже.");
            log.info(inMessage.getText());
            execute(outMessage);
        } else {
            for (SlangTranslator slangTranslator : slangTranslatorList) {
                outMessage.setText(slangTranslator.getDescription());
                execute(outMessage);
            }
            if (slangTranslatorList.size() == 5) {
                outMessage.setText("Найдено много совпадений! Уточните запрос.");
                execute(outMessage);
            }
        }
    }
}
