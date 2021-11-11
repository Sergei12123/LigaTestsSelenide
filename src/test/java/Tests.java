import com.codeborne.selenide.Browser;
import com.codeborne.selenide.Condition;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Заявки")
public class Tests {
    @BeforeEach
    public void beforeAll(){
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        System.setProperty("selenide.browser", "Chrome");

    }

    public void setPasoLog(){
        $(byId("login")).setValue("sealivanov");
        $(byId("pass")).setValue("Huvefe94").pressEnter();
    }
    @Epic("Заявки на перевод")
    @Feature("Заявка на перевод между блоками")
    @Description("Создание заявки на перевод между блоками и отправка Ассистенту ПС")
    @DisplayName("Заявка на перевод между блоками")
    @Test
    public void testTransferBetweenBlocks(){
        open("https://crc-694-ontest-26fcfb5c.test-intranet.digitalleague.ru/services-transfer");
        setPasoLog();
        //Выбираем нужную заявку
        while(!$(".services-add-button__text").exists())
            sleep(2000);
        $(".services-add-button__text").should(Condition.exist).click();
        $(byText("Перевод между блоками")).click();
        //Выбираем кандидата
        $(byId("services-search-input")).setValue("Иванов ");
        while (!$(byClassName("services__dropdown-search-person")).exists())
            sleep(1500);
        $$(byClassName("services__dropdown-search-person")).first().click();
        //Выбираем блок для перевода
        $(byAttribute("data-name","department_parent_id")).click();
        $$(byCssSelector("[data-id=\"547\"][ data-name=\"department_parent_id\"]")).find(Condition.exactText("Блок Восток")).click();
        //Выбираем практику для перевода
        $(byAttribute("data-name","department_id")).click();
        $$(byCssSelector("[data-id=\"547\"][ data-name=\"department_id\"]")).find(Condition.exactText("Smart-City")).click();
        //Выбираем руководителя
        $(byAttribute("placeholder","Введите ФИО сотрудника")).setValue("А");
        while (!$(".transfer__data-search-person").exists())
            sleep(500);
        $$(".transfer__data-search-person").first().click();
        //Указываем ЗП
        $(byAttribute("data-name","full_salary")).setValue("100000");
        //Завершаем создание заявки
        $(byText("Создать")).click();
        $(byText("Перейти к моим заявкам")).click();
    }
    @Epic("Заявка на внешнее обучение")
    @Feature("Заявка на внешнее обучение")
    @Description("Создание заявки на внешнее обучение")
    @DisplayName("Заявка на внешнее обучение")
    @Test
    public void testOutLearning(){
        open("https://crc-694-ontest-26fcfb5c.test-intranet.digitalleague.ru/services/education");
        setPasoLog();
        //Выбираем нужную заявку
        while(!$(".services-add-button__text").exists())
            sleep(2000);
        $(".services-add-button__text").should(Condition.exist).click();
        $(byText("Создать")).click();

        //Указываем учебный центр
        $(byAttribute("name","edu_facility")).setValue("МГУ");
        //Указываем название мероприятия
        $(byAttribute("name","course")).setValue("Java");
        //Указываем формат мероприятия/тип билета
        $(byAttribute("name","format")).setValue("Дистанционный");
        //Указываем почту для регистрации (личная/внешняя)
        $(byAttribute("name","email-994112")).setValue("abcd@gmail.com");
        //Указываем даты
        $$(byAttribute("placeholder","Выберите даты")).first().click();
        $(byAttribute("aria-label","четверг, 11 ноября 2021 г.")).click();
        $(byAttribute("aria-label","вторник, 30 ноября 2021 г.")).click();
        $(byAttribute("name","url")).setValue("https://www.mirea.ru");
        //Указываем стоимость
        $(byAttribute("name","price")).setValue("10000");
        //Завершаем создание заявки
        $(byText("Отправить")).click();
    }

    @AfterEach
    public void clearWebDriver() {
        try {
            closeWebDriver();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
