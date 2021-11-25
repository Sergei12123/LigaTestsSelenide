package at.steps;

import at.database.requests.RequestsDAO;
import at.exceptions.StepNotImplementedException;
import at.model.Product;
import at.model.enums.Category;
import at.parser.Context;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import pages.requests.MainRequestsPage;
import pages.requests.OutLearningPage;
import pages.requests.TransferBetweenBlocksPage;

public class RequestSteps {

    @Step("Заполняет данные о кандидате и блоке для перевода")
    public static void setCandidatAndNewBlockData(String candidatName,
                                                  String newBlock,
                                                  String newPractice,
                                                  double newRate,
                                                  String managerName,
                                                  int newSalary){
        TransferBetweenBlocksPage transferBetweenBlocksPage= new TransferBetweenBlocksPage();
        transferBetweenBlocksPage.chooseCandidat(candidatName);
        transferBetweenBlocksPage.chooseBlock(newBlock);
        transferBetweenBlocksPage.choosePractic(newPractice);
        transferBetweenBlocksPage.setNewRate(newRate);

        transferBetweenBlocksPage.chooseManager(managerName);
        transferBetweenBlocksPage.setSalary(newSalary);
    }

    @Step("Создает новую заявку")
    public static void createRequest() {
        Product product=(Product) Context.getSavedObject("Продукт");
        switch (product.getSubCategory()){
            case TRANSFER_BETWEEN_BLOCKS:
                new TransferBetweenBlocksPage().clickCreateRequest();
                break;
            case OUT_LEARNING:
                new OutLearningPage().clickSendRequest();
                break;
        }
        getAllRequestData();
    }

    @Step("Заполняет данные о месте обучения")
    public static void setOutLearningData(String collegeName,
                                          String courseName,
                                          String typeLearning,
                                          String mail,
                                          String beginDate,
                                          String finishDate,
                                          String url,
                                          int price){
        OutLearningPage outLearningPage=new OutLearningPage();
        outLearningPage.setCollege(collegeName);
        outLearningPage.setCourse(courseName);
        outLearningPage.setType(typeLearning);
        outLearningPage.setMail(mail);
        outLearningPage.setDate(beginDate,finishDate);
        outLearningPage.setURL(url);
        outLearningPage.setPrice(price);
    }

    @Step("Сохранить все данные новой заявки")
    private static void getAllRequestData() {
        new MainRequestsPage().getNeededRequest();
    }

    @Step("Проверить что статус заявки {0}")
    public static void checkStatus(String expectedStatus) {
        Product product=(Product) Context.getSavedObject("Продукт");
        String reqStatus=new RequestsDAO().getRequestStatus(product.getNumber());
        Assertions.assertEquals(expectedStatus,reqStatus);
        product.setStatus(reqStatus);
    }

    @Step("Отменяет заявку")
    public static void cancelRequest(String assistantType) {
        Product product=(Product) Context.getSavedObject("Продукт");
        MainRequestsPage mainRequestsPage=new MainRequestsPage();
        mainRequestsPage.chooseRequest(product.getNumber(), product.getSubCategory());
        mainRequestsPage.cancelRequest(product.getSubCategory(),assistantType,product.getNumber());
    }
    @Step("Начинает новую заявку")
    public static void beginNewRequest() {
        Product product=(Product) Context.getSavedObject("Продукт");
        new MainRequestsPage().beginNewRequest(product.getSubCategory());

    }

    @Step("Проверяет что заявка была отменена")
    public static void checkRequestCanceled() {
        Product product=(Product) Context.getSavedObject("Продукт");
        new MainRequestsPage().checkRequestCanceled(product.getNumber(),product.getSubCategory());
    }
}
