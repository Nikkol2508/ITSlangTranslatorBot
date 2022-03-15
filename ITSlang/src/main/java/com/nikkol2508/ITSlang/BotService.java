package com.nikkol2508.ITSlang;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class BotService extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "ITSlangTranslatorBot";
    }

    @Override
    public String getBotToken() {
        return "5280871293:AAHtabr2es4ylSP1TKOs-xDPlsdVuscNLT8" ;
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
                outMessage.setText(inMessage.getText() + " повторюшка");
                execute(outMessage);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
