package at.steps;

import at.exceptions.StepNotImplementedException;
import at.helpers.Environment;
import at.helpers.HookHelper;
import at.model.Product;
import at.model.User;
import at.model.enums.Category;
import at.model.enums.SubCategory;
import at.parser.Context;
import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;
import pages.IntranetLoginPage;
import pages.IntranetMainPage;
import pages.requests.MainRequestsPage;

import static com.codeborne.selenide.Selenide.*;

public class UniversalSteps {
    @Step("{0} заходит на портал {1}")
    public static void userLoginWithRole(String role, String app){
        Environment environment= HookHelper.getEnvironment();
        for(User user: environment.getUsers()){
            if(user.getRole().equals(role)){
                Context.saveObject("Текущий пользователь",user);
                break;
            }
        }
        open(environment.getMainUrl());
        new IntranetLoginPage().login((User)Context.getSavedObject("Текущий пользователь"),
                environment.getMainUrl());
       }
    @Step("Перейти в категорию {0} во вкладку {1}")
    public static void goToPageByCategoryAndSubCategory(Category category, SubCategory subCategory){
        new IntranetMainPage().goToTab(category);
        switch (category){
            case REQUEST:
                new MainRequestsPage().chooseNededRequest(subCategory);
                break;
            default:
                throw new StepNotImplementedException("Нельзя перейти в категорию "+category.getType(),UniversalSteps.class);

        }
    }

    @Step("Зайти в систему {0} от имени {1}")
    public static void loginOnBehalf(String system, String name){
        switch (system){
            case "Intranet":
                IntranetMainPage intranetMainPage=new IntranetMainPage();
                intranetMainPage.search(name);
                intranetMainPage.loginOnBehalfFirst();
                break;
            default:
                throw new StepNotImplementedException("Зайти в систему "+system+ " от имени "+name+"не реализован для системы "+system,UniversalSteps.class);

        }
    }

}
