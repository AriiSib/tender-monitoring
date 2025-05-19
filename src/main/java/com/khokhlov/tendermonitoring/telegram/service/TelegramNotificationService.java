package com.khokhlov.tendermonitoring.telegram.service;

import com.khokhlov.tendermonitoring.model.entity.TrackedTender;
import com.khokhlov.tendermonitoring.telegram.listner.TenderBot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramNotificationService {

    private final TenderBot telegramBot;

    public void notifyUser(List<TrackedTender> trackedTenders, Long chatId) {
        String message = buildMessage(trackedTenders, chatId);
        telegramBot.send(chatId, message);
    }

    private String buildMessage(List<TrackedTender> trackedTenders, Long chatId) {
        StringBuilder message = new StringBuilder();
        if (!trackedTenders.isEmpty() && chatId != null) {
            message.append("🔔 Найдены новые тендеры:\n\n");

            for (TrackedTender tender : trackedTenders) {
                message.append("🔹 ")
                        .append(tender.getTitle())
                        .append("\n")
                        .append(tender.getLink())
                        .append("\n\n");
            }
        }
        return message.toString();
    }
}
