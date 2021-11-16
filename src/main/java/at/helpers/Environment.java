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
import java.util.List;
@Getter
public class Environment {
    private Document document;
    private String mainUrl;
    private String[][] apps;
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
        this.setEnvironment("intranet");
        this.setLigaEndpoints();
//        this.setApps();
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

//    private void setApps() {
//        NodeList nodeList = this.environment.getElementsByTagName("applications");
//        if (nodeList != null && nodeList.getLength() >= 1) {
//            NodeList apps = ((Element)nodeList.item(0)).getElementsByTagName("application");
//            this.apps = new String[apps.getLength()][2];
//
//            for(int i = 0; i < apps.getLength(); ++i) {
//                Node app = apps.item(i);
//                this.apps[i][0] = this.getTagAttribute(app, "alias");
//                this.apps[i][1] = app.getTextContent();
//            }
//        } else {
//            System.out.println("Tag 'applications' is undefined");
//            Assertions.fail("Список приложений не определен!");
//        }
//    }

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
