package requests;

import at.helpers.MyTestWatcher;
import at.model.enums.Category;
import at.model.enums.SubCategory;
import at.steps.Hook;
import at.steps.PreconditionsSteps;
import at.steps.RequestSteps;
import at.steps.UniversalSteps;
import at.tagsForExecution.Ready;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;


@Epic("Заявки")
@ExtendWith({MyTestWatcher.class,Hook.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Tests {

    @Feature("Заявка на перевод между блоками, доведение до статуса “Заявка отменена”")
    @Description("Создание, прикрепление скана, отправка на доработку, доведение до статуса “Заявка отменена” от Ассистента ОС")
    @DisplayName("Заявка на перевод между блоками")
    @Order(1)
    @Ready
    @Test
    public void testTransferBetweenBlocks(){
        PreconditionsSteps.existsProduct(Category.REQUEST,SubCategory.TRANSFER_BETWEEN_BLOCKS);
        RequestSteps.checkStatus("Новая заявка");

        UniversalSteps.userLoginWithRole("Администратор","Intranet");
        UniversalSteps.loginOnBehalf("Intranet","Вербицкая Анна");
        UniversalSteps.goToPageByCategoryAndSubCategory(Category.REQUEST, SubCategory.TRANSFER_BETWEEN_BLOCKS);
        RequestSteps.beginNewRequest();
        RequestSteps.setCandidatAndNewBlockData(
                "Боброва",
                "Блок Инновационных решений BSS/GSS",
                "Практика Решений для электронной коммерции",
                0.9,
                "Белоусов",
                60000);
        RequestSteps.createRequest();
        RequestSteps.checkStatus("Новая заявка");
        UniversalSteps.userLoginWithRole("Администратор","Intranet");
        UniversalSteps.loginOnBehalf("Intranet","Фомина Александра");
        UniversalSteps.goToPageByCategoryAndSubCategory(Category.REQUEST, SubCategory.TRANSFER_BETWEEN_BLOCKS);
        RequestSteps.cancelRequest("Ассистент ПС");
        UniversalSteps.userLoginWithRole("Администратор","Intranet");
        UniversalSteps.loginOnBehalf("Intranet","Вербицкая Анна");
        UniversalSteps.goToPageByCategoryAndSubCategory(Category.REQUEST, SubCategory.TRANSFER_BETWEEN_BLOCKS);
        RequestSteps.cancelRequest("Ассистент ОС");
        RequestSteps.checkRequestCanceled();
    }

    @Feature("Заявка на внешнее обучение feature")
    @Description("Создание, менеджер отклонил, статус “Заявка отклонена”")
    @DisplayName("Заявка на внешнее обучение DisplayName")
    @Story("Заявка на внешнее обучение Story")
    @Order(2)
    @Ready
    @Test
    public void testOutLearning(){
        UniversalSteps.userLoginWithRole("Администратор","Intranet");
        UniversalSteps.goToPageByCategoryAndSubCategory(Category.REQUEST,SubCategory.OUT_LEARNING);
        RequestSteps.beginNewRequest();
        RequestSteps.setOutLearningData(
                "МГУ",
                "Java",
                "Дистанционный",
                "abcd@gmail.com",
                "22.11.2021",
                "21.12.2022",
                "https://www.mirea.ru",
                10000);
        RequestSteps.createRequest();
    }
}
