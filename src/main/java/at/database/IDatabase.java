package at.database;

import java.util.Map;

public interface IDatabase {
    /**
     * Метод возвращает значение реквизита
     *
     * @param rowId  Id записи
     * @param column реквизит
     * @return String - значение указанного реквизита указанной записи
     */
    String getColumn(String rowId, String column);

    /**
     * Метод возвращает все значения строки
     *
     * @param rowId Id записи
     * @return Map - данные записи
     */
    Map<String, String> getRowMap(String rowId);

    /**
     * Обновления поля выбранной записи текущей таблицы
     *
     * @param rowId Идентификатор записи
     * @param column Название реквизита
     * @param value Значение реквизита
     */
    void setColumn(String rowId, String column, String value);
}
