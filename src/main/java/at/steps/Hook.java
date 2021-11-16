package at.steps;

import at.helpers.HookHelper;
import at.parser.Context;
import at.utils.allure.AllureHelper;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;



/**
 * Класс содержит действия для каждого сценария или шага
 */
@Log4j2
public class Hook {

//    /**
//     * Подключение к базе данных
//     */
//    @BeforeEach
//    @Order(1)
//    @Step("Инициализация БД Siebel")
//    public void initializeSiebelDB() {
//        HookHelper.initDatabaseSiebel(HookHelper.getEnvironment());
//    }




    /**
     * Запуск браузера
     */
    @BeforeEach
    @Order(4)
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
    @AfterEach
    @Order(2)
    @Step("Дополнительная информация")
    public void addInformation() {
        HookHelper.printContext();
    }

    /**
     * Очистить контекст
     */
    @AfterEach
    @Order(1)
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
    @Order(0)
    @Step("Закрытие браузера и подключений к БД")
    public void clear() {
        AllureHelper.execIgnoreException("Закрытие браузера", () ->
        {
            HookHelper.clearWebDriver();
            return null;
        });
        AllureHelper.execIgnoreException("Закрытие коннекта к БД", () ->
        {
            //HookHelper.clearDatabaseConnections();
            HookHelper.clearWebDriver();
            return null;
        });
    }


}