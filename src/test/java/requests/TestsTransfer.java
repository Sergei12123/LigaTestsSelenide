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
@Story("Заявки на перевод")
@ExtendWith({MyTestWatcher.class, Hook.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestsTransfer {

    @Feature("Заявка на перевод между блоками, доведение до статуса “Заявка отменена”")
    @Description("Создание, прикрепление скана, отправка на доработку, доведение до статуса “Заявка отменена” от Ассистента ОС")
    @DisplayName("Заявка на перевод между блоками")
    @Order(2)
    @Ready
    @Test
    public void testTransferBetweenBlocks() {
        PreconditionsSteps.existsProduct(Category.REQUEST, SubCategory.TRANSFER_BETWEEN_BLOCKS);
        UniversalSteps.userLoginWithRole("Администратор", "Intranet");
        UniversalSteps.loginOnBehalf("Intranet", "Вербицкая Анна");
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
        UniversalSteps.userLoginWithRole("Администратор", "Intranet");
        UniversalSteps.loginOnBehalf("Intranet", "Фомина Александра");
        UniversalSteps.goToPageByCategoryAndSubCategory(Category.REQUEST, SubCategory.TRANSFER_BETWEEN_BLOCKS);
        RequestSteps.cancelRequest("Ассистент ПС");
        RequestSteps.checkStatus("На доработке у Ассистента отдающей стороны");
        UniversalSteps.userLoginWithRole("Администратор", "Intranet");
        UniversalSteps.loginOnBehalf("Intranet", "Вербицкая Анна");
        UniversalSteps.goToPageByCategoryAndSubCategory(Category.REQUEST, SubCategory.TRANSFER_BETWEEN_BLOCKS);
        RequestSteps.cancelRequest("Ассистент ОС");
        RequestSteps.checkStatus("Заявка отменена");
    }


}
