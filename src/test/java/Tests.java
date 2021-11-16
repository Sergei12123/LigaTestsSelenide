import at.helpers.MyTestWatcher;
import at.steps.UniversalSteps;
import at.tagsForExecution.Ready;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.IntranetMainPage;
import pages.requests.MainRequestsPage;
import pages.requests.OutLearningPage;
import pages.requests.TransferBetweenBlocksPage;


@Epic("Заявки")
@ExtendWith(MyTestWatcher.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Tests {

    @Feature("Заявка на перевод между блоками")
    @Description("Создание заявки на перевод между блоками и отправка Ассистенту ПС")
    @DisplayName("Заявка на перевод между блоками")
    @Order(1)
    @Test
    @Ready
    public void testTransferBetweenBlocks(){
        UniversalSteps.userLoginWithRole("Ассистент","Intranet");
        //Выбираем нужную заявку
        new IntranetMainPage().goToTab("Заявки");
        MainRequestsPage mainRequestsPage=new MainRequestsPage();
        mainRequestsPage.chooseNeededRequest("Перевод между блоками");
        //Выбираем кандидата
        TransferBetweenBlocksPage transferBetweenBlocksPage= new TransferBetweenBlocksPage();
        transferBetweenBlocksPage.chooseCandidat("Иванов");
        transferBetweenBlocksPage.chooseBlock("Блок Восток");
        //Выбираем практику для перевода
        transferBetweenBlocksPage.choosePractic("Smart-City");
        //Выбираем руководителя
        transferBetweenBlocksPage.chooseManager("а");
        //Указываем ЗП
        transferBetweenBlocksPage.setSalary(100000);
        //Завершаем создание заявки
        transferBetweenBlocksPage.clickCreateRequest();
    }
    @Feature("Заявка на внешнее обучение")
    @Description("Создание заявки на внешнее обучение")
    @DisplayName("Заявка на внешнее обучение")
    @Order(2)
    @Ready
    @Test
    public void testOutLearning(){
        UniversalSteps.userLoginWithRole("Ассистент","Intranet");
        //Выбираем нужную заявку
        new IntranetMainPage().goToTab("Заявки");
        MainRequestsPage mainRequestsPage=new MainRequestsPage();
        mainRequestsPage.chooseNeededRequest("Внешнее обучение");
        //Указываем учебный центр
        OutLearningPage outLearningPage=new OutLearningPage();
        outLearningPage.setCollege("МГУ");
        //Указываем название мероприятия
        outLearningPage.setCourse("Java");
        //Указываем формат мероприятия/тип билета
        outLearningPage.setType("Дистанционный");
        //Указываем почту для регистрации (личная/внешняя)
        outLearningPage.setMail("abcd@gmail.com");
        //Указываем даты
        outLearningPage.setDate("20.11.2021","21.12.2022");
        outLearningPage.setURL("https://www.mirea.ru");
        //Указываем стоимость
        outLearningPage.setPrice(10000);
        //Завершаем создание заявки
        outLearningPage.clickSendRequest();
    }
}
