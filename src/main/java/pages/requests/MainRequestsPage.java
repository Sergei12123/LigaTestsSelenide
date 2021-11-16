package pages.requests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import io.qameta.allure.Step;


import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class MainRequestsPage {
    @Step ("Выбираем нужную заявку")
    public void chooseNeededRequest(String request){
        Configuration.timeout = 30000;
        switch (request){
            case "Перевод между блоками":
                $$(byXpath("//*[@class=\"services__filter\"]//*")).find(Condition.text("Заявки на перевод")).click();
                $(".services-add-button").shouldBe(Condition.exist).click();
                $(byText(request)).click();
                break;
            case "Внешнее обучение":
                $$(byXpath("//*[@class=\"services__filter\"]//*")).find(Condition.text("Заявки на внешнее обучение")).click();
                $(".services-add-button").shouldBe(Condition.exist).click();
                $(byText("Создать")).click();
        }
        Configuration.timeout = 4000;
    }
}

