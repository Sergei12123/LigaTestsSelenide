package pages;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.*;

public class IntranetMainPage {
    @Step("Переходит во вкладку {0}")
    public void goToTab(String tabName){
        switch (tabName){
            case "Заявки":
                $$(byXpath("//*[@class=\"main-nav__list main-nav__list--shorter\"]//*")).find(Condition.exactText(tabName)).click();
                break;
        }
    }
}
