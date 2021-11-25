package at.helpers;

import at.model.User;
import lombok.Getter;

import org.junit.jupiter.api.Assertions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Environment {
    private String name;
    private Document document;
    private String mainUrl;
    private String[][] apps;
    public Map<String, Map<String, String>> databases = new HashMap();
    private User[] users;
    private Element environment;

    public Environment(File document){
        setDocument(document);
        setAllData();
    }

    private void setDocument(File xml) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            this.document = documentBuilder.parse(xml);
        } catch (Exception var4) {
            System.out.println("Parse Environment XML failed");
            var4.printStackTrace();
            Assertions.fail("Не удалось распарсить XML документ с параметрами тестового окружения!");
        }

    }
    private void setAllData() {
        this.setEnvironment("test-intranet");
        this.setLigaEndpoints();
        this.setDatabaseProperties();
        this.setUsers();
    }
    private void setEnvironment(String environment) {
        Element root = this.document.getDocumentElement();
        NodeList nodeList = root.getElementsByTagName("environment");
        if (nodeList != null && nodeList.getLength() >= 1) {
            for(int i = 0; i < nodeList.getLength(); ++i) {
                Element element = (Element)nodeList.item(i);
                String name = element.getAttribute("name");
                if (name != null && name.equalsIgnoreCase(environment)) {
                    this.environment = element;
                    this.name=name;
                }
            }

            if (this.environment == null) {
                System.out.println("Environment '" + environment + "' is not found");
                Assertions.fail("Тестовое окружение '" + environment + "' не найдено!");
            }

        } else {
            System.out.println("Tag 'environment' is undefined");
            Assertions.fail("Тестовое окружение не определено!");
        }
    }

    private void setDatabaseProperties() {
        NodeList nodeList = this.environment.getElementsByTagName("databases");
        if (nodeList != null && nodeList.getLength() >= 1) {
            NodeList databases = ((Element)nodeList.item(0)).getElementsByTagName("database");

            for(int i = 0; i < databases.getLength(); ++i) {
                Node database = databases.item(i);
                Map<String, String> databaseParameters = new HashMap();
                databaseParameters.put("serviceName", this.getTagValue(database, "serviceName"));
                databaseParameters.put("protocol", this.getTagValue(database, "protocol"));
                databaseParameters.put("host", this.getTagValue(database, "host"));
                databaseParameters.put("user", this.getTagValue(database, "user"));
                databaseParameters.put("userPass", this.getTagValue(database, "userPass"));
                databaseParameters.put("port", this.getTagValue(database, "port"));
                databaseParameters.put("databaseName", this.getTagValue(database, "databaseName"));
                databaseParameters.put("databasePass", this.getTagValue(database, "databasePass"));

                this.databases.put(this.getTagAttribute(database, "alias"), databaseParameters);
            }

        } else {
            System.out.println("Tag 'databases' is undefined");
            Assertions.fail("Параметры баз данных не определены!");
        }
    }

    private void setUsers() {
        NodeList nodeList = this.environment.getElementsByTagName("users");
        if (nodeList != null && nodeList.getLength() >= 1) {
            NodeList users = ((Element)nodeList.item(0)).getElementsByTagName("user");
            this.users = new User[nodeList.getLength()];

            for(int i = 0; i < users.getLength(); ++i) {
                Node user = users.item(i);
                this.users[i]=new User(this.getTagAttribute(user, "alias"),
                        this.getTagValue(user, "name"),
                        this.getTagValue(user, "password"),
                        this.getTagValue(user, "role"));
            }
        } else {
            System.out.println("Tag 'users' is undefined");
            Assertions.fail("Список пользователей не определен!");
        }
    }

    private String getTagAttribute(Node node, String attribute) {
        return ((Element)node).getAttribute(attribute);
    }

    private void setLigaEndpoints() {
        NodeList nodeList = this.environment.getElementsByTagName("ligaEndpoints");
        if (nodeList != null && nodeList.getLength() >= 1) {
            NodeList urls = ((Element)nodeList.item(0)).getElementsByTagName("ligaEndpoint");
            this.mainUrl=urls.item(0).getTextContent();
        } else {
            System.out.println("Tag 'ligaEndpoints' is undefined");
            Assertions.fail("Список ligaEndpoints не определен!");
        }
    }

    private String getTagValue(Node node, String tagName) {
        NodeList nodeList = ((Element)node).getElementsByTagName(tagName);
        if (nodeList != null && nodeList.getLength() >= 1) {
            return this.getTagValue(nodeList.item(0));
        } else {
            System.out.println("Tag by name '" + tagName + "' is not found");
            Assertions.fail("Тег '" + tagName + "' не найден!");
            return null;
        }
    }

    private String getTagValue(Node node) {
        return node.getFirstChild() != null ? node.getFirstChild().getNodeValue() : null;
    }

    public Environment(String mainUrl, String[][] apps,User[] users){
        this.mainUrl =mainUrl;
        this.apps =apps;
        this.users =users;
    }
    private void getEnvironment(String environment) {
        Element root = this.document.getDocumentElement();
        NodeList nodeList = root.getElementsByTagName("environment");
        if (nodeList != null && nodeList.getLength() >= 1) {
            for(int i = 0; i < nodeList.getLength(); ++i) {
                Element element = (Element)nodeList.item(i);
                String name = element.getAttribute("name");
                if (name != null && name.equalsIgnoreCase(environment)) {
                    this.environment = element;
                }
            }

            if (this.environment == null) {
                System.out.println("Environment '" + environment + "' is not found");
                Assertions.fail("Тестовое окружение '" + environment + "' не найдено!");
            }

        } else {
            System.out.println("Tag 'environment' is undefined");
            Assertions.fail("Тестовое окружение не определено!");
        }
    }

    public String getAppUrl(String application) {
        for (String[] app : apps) {
            if (app[0].equals(application)) {
                return app[1];
            }
        }
        return "";
    }
}
