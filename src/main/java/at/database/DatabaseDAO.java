package at.database;

import at.model.Database;
import at.utils.allure.AllureReport;
import io.qameta.allure.Step;
import managers.DatabaseManager;


import java.util.ArrayList;
import java.util.Map;

public abstract class DatabaseDAO {
    protected Database database = DatabaseManager.getDatabase();


    public DatabaseDAO(Database database) {
        this.database = database;
    }

    public DatabaseDAO() {

    }

    public Database getDatabase()
    {
        return database;
    }

    /**
     * Метод возвращает значение реквизита
     *
     * @param table  Таблица
     * @param rowId  Id записи
     * @param column Реквизит
     * @return String - значение указанного реквизита указанной записи
     */
    protected String getColumn(String table, String rowId, String column) {
        String query = "SELECT " + column + " \n" +
                "FROM " + table + " \n" +
                "WHERE row_id = '" + rowId + "'";
        stepAllureQueryText(query);
        ArrayList<Map<String, String>> result = this.database.select(query);


        String value = null;
        if (result.size() > 0) {
            value = (result.get(0).get(column) != null) ? result.get(0).get(column) : "";
        }

        return value;
    }

    /**
     * Метод возвращает все значения строки
     *
     * @param table Таблица
     * @param rowId Id записи
     * @return Map - данные указанной записи
     */
    protected Map<String, String> getRow(String table, String rowId) {
        String query = "SELECT * \n" +
                "FROM " + table + " \n" +
                "WHERE row_id = '" + rowId + "'";
        stepAllureQueryText(query);
        ArrayList<Map<String, String>> result = this.database.select(query);

        return (result.size() > 0) ? result.get(0) : null;
    }

    @Step("Текст запроса в базу")
    public void stepAllureQueryText(String queryText) {
        AllureReport.makeAttachTXT("Текст запроса в базу", queryText + "\n");
        System.out.println("Текст запроса в базу: \n" + queryText + "\n");
    }

    /**
     * Метод устанавливает значение для выбранного параметра
     *
     * @param table  Таблица
     * @param id     Id записи
     * @param column реквизит
     * @param value  устанавливаемое значение
     */
    protected void setColumn(String table, String id, String column, String value) {
        String query = "update " + table + "\n" +
                "set " + column + " = '" + value + "' \n" +
                "where row_id = '" + id + "'";
        stepAllureQueryText(query);
        this.database.update(query);
    }
}
