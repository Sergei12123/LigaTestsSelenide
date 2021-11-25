package managers;

import at.helpers.HookHelper;
import at.model.Database;
import at.parser.Context;
import lombok.extern.java.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Класс содержит методы для работы с базой данных
 */
public abstract class DatabaseManager {

    private static final ThreadLocal<Database> databaseThreadLocal = new ThreadLocal<>();
    private static final Logger LOG = LogManager.getLogger(Context.class);


    public static void setDatabase(Database database) {
        LOG.info("Сохраняем данные БД");
        switch (database.getAlias()){
            case "crc_694_ontest_26fcfb5c" : { databaseThreadLocal.set(database); break; }
            default : { databaseThreadLocal.remove(); }
        }
    }

    public static void connect(){

    }

    public static Database getDatabase(){
        LOG.info("Получаем данные о БД");
        return databaseThreadLocal.get() == null?null:databaseThreadLocal.get();
    }

}