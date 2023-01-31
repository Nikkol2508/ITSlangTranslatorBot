package com.nikkol2508.it_slang.service;

import com.nikkol2508.it_slang.repository.NotFoundRepository;
import com.nikkol2508.it_slang.repository.SlangRepository;
import com.nikkol2508.it_slang.repository.entity.NotFound;
import com.nikkol2508.it_slang.repository.entity.SlangTranslator;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class BotService extends TelegramLongPollingBot {

    private final String botUsername;
    private final String botToken;
    private final int MAX_DISPLAYED_VALUES;
    private final SlangRepository slangRepository;
    private final NotFoundRepository notFoundRepository;

    public BotService(@Value("${telegram-bot.name}") String botUsername,
                      @Value("${telegram-bot.token}") String botToken,
                      @Value("${telegram-bot.max_displayed_values}") int MAX_DISPLAYED_VALUES,
                      SlangRepository slangRepository, NotFoundRepository notFoundRepository) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.slangRepository = slangRepository;
        this.notFoundRepository = notFoundRepository;
        this.MAX_DISPLAYED_VALUES = MAX_DISPLAYED_VALUES;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                Message inMessage = update.getMessage();
                if (inMessage.getText().equals("/start")) {
                    sendMessage(inMessage.getChatId(),
                            "Здравствуйте " + inMessage.getChat().getFirstName() + " Введите слово из сферы IT");
                } else if (inMessage.getText().equals("/nf")) {
                    List<NotFound> notFoundList = (List<NotFound>) notFoundRepository.findAll();
                    for (NotFound notFound : notFoundList) {
                        sendMessage(inMessage.getChatId(), notFound.getNotFoundQuery());
                    }
                } else {
                    getDescriptions(inMessage);
                }
            }
        } catch (Exception ex) {
            sendMessage(update.getMessage().getChatId(),
                    "Что-то пошло не так. Попробуйте ещё раз, или повторите попытку позже.");
            ex.printStackTrace();
        }
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage outMessage = new SendMessage();
        outMessage.setChatId(chatId);
        outMessage.setText(text);
        try {
            execute(outMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void getDescriptions(Message inMessage) {
        String textMessage = inMessage.getText().toLowerCase();
        List<SlangTranslator> slangTranslatorList = slangRepository
                .findTop5BySearchQueryContainingIgnoreCase(textMessage);
        if (slangTranslatorList.isEmpty()) {
            log.info(inMessage.getText());
            NotFound notFoundQuery = new NotFound();
            notFoundQuery.setNotFoundQuery(textMessage);
            notFoundRepository.save(notFoundQuery);
            sendMessage(inMessage.getChatId(),
                    "Такого слова пока нет в нашей базе, но скоро мы его добавим. Попробуйте позже.");
        } else {
            for (SlangTranslator slangTranslator : slangTranslatorList) {
                sendMessage(inMessage.getChatId(), slangTranslator.getDescription());
            }
            if (slangTranslatorList.size() == MAX_DISPLAYED_VALUES) {
                sendMessage(inMessage.getChatId(), "Найдено много совпадений! Уточните запрос.");
            }
        }
    }
}
