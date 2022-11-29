package com.nikkol2508.ITSlang.service;

import com.nikkol2508.ITSlang.repository.NotFoundRepository;
import com.nikkol2508.ITSlang.repository.SlangRepository;
import com.nikkol2508.ITSlang.repository.entity.NotFound;
import com.nikkol2508.ITSlang.repository.entity.SlangTranslator;
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

    private final String  botUsername;
    private final String botToken;
    private final SlangRepository slangRepository;
    private final NotFoundRepository notFoundRepository;

    public BotService(@Value("${telegram-bot.name}") String botUsername,
                      @Value("${telegram-bot.token}") String botToken,
                      SlangRepository slangRepository, NotFoundRepository notFoundRepository) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.slangRepository = slangRepository;
        this.notFoundRepository = notFoundRepository;
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
            if (update.hasMessage() && update.getMessage().hasText()) {
                Message inMessage = update.getMessage();
                SendMessage outMessage = new SendMessage();
                outMessage.setChatId(inMessage.getChatId().toString());
                if (inMessage.getText().equals("/start")) {
                    outMessage.setText("Введите слово из сферы IT");
                    execute(outMessage);
                }
                else if (inMessage.getText().equals("/nf")) {
                    List<NotFound> notFoundList = (List<NotFound>) notFoundRepository.findAll();
                    for (NotFound notFound : notFoundList) {
                        outMessage.setText(notFound.getNotFoundQuery());
                        execute(outMessage);
                    }
                }
                else {
                    getDescriptions(inMessage, outMessage);
                }
            }
        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            SendMessage outMessage = new SendMessage();
            Message inMessage = update.getMessage();
            outMessage.setChatId(inMessage.getChatId().toString());
            outMessage.setText("Что-то пошло не так. Попробуйте ещё раз, или повторите попытку позже.");
            try {
                execute(outMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

    private void getDescriptions(Message inMessage, SendMessage outMessage) throws TelegramApiException, IOException {
        List<SlangTranslator> slangTranslatorList = slangRepository
                .findTop5BySearchQueryEnContainingOrSearchQueryRuContaining(inMessage.getText().toLowerCase(),
                        inMessage.getText().toLowerCase());
        if (slangTranslatorList.isEmpty()) {
            outMessage.setText("Такого слова пока нет в нашей базе, но скоро мы его добавим. Попробуйте позже.");
            log.info(inMessage.getText());
            NotFound notFoundQuery = new NotFound();
            notFoundQuery.setNotFoundQuery(inMessage.getText());
            notFoundRepository.save(notFoundQuery);
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
