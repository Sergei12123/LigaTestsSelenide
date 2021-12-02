package at.database.requests;

import at.database.DatabaseDAO;
import at.database.IDatabase;
import at.exceptions.StepNotImplementedException;
import at.helpers.HookHelper;
import at.model.Database;
import at.model.enums.Category;
import at.model.enums.SubCategory;
import managers.DatabaseManager;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import static java.lang.Thread.sleep;

/**
 * Класс содержит методы для работы с таблицей "SIEBEL.cx_sr"
 */
public class RequestsDAO extends DatabaseDAO implements IDatabase {

    private final String historyTABLE = "bp_history";
    private final String statusesTABLE ="bp_statuses";
    private final String statusesNamesTABLE="bp_status_names";
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
     * Метод возвращающий статус продукта по номеру, категории и подкатегории
     *
     * @param number
     * @param category
     * @param subCategory
     * @return
     */
    public String getRequestStatus(String number, Category category, SubCategory subCategory) {
        String query="";
        switch (category){
            case REQUEST:
                switch (subCategory){
                    case TRANSFER_BETWEEN_BLOCKS:
                        query = "SELECT name\n" +
                                "FROM " + historyTABLE + " \n" +
                                "WHERE bp_id = '" + number + "' order by date desc";
                        break;
                    case OUT_LEARNING:
                        query = "select name from "+statusesNamesTABLE+"\n" +
                                "where id=(select bp_status_name_id from "+statusesTABLE+" where bp_id='"+number+
                                "' order by created_at desc limit 1)";
                        break;
                    default:
                        throw new StepNotImplementedException("Не обозначен запрос для выбранной подкатегории заявки "+subCategory.getSubType(), this.getClass());

                }
                break;
            default:
                throw new StepNotImplementedException("Не обозначен запрос для выбранной категории "+category.getType(), this.getClass());
        }

        stepAllureQueryText(query);
        ArrayList<Map<String, String>> result = this.database.select(query);

        return (result.size() > 0) ? result.get(0).get("name") : null;

    }





}
