package at.model;


import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

@Getter
public class Database {

    private final String alias;
    private final String serviceName;
    private final String protocol;
    private final String tunnelHost;
    private String[][] users;
    private final String tunnelUser;
    private final String tunnelPassword;
    private final String tunnelPort;
    private final String databaseName;
    private final String databasePass;
    private int sqlQueryTimeout = 300;
    private String DB_URL;
    private int assinged_port;


    public Database(String alias, String serviceName, String protocol, String host, String user, String userPass, String port, String databaseName, String databasePass) {
        this.alias = alias;
        this.serviceName = serviceName;
        this.protocol = protocol;
        this.tunnelHost = host;
        this.tunnelUser = user;
        this.tunnelPassword = userPass;
        this.tunnelPort = port;
        this.databaseName = databaseName;
        this.databasePass = databasePass;
    }
    private Connection getConnection(String user, String password) {
        if(DB_URL== null)
            DB_URL="jdbc:postgresql://localhost:5432/postgres";
        Session session=null;

        JSch jSch=new JSch();
        try {
            session=jSch.getSession(tunnelUser,tunnelHost,Integer.parseInt(tunnelPort));
            session.setPassword(tunnelPassword);
            Properties config=new Properties();
            config.put("StrictHostKeyChecking","no");
            config.put("Compression", "yes");
            config.put("ConnectionAttempts","2");
            session.setConfig(config);
            session.connect();

            assinged_port=session.setPortForwardingL(5432,tunnelHost,Integer.parseInt(tunnelPort) );
        } catch (JSchException e) {
            e.printStackTrace();
        }
        StringBuilder url =
                new StringBuilder("jdbc:postgresql://localhost:");

        // use assigned_port to establish database connection
        url.append("5432").append ("/").append(databaseName);

        if (user != null && !user.trim().isEmpty()) {
            if (password != null && !password.trim().isEmpty()) {
                try {
                    return DriverManager.getConnection(url.toString(),databaseName,databasePass);
                } catch (Exception var4) {
                    System.out.println("Attempt to get database connection failed");
                    var4.printStackTrace();
                    Assertions.fail("Не удалось установить соединение с базой данных!");
                    return null;
                }
            } else {
                System.out.println("Password is not specified");
                Assertions.fail("Пароль пользователя базы данных не задан!");
                return null;
            }
        } else {
            System.out.println("User is not specified");
            Assertions.fail("Пользователь базы данных не задан!");
            return null;
        }
    }

