package at.steps;

import at.helpers.HookHelper;
import at.helpers.MyTestWatcher;
import at.parser.Context;
import at.utils.allure.AllureHelper;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.*;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;


/**
 * Класс содержит действия для каждого сценария или шага
 */
@Log4j2
public class Hook implements BeforeEachCallback, AfterEachCallback {
    private static boolean started = false;

    /**
     * Подключение к базе данных
     */
    @BeforeEach
    @Step("Инициализация БД 694")
    public void initializeTest694DB() {
        HookHelper.getDatabase("crc_694_ontest_26fcfb5c");
        initializeWD();
    }

    /**
     * Запуск браузера
     */
    @Step("Инициализация браузера")
    public void initializeWD() {
        HookHelper.clearWebDriver();
        HookHelper.initWebDriver();
    }

    /**
     * Вывод контекста и приложить скриншот в случае ошибки
     *
     *
     */
    @Step("Дополнительная информация")
    public void addInformation() {
        HookHelper.printContext();
    }

    /**
     * Очистить контекст
     */
    @Step("Очистка контекста")
    public void clearContext() {
        AllureHelper.execIgnoreException("Очистка", () ->
        {
            Context.clearLocalStorage();
            return null;
        });
    }

    /**
     * Закрыть браузер и закрыть подключение к БД
     */
    @AfterEach
    @Step("Закрытие браузера и подключений к БД")
    public void clear() {
        AllureHelper.execIgnoreException("Закрытие браузера", () ->
        {
            HookHelper.clearWebDriver();
            return null;
        });
        AllureHelper.execIgnoreException("Закрытие коннекта к БД", () ->
        {
            HookHelper.clearDatabaseConnections();
            HookHelper.clearWebDriver();
            return null;
        });
        clearContext();
        addInformation();
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        clear();

    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        initializeTest694DB();
    }
}