package com.khokhlov.tendermonitoring.telegram.service;

import com.khokhlov.tendermonitoring.model.entity.TrackedTender;
import com.khokhlov.tendermonitoring.telegram.listener.TenderBot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
                if (tender.getTrackedKeyword() != null && tender.getTrackedKeyword().getKeyword() != null && !tender.getTrackedKeyword().getKeyword().isBlank()) {
                    message.append("🔎 Ключевое слово: ").append(tender.getTrackedKeyword().getKeyword()).append("\n\n");
                }

                message.append("🔹 ").append(tender.getTitle()).append("\n");

                if (tender.getPurchaseCode() != null && !tender.getPurchaseCode().isBlank()) {
                    message.append("№: ").append(tender.getPurchaseCode()).append("\n");
                }

                List<String> lawAndStage = new ArrayList<>();
                if (tender.getProcurementLaws() != null && !tender.getProcurementLaws().isBlank())
                    lawAndStage.add("🏛 Закон: " + tender.getProcurementLaws());
                if (tender.getStage() != null && !tender.getStage().isBlank())
                    lawAndStage.add("🏷 Этап: " + tender.getStage());
                if (!lawAndStage.isEmpty()) {
                    message.append(String.join("   ", lawAndStage)).append("\n");
                }

                if (tender.getPurchaseObject() != null && !tender.getPurchaseObject().isBlank()) {
                    message.append("💼 Объект: ").append(tender.getPurchaseObject()).append("\n");
                }

                if (tender.getAuthor() != null && !tender.getAuthor().isBlank()) {
                    message.append("👤 Заказчик: ").append(tender.getAuthor()).append("\n");
                }

                if (tender.getPrice() != null) {
                    message.append("💰 Цена: ")
                            .append(formatPrice(tender.getPrice()))
                            .append(" ")
                            .append(tender.getCurrency() != null ? tender.getCurrency() : "₽")
                            .append("\n");
                }

                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                if (tender.getPublishedDate() != null) {
                    message.append("📅 Публикация: ").append(fmt.format(tender.getPublishedDate())).append("   ");
                }
                if (tender.getExpirationDate() != null) {
                    message.append("🗓 Окончание: ").append(fmt.format(tender.getExpirationDate())).append("\n");
                } else {
                    message.append("\n");
                }

                if (tender.getLink() != null && !tender.getLink().isBlank()) {
                    message.append("🔗").append(tender.getLink());
                }
            }
        }
        return message.toString();
    }

    private String formatPrice(BigDecimal price) {
        return String.format("%,.2f", price).replace(',', ' ');
    }

}
