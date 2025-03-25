package com.khokhlov.tendermonitoring.service;

import com.khokhlov.tendermonitoring.model.entity.Tender;
import com.khokhlov.tendermonitoring.repository.TenderRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TenderService {

    private final TenderRepository tenderRepository;


    public List<Tender> fetchAndSaveTenders() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36");
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-browser-side-navigation");

        WebDriver driver = new ChromeDriver(options);

        try {
//            driver.get("https://zakupki.gov.ru/epz/order/extendedsearch/results.html");
//            driver.get("https://zakupki.gov.ru/epz/order/extendedsearch/results.html?searchString=%D1%83%D0%B1%D0%BE%D1%80%D0%BA%D0%B0+%D0%BF%D0%BE%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D0%B9&morphology=on&search-filter=%D0%94%D0%B0%D1%82%D0%B5+%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D1%8F&pageNumber=1&sortDirection=false&recordsPerPage=_10&showLotsInfoHidden=false&sortBy=UPDATE_DATE&fz44=on&fz223=on&af=on&currencyIdGeneral=-1&gws=%D0%92%D1%8B%D0%B1%D0%B5%D1%80%D0%B8%D1%82%D0%B5+%D1%82%D0%B8%D0%BF+%D0%B7%D0%B0%D0%BA%D1%83%D0%BF%D0%BA%D0%B8");
            driver.get("https://zakupki.gov.ru/epz/order/extendedsearch/results.html?searchString=%D1%83%D0%B1%D0%BE%D1%80%D0%BA%D0%B0+%D0%BF%D0%BE%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D0%B9&morphology=on&search-filter=%D0%94%D0%B0%D1%82%D0%B5+%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D1%8F&pageNumber=1&sortDirection=false&recordsPerPage=_100&showLotsInfoHidden=false&sortBy=UPDATE_DATE&fz44=on&fz223=on&af=on&currencyIdGeneral=-1&gws=%D0%92%D1%8B%D0%B1%D0%B5%D1%80%D0%B8%D1%82%D0%B5+%D1%82%D0%B8%D0%BF+%D0%B7%D0%B0%D0%BA%D1%83%D0%BF%D0%BA%D0%B8");
//            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));

            List<Tender> tenders = new ArrayList<>();
            List<WebElement> tenderElements = driver.findElements(By.cssSelector(".search-registry-entry-block"));

            for (WebElement element : tenderElements) {
                Tender tender = null;
                try {
                    String title = element.findElement(By.cssSelector(".registry-entry__header-top__title")).getText();
                    String description = element.findElement(By.cssSelector(".registry-entry__body-value")).getText();
                    String deadline = element.findElement(By.cssSelector(".data-block__value")).getText();
                    tender = new Tender(null, title, description, deadline);

                } catch (NoSuchElementException e) {
                    tender = new Tender();
                }

                tenders.add(tenderRepository.save(tender));
            }

            return tenders;
        } finally {
            driver.quit();
        }
    }

    public List<Tender> findAllTenders() {
        return tenderRepository.findAll();
    }
}
