package com.khokhlov.tendermonitoring.telegram.listener;

import com.khokhlov.tendermonitoring.model.dto.UserDTO;
import com.khokhlov.tendermonitoring.model.dto.UserLoginDTO;
import com.khokhlov.tendermonitoring.service.UserService;
import com.khokhlov.tendermonitoring.telegram.model.dto.AuthState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TenderBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final UserService userService;

    private final Map<Long, AuthState> authSessions = new ConcurrentHashMap<>();
    private final Map<Long, UserDTO> authorizedUsers = new ConcurrentHashMap<>();
    private final String token;

    public TenderBot(UserService userService,
                     @Value("${telegram.bot.token}") String token) {
        this.token = token;
        this.userService = userService;
        this.telegramClient = new OkHttpTelegramClient(token);
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        String text = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        authorization(chatId, text);
    }

    public void send(Long chatId, String message) {
        SendMessage msg = SendMessage.builder()
                .chatId(chatId.toString())
                .text(message)
                .build();
        try {
            telegramClient.execute(msg);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Error when sending a message: " + e.getMessage());
        }
    }

    private void authorization(Long chatId, String text) {
        if (authorizedUsers.containsKey(chatId)) {
//            send(chatId, "✅ Вы уже авторизованы.");
            return;
        }

        if (text.equals("/start")) {
            authSessions.put(chatId, new AuthState());
            send(chatId, "Введите логин:");
            return;
        }

        AuthState state = authSessions.get(chatId);
        if (state == null) {
            send(chatId, "❗ Напишите /start, чтобы начать авторизацию.");
            return;
        }

        if (!state.isWaitingForPassword()) {
            state.setUsername(text);
            state.setWaitingForPassword(true);
            send(chatId, "Введите пароль:");
        } else {
            String username = state.getUsername();
            String password = text;

            try {
                UserDTO user = userService.login(new UserLoginDTO(username, password));
                authorizedUsers.put(chatId, user);
                authSessions.remove(chatId);

                userService.updateTelegramChatId(user.id(), chatId);

                send(chatId, "✅ Успешный вход. Теперь вы будете получать уведомления.");
            } catch (Exception e) {
                authSessions.remove(chatId);
                send(chatId, "❌ Неверные логин или пароль. Попробуйте /start заново.");
            }
        }
    }

}
