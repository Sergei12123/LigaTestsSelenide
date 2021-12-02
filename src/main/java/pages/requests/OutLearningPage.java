package pages.requests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class OutLearningPage {
    @Step("Указать учебный центр")
    public void setCollege(String college){
        Configuration.timeout=30000;
        $(byAttribute("name","edu_facility")).setValue(college);
        Configuration.timeout=10000;
    }

    @Step("Указать курс")
    public void setCourse(String course){
        $(byAttribute("name","course")).setValue(course);
    }

    @Step("Указать формат мероприятия/тип билета")
    public void setType(String type){
        $(byAttribute("name","format")).setValue(type);
    }

    @Step("Указать почту")
    public void setMail(String mail){
        $(byXpath("//*[contains(text(), 'Почта')]/../../../*//input")).setValue(mail);
    }

    @Step("Указать даты обучения")
    public void setDate(LocalDate beginDate, LocalDate endDate) {
        DateTimeFormatter newDateFormat = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy г.");
        Assertions.assertTrue(endDate.isAfter(beginDate) && beginDate.isAfter(LocalDate.now()),"Дата начала обучения позже даты конца обучения");
        SelenideElement selenideElement1=$$(byAttribute("placeholder","Выберите даты")).first();
        selenideElement1.click();
        SelenideElement dateElement1=$(byAttribute("aria-label",newDateFormat.format(beginDate)));
        while(!dateElement1.isDisplayed()) {
            $(byCssSelector(".vc-arrow.is-right")).click();
        }
        $(byAttribute("aria-label",newDateFormat.format(beginDate))).click();
        SelenideElement selenideElement2=$$(byAttribute("placeholder","Выберите даты")).last();
        selenideElement2.click();
        SelenideElement dateElement2=$(byAttribute("aria-label",newDateFormat.format(endDate)));
        while(!dateElement2.isDisplayed()) {
            $(byCssSelector(".vc-arrow.is-right")).click();
        }
        $(byAttribute("aria-label",newDateFormat.format(endDate))).click();

    }

    @Step("Указать URL учебного заведения")
    public void setURL(String url){
        $(byAttribute("name","url")).setValue(url);
    }

    @Step("Указать стоимость")
    public void setPrice(int price){
        $(byAttribute("name","price")).setValue(Integer.toString(price));
    }

    @Step("Нажать кнопку \"Отправить\"")
    public void clickSendRequest(){
        $(byText("Отправить")).click();
    }
}
