package pages.requests;

import at.exceptions.StepNotImplementedException;
import at.model.Product;
import at.model.enums.SubCategory;
import at.parser.Context;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;


import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class MainRequestsPage {
    @Step ("Начать новую заявку с типом {0}")
    public void beginNewRequest(SubCategory subCategory){
        Configuration.timeout = 40000;
        switch (subCategory){
            case TRANSFER_BETWEEN_BLOCKS:
                $$(byXpath("//*[@class=\"services__filter\"]//*")).find(Condition.text("Заявки на перевод")).click();
                $(".services-add-button").shouldBe(Condition.exist).click();
                $(byText(subCategory.getSubType())).click();
                break;
            case OUT_LEARNING:
                $$(byXpath("//*[@class=\"services__filter\"]//*")).find(Condition.text("Заявки на внешнее обучение")).click();
                $(".services-add-button").shouldBe(Condition.exist).click();
                $(byText("Создать")).click();
        }
        Configuration.timeout = 4000;
    }

    @Step("Выбрать тип необходимой заявки")
    public void chooseNededRequest(SubCategory subCategory){
        Configuration.timeout = 40000;
        switch (subCategory){
            case TRANSFER_BETWEEN_BLOCKS:
                $$(byXpath("//*[@class=\"services__filter\"]//*")).find(Condition.text("Заявки на перевод")).click();
                break;
            case OUT_LEARNING:
                $$(byXpath("//*[@class=\"services__filter\"]//*")).find(Condition.text("Заявки на внешнее обучение")).click();
                break;
            default:
                throw new StepNotImplementedException("Нельзя выбрать заявку с типом "+subCategory.getSubType(), this.getClass());
        }
        Configuration.timeout = 4000;

    }

    @Step ("Получить основные данные по созданной заявке")
    public void getNeededRequest(){
        Configuration.timeout = 30000;
        Product product=(Product)Context.getSavedObject("Продукт");
        switch (product.getSubCategory()){
            case TRANSFER_BETWEEN_BLOCKS:
                $(".transfer-item__id").shouldBe(Condition.exist);
                product.setNumber($$(".transfer-item__id").first().getText());
                product.setCandidateName($$(".transfer-item__name").first().getText());
                product.setDate($$(".transfer-item__date").first().getText());
                break;
            case OUT_LEARNING:
                $(byXpath("//*[@class=\"services__item-left-wrapper\"]")).shouldBe(Condition.exist);
                product.setNumber($$(byXpath("//*[@class=\"services__item-left-wrapper\"]//*")).first().getText());
                product.setCandidateName($$(".services__item-name").first().getText());
                product.setDate($$(byXpath("//*[@class=\"services__item-left-wrapper\"]//*")).first().getText());
                break;
        }
        Configuration.timeout = 4000;
    }

    @Step ("Выбрать созданную заявку")
    public void chooseRequest(String number,SubCategory subCategory){
        Configuration.timeout = 40000;
        switch (subCategory){
            case TRANSFER_BETWEEN_BLOCKS:
                $(".transfer-item__id").shouldBe(Condition.exist);
                $$(".transfer-item__id").find(Condition.exactText(number)).click();
                break;
            case OUT_LEARNING:
                $(byXpath("//*[@class=\"services__item-left-wrapper\"]")).shouldBe(Condition.exist);
                $$(byXpath("//*[@class=\"services__item-left-wrapper\"]//*")).find(Condition.exactText(number)).click();
                break;
        }
        Configuration.timeout = 4000;
    }

    @Step ("Отменить заявку")
    public void cancelRequest(SubCategory subCategory,String assistantType,String requestNumber) {
        switch (subCategory){
            case TRANSFER_BETWEEN_BLOCKS:
                if(assistantType.equals("Ассистент ПС"))
                    $(byXpath("//*[text()=\""+requestNumber+"\"]/../../..//*[contains(text(), 'Отмена')]")).click();
                else
                    $(byXpath("//*[text()=\""+requestNumber+"\"]/../../..//*[contains(text(), 'Отменить')]")).click();
                break;
            default:
                throw new StepNotImplementedException("Шаг отменить заявку не реализован для подкатегории"+subCategory.getSubType(),this.getClass());
        }
    }

    @Step("Проверить что заявка была отменена")
    public void checkRequestCanceled(String number,SubCategory subCategory) {
        Configuration.timeout = 40000;
        refresh();
        switch (subCategory){
            case TRANSFER_BETWEEN_BLOCKS:
                $(".transfer-item__id").shouldBe(Condition.exist);
                $$(".transfer-item__id").find(Condition.exactText(number)).parent().parent().lastChild().find(byClassName("transfer-item__status")).shouldBe(Condition.exactText("Заявка отменена"));
                break;
            default:
                throw new StepNotImplementedException("Шаг \"Проверить что заявка была отменена\" не реализован для подкатегории"+subCategory.getSubType(),this.getClass());
        }
        Configuration.timeout = 4000;

    }//todo Глянуть после БД
}

