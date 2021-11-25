package at.database.requests;

import at.database.DatabaseDAO;
import at.database.IDatabase;
import at.helpers.HookHelper;
import at.model.Database;
import managers.DatabaseManager;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import static java.lang.Thread.sleep;

/**
 * Класс содержит методы для работы с таблицей "SIEBEL.cx_sr"
 */
public class RequestsDAO extends DatabaseDAO implements IDatabase {

    private final String historyTABLE = "bp_history";
    /**
     * Constructor
     *
     * @param database Database atc.crm.soui:utility
     */
    public RequestsDAO(Database database) {
        super(database);
    }

    public RequestsDAO() { }

    /**
     * Метод возвращает значение реквизита
     *
     * @param rowId  Id записи
     * @param column реквизит
     * @return String - значение указанного реквизита указанной записи
     */
    @Override
    public String getColumn(String rowId, String column) {
        return this.getColumn(historyTABLE, rowId, column);
    }

    /**
     * Метод возвращает все значения строки
     *
     * @param rowId Идентификатор записи
     * @return Map - данные записи
     */
    @Override
    public Map<String, String> getRowMap(String rowId) {
        return this.getRow(historyTABLE, rowId);
    }

    /**
     * Обновления поля выбранной записи текущей таблицы
     *
     * @param rowId Идентификатор записи
     * @param column Название реквизита
     * @param value Значение реквизита
     */
    @Override
    public void setColumn(String rowId, String column, String value) {
        this.setColumn(historyTABLE, rowId, column, value);
    }

    /**
     * Метод возвращает статус заявления по его номеру
     *
     * @param number номер запроса на обслуживание
     * @return String - id запроса
     */
    public String getRequestStatus(String number) {

        String query = "SELECT name \n" +
                "FROM public." + historyTABLE + " \n" +
                "WHERE bp_id = '" + number + "' ordered by date desc";
        stepAllureQueryText(query);
        ArrayList<Map<String, String>> result = this.database.select(query);

        return (result.size() > 0) ? result.get(0).get("name") : null;

    }



}
