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
        Configuration.timeout=4000;
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
        $(byAttribute("name","email-994112")).setValue(mail);
    }

    @Step("Указать даты обучения")
    public void setDate(String beginDate, String endDate) {
        DateTimeFormatter oldDateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter newDateFormat = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy г.");
        LocalDate d1= LocalDate.parse(beginDate,oldDateFormat);
        LocalDate d2= LocalDate.parse(endDate,oldDateFormat);
        Assertions.assertTrue(d2.isAfter(d1) && d1.isAfter(LocalDate.now()));
        SelenideElement selenideElement1=$$(byAttribute("placeholder","Выберите даты")).first();
        selenideElement1.click();
        SelenideElement dateElement1=$(byAttribute("aria-label",newDateFormat.format(d1)));
        while(!dateElement1.isDisplayed()) {
            $(byCssSelector(".vc-arrow.is-right")).click();
        }
        $(byAttribute("aria-label",newDateFormat.format(d1))).click();
        SelenideElement selenideElement2=$$(byAttribute("placeholder","Выберите даты")).last();
        selenideElement2.click();
        SelenideElement dateElement2=$(byAttribute("aria-label",newDateFormat.format(d2)));
        while(!dateElement2.isDisplayed()) {
            $(byCssSelector(".vc-arrow.is-right")).click();
        }
        $(byAttribute("aria-label",newDateFormat.format(d2))).click();

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
