package pages;

import at.exceptions.StepNotImplementedException;
import at.model.enums.Category;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selectors.*;

public class IntranetMainPage {
    @Step("Перейти во вкладку {0}")
    public void goToTab(Category category){
        switch (category){
            case REQUEST:
                $$(byXpath("//*[@class=\"main-nav__list main-nav__list--shorter\"]//*")).find(Condition.exactText(category.getType())).click();
                break;
            default:
                throw new StepNotImplementedException("Шаг перейти во вкладку "+category.getType()+"не реализован для категории "+category.getType(),this.getClass());
        }
    }

    @Step("Найти в поиске:{0}")
    public void search(String textForSearch){
        $(byAttribute("aria-labelledby","Поиск")).shouldBe(Condition.exist).parent().click();
        $(byAttribute("placeholder","Я хочу найти...")).setValue(textForSearch);
    }

    @Step("Выбрать в поисковой выдаче первого кандидата и нажать \"войти от имени\"")
    public void loginOnBehalfFirst(){
        $(byText("Войти от имени")).shouldBe(Condition.exist);
        $$(byText("Войти от имени")).first().click();
        $(byText("Войти от имени")).should(Condition.not(Condition.exist));
        refresh();
    }
}
