package at.utils.allure;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.util.ResultsUtils;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;
import java.util.UUID;
import java.util.function.Supplier;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

/**
 * Работа с allure
 */
@Log4j2
public class AllureHelper {
    private AllureHelper() {
    }

    /**
     * Выполнение методов с указанием шага
     *
     * @param name название шага allure
     * @param method выполнение метода
     * @param <T> объект
     * @return T - результат
     */
    public static <T> T execIgnoreException(String name, Supplier<T> method) {
        String uuid = UUID.randomUUID().toString();
        Allure.getLifecycle().startStep(uuid, (new StepResult()).setName(name).setStatus(Status.PASSED));
        try {
            return method.get();
        } catch (Throwable t) {
            log.trace(t);
            Allure.getLifecycle().updateStep(uuid, s -> s.setStatus(Status.FAILED)
                    .setStatusDetails(ResultsUtils.getStatusDetails(t).orElse(null)));
        } finally {
            Allure.getLifecycle().stopStep(uuid);
        }
        return null;
    }

    /**
     * Добавление скриншота и исходный код страницы в отчет после упавшего теста
     *
     */
    public static void addAttachmentsToCase() {
        byte[] screenshot = ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES);
        String pageSource = getWebDriver().getPageSource();
        if (screenshot != null && screenshot.length != 0)
            Allure.addAttachment("[SCREENSHOT]", "image/png", new ByteArrayInputStream(screenshot), "png");
        if (pageSource != null && pageSource.length() != 0)
            Allure.addAttachment("[PAGESOURCE]", "text", pageSource, "html");
    }

    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] screenshot() {
        return ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }
    /**
     * Добавление информационного текста в отчет
     *
     * @param text сообщение
     */
    @Step("{0}")
    public static void report(String text) {
        log.info(text);
    }

    /**
     * Создание XML файла в отчете
     *
     * @param name Название
     * @param xml текст xml
     * @return String - текст xml
     */
    @Attachment(value = "{0}", type = "text/xml")
    public static String makeAttachXML(String name, String xml) {
        return xml;
    }

    /**
     * Создание TXT файла в отчете
     *
     * @param name Название
     * @param text текст
     * @return String - текст
     */
    @Attachment(value = "{0}", type = "text/plain")
    public static String makeAttachTXT(String name, String text) {
        return text;
    }

    /**
     * Создание PDF файла в отчете
     *
     * @param name Название
     * @param pdf содержимое pdf
     * @return byte - файл в байтах
     */
    @Attachment(value = "{0}", type = "application/pdf")
    public static byte[] makeAttachPDF(String name, byte[] pdf) {
        return pdf;
    }

    /**
     * Создание JPEG файла в отчете
     *
     * @param name Название
     * @param image изображение
     * @return byte - файл в байтах
     */
    @Attachment(value = "{0}", type = "image/jpeg")
    public static byte[] makeAttachJPEG(String name, byte[] image) {
        return image;
    }
}
