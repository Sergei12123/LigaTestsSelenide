package at.utils.allure;


import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
public class AllureReport {
    public AllureReport() {
    }

    @Step("{0}")
    public static void report(String text) {
        System.out.println(text);
    }

    @Attachment(
            value = "{0}",
            type = "text/xml"
    )
    public static String makeAttachXML(String name, String xml) {
        return xml;
    }

    @Attachment(
            value = "{0}",
            type = "text/plain"
    )
    public static String makeAttachTXT(String name, String text) {
        return text;
    }

    @Attachment(
            value = "{0}",
            type = "application/pdf"
    )
    public static byte[] makeAttachPDF(String name, byte[] pdf) {
        return pdf;
    }

    @Attachment(
            value = "{0}",
            type = "text/html"
    )
    public static String makeAttachHTML(String name, String html) {
        return html;
    }

    @Attachment(
            value = "{0}",
            type = "image/png"
    )
    public static byte[] makeAttachPNG(String name, byte[] image) {
        return image;
    }

    @Attachment(
            value = "{0}",
            type = "image/jpeg"
    )
    public static byte[] makeAttachJPEG(String name, byte[] image) {
        return image;
    }
}

