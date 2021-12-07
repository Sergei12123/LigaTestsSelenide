package at.helpers;

import at.model.Database;
import at.utils.allure.AllureHelper;
import at.utils.allure.AllureReport;
import com.codeborne.selenide.Browser;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;

import managers.DatabaseManager;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.*;
import at.parser.Context;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codeborne.selenide.WebDriverRunner.*;

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
                log.debug("Trying to close the browser {}", getWebDriver().getClass().getSimpleName() + " ...");
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
     *
     * @return Environment - свойство в формате xml
     */
    public static Environment getEnvironment() {
        if (environment == null) {
            File xml = new File("src/main/resources/environments.xml");
            environment = new Environment(xml);
        }
        return environment;
    }

    /**
     * Запустить браузер
     */
    @Step("Инициализация браузера")
    public static void initWebDriver() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        RemoteWebDriver driver = new ChromeDriver(loadBrowserCapabilities());

        driver.manage().window().maximize();
        setWebDriver(driver);
    }

    /**
     * Установить свойства браузера
     *
     * @return DesiredCapabilities - свойства браузера
     */
    @Step("Установка свойств браузера")
    public static DesiredCapabilities loadBrowserCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities(BrowserType.CHROME, "", Platform.ANY);
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addExtensions(new File("src/main/resources/extensions/ModifyHeaders_2.2.4_0.crx"));
        chromeOptions.addArguments("--window-size=1280,1024", "--ignore-certificate-errors");
        Map<String, Object> prefs = new HashMap();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        chromeOptions.setExperimentalOption("prefs", prefs);
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
                UnexpectedAlertBehaviour.IGNORE);
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("screenResolution", "1920x1080x24");
        capabilities.setCapability("env", new String[]{"LANG=ru_RU.UTF-8", "LANGUAGE=ru:en", "LC_ALL=ru_RU.UTF-8"});
        return capabilities;
    }

    /**
     * Подключение к базе данных
     *
     * @param alias свойства среды
     */
    @Step("Инициализация БД")
    public static Database getDatabase(String alias) {
        if (database == null) {
            Map<String, String> bd = HookHelper.getEnvironment().databases.get(alias);
            database = new Database(alias,
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
     *
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
     */
    @Step("Делаем скриншот")
    public static void makeScreenshot() {
        AllureHelper.addAttachmentsToCase();
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
