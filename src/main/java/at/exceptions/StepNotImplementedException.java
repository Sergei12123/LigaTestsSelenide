package at.exceptions;

/**
 * Класс исключение StepNotImplementedException
 */
public class StepNotImplementedException extends RuntimeException {

    public StepNotImplementedException(String stepDsc, String objectName) {
        super("Шаг \""
                .concat(stepDsc)
                .concat("\" не реализован для ")
                .concat(objectName));
    }

    public StepNotImplementedException(String stepDsc, Class clazz) {
        super("Шаг \""
                .concat(stepDsc)
                .concat("\" не реализован в классе ")
                .concat(clazz.getName()));
    }
}