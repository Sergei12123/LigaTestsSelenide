package requests;

import at.helpers.MyTestWatcher;
import at.model.enums.Category;
import at.model.enums.SubCategory;
import at.steps.Hook;
import at.steps.PreconditionsSteps;
import at.steps.RequestSteps;
import at.steps.UniversalSteps;
import at.tagsForExecution.Ready;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;


@Epic("Заявки")
@Story("Заявка на внешнее обучение")
@ExtendWith({MyTestWatcher.class, Hook.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestsOutLearning {
    @Feature("Создание, менеджер отклонил")
    @Description("Создание, менеджер отклонил, статус “Заявка отклонена”")
    @DisplayName("Внешнее обучение, создание, менеджер отклонил")
    @Order(1)
    @Ready
    @Test
    public void testOutLearning() {
        PreconditionsSteps.existsProduct(Category.REQUEST, SubCategory.OUT_LEARNING);
        UniversalSteps.userLoginWithRole("Администратор", "Intranet");
        UniversalSteps.loginOnBehalf("Intranet", "Пронин Алексей");
        UniversalSteps.goToPageByCategoryAndSubCategory(Category.REQUEST, SubCategory.OUT_LEARNING);
        RequestSteps.beginNewRequest();
        RequestSteps.setOutLearningData(
                "тест",
                "тест",
                "тестирование",
                "test@exam.ru",
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(5),
                "http://test.ru",
                10000);
        RequestSteps.createRequest();
        RequestSteps.checkStatus("На согласовании у менеджера");
        UniversalSteps.userLoginWithRole("Администратор", "Intranet");
        UniversalSteps.loginOnBehalf("Intranet", "Михайлов Владислав");
        UniversalSteps.goToPageByCategoryAndSubCategory(Category.REQUEST, SubCategory.OUT_LEARNING);
        RequestSteps.cancelRequest("Менеджер");
        RequestSteps.checkStatus("Заявка отклонена");
    }

}
