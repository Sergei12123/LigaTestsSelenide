package pages.requests;

import at.utils.DocPath;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selenide.*;


public class TransferBetweenBlocksPage {
    @Step("Выбрать кандидата")
    public void chooseCandidat(String name){
        Configuration.timeout = 30000;
        $(byId("services-search-input")).shouldBe(Condition.exist).setValue(name+" ");
        $(byClassName("services__dropdown-search-person")).shouldBe(Condition.exist);
        $$(byClassName("services__dropdown-search-person")).first().click();
        Configuration.timeout = 10000;
    }

    @Step("Выбрать блок для перевода")
    public void chooseBlock(String block){
        $(byAttribute("data-name","department_parent_id")).click();
        $(byText(block)).click();
    }

    @Step("Выбрать практику для перевода")
    public void choosePractic(String practic){
        $(byAttribute("data-name","department_id")).click();
        $(byText(practic)).click();
    }

    @Step("Выбрать руководителя")
    public void chooseManager(String manager){
        Configuration.timeout = 30000;
        $(byAttribute("placeholder","Введите ФИО сотрудника")).setValue(manager+" ");
        $(".transfer__data-search-person").shouldBe(Condition.exist);
        $$(".transfer__data-search-person").first().click();
        Configuration.timeout = 10000;
    }

    @Step("Указать желаемую ЗП")
    public void setSalary(int salary){
        $(byAttribute("data-name","full_salary")).setValue(Integer.toString(salary));
    }

    @Step("Создать заявку")
    public void clickCreateRequest(){
        $(byText("Создать")).click();
        $(byText("Перейти к моим заявкам")).click();
    }

    @Step("Указать новую ставку")
    public void setNewRate(double newRate) {
        $(byAttribute("data-name","employment")).setValue(Double.toString(newRate));
        if($(byText("Прикрепить заявление")).exists()){
            $(".transfer__data-file").sendKeys(System.getProperty("user.dir").concat(DocPath.BID_CHANGE_APPLICATION.getPath()));
        }
    }


}
