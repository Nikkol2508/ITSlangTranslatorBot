package com.nikkol2508.ITSlang;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

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
                }
                else {
                    Optional<SlangTranslator> slangTranslator = slangRepository
                            .findBySearchQueryEnContainingOrSearchQueryRuContaining(inMessage.getText().toLowerCase(),
                                    inMessage.getText().toLowerCase());
                    String description = slangTranslator.isPresent()
                            ? slangTranslator.get().getDescription()
                            : "Такого слова пока нет в нашей базе, но скоро мы его добавим. Попробуйте позже.";
                    outMessage.setText(description);
                }
                execute(outMessage);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
