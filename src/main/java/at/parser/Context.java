package at.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import at.exceptions.ParserException;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс содержит свойства для работы с контекстом
 */
public class Context {

    private static ThreadLocal<Map<String, String>> savedLocalVariables = ThreadLocal.withInitial(HashMap::new);
    private static ThreadLocal<Map<String, Object>> savedObjects = ThreadLocal.withInitial(HashMap::new);
    private static final Logger LOG = LogManager.getLogger(Context.class);

    private Context() {
    }

    /**
     * Возвращает значение переменной из контекста
     *
     * @return Map - key - переменная, value - значение
     */
    public static Map<String, String> getSavedVariables() {
        return savedLocalVariables.get();
    }

    /**
     * Возвращает объекты из контекста
     *
     * @return Map - key - переменная, value - объект
     */
    public static Map<String, Object> getSavedObjects() {
        return savedObjects.get();
    }

    /**
     * Сохраняет объект
     * @param name наименование объекта
     * @param object объект
     */
    public static void saveObject(String name, Object object) {
        savedObjects.get().put(name.toLowerCase(), object);
    }

    /**
     * Возвращает объект из контекста
     *
     * @param name название объекта
     * @return Object - возвращает объект
     */
    public static Object getSavedObject(String name) {
        Object result = savedObjects.get().get(name.toLowerCase());
        if (result == null) {
            LOG.error("Can not find object {}. Please check object name.", name);
            throw new ParserException("Can not find object " + name + ". Please check object name.");
        } else {
            return result;
        }
    }

    /**
     * Возвращает переменную из контекста
     *
     * @param key название переменной
     * @return String
     */
    public static String getLocalVariableValue(String key) {
        if (key == null) {
            LOG.error("Can not find local variable. Please check variable name.");
            throw new ParserException("Can not find local variable. Please check variable name.");
        } else {
            String result = savedLocalVariables.get().get(key.toLowerCase());
            if (result == null) {
                LOG.error("Can not find local variable with name {}.", key);
                throw new ParserException("Can not find local variable " + key + ".");
            } else {
                return result;
            }
        }
    }

    /**
     * Обнуление ThreadLocal
     */
    public static void clearLocalStorage() {
        savedLocalVariables.get().clear();
        savedObjects.get().clear();
    }

    /**
     * Сохраняет переменной
     * @param key переменная
     * @param value значение
     */
    public static void saveLocalVariable(String key, String value) {
        savedLocalVariables.get().put(key.toLowerCase(), value);
    }

    /**
     * Проверка наполняемости значения переменной
     *
     * @param name название переменной
     * @return boolean
     */
    public static boolean isSavedLocalVariableExist(String name) {
        String result = savedLocalVariables.get().get(name.toLowerCase());
        return result != null;
    }
}
