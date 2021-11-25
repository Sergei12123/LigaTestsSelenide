package at.helpers;

import at.model.Database;
import at.utils.allure.AllureHelper;
import at.utils.allure.AllureReport;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import managers.DatabaseManager;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.UnreachableBrowserException;
import at.parser.Context;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codeborne.selenide.WebDriverRunner.closeWindow;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

/**
 * Класс описывает действия из класса Hook
 */
@Log4j2
public class HookHelper {

    private static Database database;
    private static Environment environment;

    public static final String DIRECTORY_PROJECT = System.getProperty("user.dir") + File.separator;

    /**
     * Закрытие браузера
     */
    public static void closeWebDriver() {
        log.debug("Close webdriver");
        try {
            closeWebDriver();
        } catch (Throwable t) {
            t.printStackTrace();
        }



        long start = System.currentTimeMillis();
        Thread t = new Thread(() -> {
            try {
                log.debug("Trying to close the browser {}", getWebDriver().getClass().getSimpleName()+ " ...");
                closeWebDriver();
            } catch (UnreachableBrowserException e) {

                log.warn("Browser is unreachable", e);
            }
        });
        t.setDaemon(true);
        t.start();
        try {
            t.join(5000);
            long duration = System.currentTimeMillis() - start;
            log.debug("Closed webdriver in {} ms", duration);
        } catch (InterruptedException e) {
            long duration = System.currentTimeMillis() - start;
            log.warn("Failed to close webdriver in " + duration + " ms", e);
        }
    }

    /**
     * Инициализация переменных для заданного стенда
     * @return Environment - свойство в формате xml
     */
    public static Environment getEnvironment() {
        if (environment == null){
            File xml = new File("src/main/resources/environments.xml");
            environment = new Environment(xml);
        }
        return environment;
    }

    /**
     * Запустить браузер
     *
     */
    @Step("Инициализация браузера")
    public static void initWebDriver() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        System.setProperty("selenide.browser", "Chrome");
    }

    /**
     * Подключение к базе данных
     * @param alias свойства среды
     */
    @Step("Инициализация БД")
    public static Database getDatabase(String alias) {
        if(database==null){
            Map<String, String> bd = HookHelper.getEnvironment().databases.get(alias);
            database= new Database(alias,
                    bd.get("serviceName"),
                    bd.get("protocol"),
                    bd.get("host"),
                    bd.get("user"),
                    bd.get("userPass"),
                    bd.get("port"),
                    bd.get("databaseName"),
                    bd.get("databasePass"));
            DatabaseManager.setDatabase(database);
        }
        return database;
    }


    /**
     * Возвращает работу браузера
     * @return boolean - работоспособность браузера
     */
    public static boolean isBrowserAlive() {
        try {
            getWebDriver().getCurrentUrl();
            return true;
        } catch (UnhandledAlertException unhandledAlertException) {
            AllureHelper.report("Отображается алерт: ".concat(unhandledAlertException.getAlertText()));
            getWebDriver().switchTo().alert().dismiss();
            return true;
        } catch (WebDriverException t) {
            return false;
        }
    }

    /**
     * Закрыть подключение к БД
     */
    public static void clearDatabaseConnections() {
        if (DatabaseManager.getDatabase() != null) {
            DatabaseManager.setDatabase(null);
        }
    }

    /**
     * Закрыть браузер и подключение к системе Siebel
     */
    public static void clearWebDriver() {
        try {
            Selenide.closeWebDriver();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Сделать скриншот для отчета
     *
     * @param dismissAlert алерт закрыть/не закрывать
     */
    public static void makeScreenshot(boolean dismissAlert) {
        boolean browserState = HookHelper.canMakeScreenshot(dismissAlert);
        if (browserState) {
            AllureHelper.addAttachmentsToCase();
        }
    }

    /**
     * Возможность сделать скриншот
     *
     * @param dismissAlert закрытие алерта
     * @return boolean - возможно/невозможно сделать скриншот
     */
    public static boolean canMakeScreenshot(boolean dismissAlert) {
        try {
            getWebDriver().getCurrentUrl();
            return true;
        } catch (UnhandledAlertException unhandledAlertException) {
            AllureReport.report("Отображается алерт: ".concat(unhandledAlertException.getAlertText()));
            if (dismissAlert) {
                getWebDriver().switchTo().alert().dismiss();
                AllureReport.report("Закрытие алерта");
                return true;
            }
            return false;
        } catch (WebDriverException t) {
            AllureReport.report("Состояние браузера повис или закрыт по тайм-ауту");
            return false;
        }
    }

    /**
     * Вывод контекста
     */
    @Step("Вывод содержимого контекста")
    public static void printContext() {
        String context = Context.getSavedVariables().entrySet().stream()
                .map(e -> e.getKey().concat(" : ").concat(e.getValue()))
                .collect(Collectors.joining("\n"));
        context = context.concat("\n\nObjects:\n\n");
        context += Context.getSavedObjects().entrySet().stream().map(e -> e.getKey().concat(" : ")
                .concat(e.getValue().toString())
                .concat(" ")).collect(Collectors.joining("\n"));
        AllureHelper.makeAttachTXT("Context", context);
        log.info("Context:\n".concat(context));
    }

}
