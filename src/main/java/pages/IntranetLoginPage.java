package pages;

import at.model.User;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class IntranetLoginPage {
    @Step("Произвести авторизацию на портале \"Intranet\"")
    public void login(User user,String url){
        open(url);
        $(byId("login")).setValue(user.getLogin());
        $(byId("pass")).setValue(user.getPassword()).pressEnter();
    }
}
