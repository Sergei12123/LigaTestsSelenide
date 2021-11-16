package at.steps;

import at.helpers.Environment;
import at.helpers.HookHelper;
import at.model.User;
import at.parser.Context;

import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class UniversalSteps {
    public static void userLoginWithRole(String role, String app){
        Environment environment= HookHelper.getEnvironment();
        for(User user: environment.getUsers()){
            if(user.getRole().equals(role)){
                Context.saveObject("Текущий пользователь",user);
                break;
            }
        }
        open(environment.getMainUrl());
        $(byId("login")).setValue(((User)Context.getSavedObject("Текущий пользователь")).getLogin());
        $(byId("pass")).setValue(((User)Context.getSavedObject("Текущий пользователь")).getPassword()).pressEnter();
    }

}