    private void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException var3) {
            System.out.println("Close connection failed");
            var3.printStackTrace();
            Assertions.fail("Не удалось закрыть соединение с базой данных!");
        }

    }

    public ArrayList<Map<String, String>> select(String user, String password, String sqlQuery) {
        Connection connection = this.getConnection(user, password);
        Assertions.assertNotNull(connection, "Не удалось установить соединение с базой данных!");
        System.out.println("SQL QUERY:\n  " + sqlQuery.replaceAll("\\n", "\n  "));
        System.out.println("Execute SQL QUERY");

        Throwable var6;
        try {
            Statement statement = connection.createStatement();
            var6 = null;

            try {
                statement.setQueryTimeout(this.sqlQueryTimeout);
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                System.out.println("  Success");
                ResultSetMetaData metadata = resultSet.getMetaData();
                int columnCount = metadata.getColumnCount();
                ArrayList result = new ArrayList();

                while(resultSet.next()) {
                    Map<String, String> row = new TreeMap(String.CASE_INSENSITIVE_ORDER);

                    for(int i = 1; i <= columnCount; ++i) {
                        row.put(metadata.getColumnName(i), resultSet.getString(i));
                    }

                    result.add(row);
                }

                ArrayList var33 = result;
                return var33;
            } catch (Throwable var29) {
                var6 = var29;
                throw var29;
            } finally {
                if (statement != null) {
                    if (var6 != null) {
                        try {
                            statement.close();
                        } catch (Throwable var28) {
                            var6.addSuppressed(var28);
                        }
                    } else {
                        statement.close();
                    }
                }

            }
        } catch (SQLException var31) {
            System.out.println("  QUERY failed");
            var31.printStackTrace();
            this.assertQueryTimeout(var31.getMessage());
            Assertions.fail("Не удалось выполнить SQL запрос!");
            var6 = null;
        } finally {
            this.closeConnection(connection);
        }


        return null;
    }

    public ArrayList<Map<String, String>> select(String sqlQuery) {
        return this.select(this.tunnelUser, this.tunnelPassword, sqlQuery);
    }

    public ArrayList<Map<String, String>> select(String userAlias, String sqlQuery) {
        String[] user = this.getUserByAlias(userAlias);
        Assertions.assertNotNull(user, "Пользователь с псевдонимом '" + userAlias + "' не найден!");
        return this.select(user[1], user[2], sqlQuery);
    }

    public void setUsers(String[][] users) {
        if (users != null && users.length >= 1) {
            this.users = users;
        } else {
            System.out.println("List of users is not specified");
            Assertions.fail("Список пользователей базы данных не задан!");
        }

    }

    private String[] getUserByAlias(String userAlias) {
        if (userAlias != null && !userAlias.trim().isEmpty()) {
            this.setUsers(this.users);
            String[][] var2 = this.users;
            int var3 = var2.length;
            for(int var4 = 0; var4 < var3; ++var4) {
                String[] user = var2[var4];
                if (user[0].equalsIgnoreCase(userAlias)) {
                    return user;
                }
            }

            Assertions.fail("Пользователь с псевдонимом '" + userAlias + "' не найден в списке пользователей базы данных!");
            return null;
        } else {
            System.out.println("User Alias is not specified");
            Assertions.fail("Псевдоним пользователя базы данных не задан!");
            return null;
        }
    }
    public void update(String user, String password, String sqlUpdate) {
        Connection connection = this.getConnection(user, password);
        Assertions.assertNotNull(connection, "Не удалось установить соединение с базой данных!");
        System.out.println("SQL UPDATE:\n  " + sqlUpdate.replaceAll("\\n", "\n  "));
        System.out.println("Execute SQL UPDATE");

        try {
            Statement statement = connection.createStatement();
            Throwable var6 = null;

            try {
                statement.setQueryTimeout(this.sqlQueryTimeout);
                statement.executeUpdate(sqlUpdate);
                System.out.println("  Success");
            } catch (Throwable var24) {
                var6 = var24;
                throw var24;
            } finally {
                if (statement != null) {
                    if (var6 != null) {
                        try {
                            statement.close();
                        } catch (Throwable var23) {
                            var6.addSuppressed(var23);
                        }
                    } else {
                        statement.close();
                    }
                }

            }
        } catch (SQLException var26) {
            System.out.println("  UPDATE failed");
            var26.printStackTrace();
            this.assertQueryTimeout(var26.getMessage());
            Assertions.fail("Не удалось выполнить SQL UPDATE!");
        } finally {
            this.closeConnection(connection);
        }

    }

    public void update(String sqlUpdate) {
        this.update(this.tunnelUser, this.tunnelPassword, sqlUpdate);
    }

    public void update(String userAlias, String sqlUpdate) {
        String[] user = this.getUserByAlias(userAlias);
        Assertions.assertNotNull(user, "Пользователь с псевдонимом '" + userAlias + "' не найден!");
        this.update(user[1], user[2], sqlUpdate);
    }

    private void assertQueryTimeout(String exceptionMessage) {
        if (exceptionMessage != null && exceptionMessage.replace("\n", "").equals("ORA-01013: user requested cancel of current operation")) {
            throw new Database.SQLTimeoutError("Превышено максимальное время ожидания выполнения SQL запроса!");
        }
    }

    public class SQLTimeoutError extends Error {
        SQLTimeoutError(String message) {
            super(message);
        }
    }

}
