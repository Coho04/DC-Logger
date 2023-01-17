package de.goldendeveloper.dclogger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Config {

    private String DiscordToken;
    private String DiscordWebhook;
    private String MysqlHostname;
    private String MysqlUsername;
    private String MysqlPassword;
    private int MysqlPort;

    private String ServerHostname;
    private int ServerPort;

    public Config() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream local = classloader.getResourceAsStream("Login.xml");
        try {
            Path path = Files.createTempFile("Login", ".xml");
            if (local != null && Files.exists(path)) {
                readXML(local);
            } else {
                File file = new File("/home/Golden-Developer/JavaBots/" + getProjektName() + "/config/Login.xml");
                InputStream targetStream = new FileInputStream(file);
                readXML(targetStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readXML(InputStream inputStream) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inputStream);
            doc.getDocumentElement().normalize();
            NodeList list = doc.getElementsByTagName("MYSQL");
            for (int i = 0; i < list.getLength(); i++) {
                if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) list.item(i);
                    String hostname = element.getElementsByTagName("Hostname").item(0).getTextContent();
                    String username = element.getElementsByTagName("Username").item(0).getTextContent();
                    String password = element.getElementsByTagName("Password").item(0).getTextContent();
                    String port = element.getElementsByTagName("Port").item(0).getTextContent();
                    if (!hostname.isEmpty() || !hostname.isBlank()) {
                        this.MysqlHostname = hostname;
                    }
                    if (!username.isEmpty() || !username.isBlank()) {
                        this.MysqlUsername = username;
                    }
                    if (!password.isEmpty() || !password.isBlank()) {
                        this.MysqlPassword = password;
                    }
                    if (!port.isEmpty() || !port.isBlank()) {
                        this.MysqlPort = Integer.parseInt(port);
                    }
                }
            }
            list = doc.getElementsByTagName("Discord");
            for (int i = 0; i < list.getLength(); i++) {
                if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) list.item(i);
                    String webhook = element.getElementsByTagName("Webhook").item(0).getTextContent();
                    String token = doc.getElementsByTagName("Token").item(0).getTextContent();
                    if (!webhook.isEmpty() || !webhook.isBlank()) {
                        this.DiscordWebhook = webhook;
                    }
                    if (!token.isEmpty() || !token.isBlank()) {
                        this.DiscordToken = token;
                    }
                }
            }

            // Server
            String hostname = doc.getElementsByTagName("Hostname").item(1).getTextContent();
            String port = doc.getElementsByTagName("Port").item(1).getTextContent();
            if (!hostname.isEmpty() || !hostname.isBlank()) {
                this.ServerHostname = hostname;
            }
            if (!port.isEmpty() || !port.isBlank()) {
                this.ServerPort = Integer.parseInt(port);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public String getDiscordWebhook() {
        return DiscordWebhook;
    }

    public int getMysqlPort() {
        return MysqlPort;
    }

    public String getDiscordToken() {
        return DiscordToken;
    }

    public String getMysqlHostname() {
        return MysqlHostname;
    }

    public String getMysqlPassword() {
        return MysqlPassword;
    }

    public String getMysqlUsername() {
        return MysqlUsername;
    }


    public String getProjektVersion() {
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("project.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties.getProperty("version");
    }
    public String getProjektName() {
        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream("project.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties.getProperty("name");
    }

    public int getServerPort() {
        return ServerPort;
    }

    public String getServerHostname() {
        return ServerHostname;
    }
}
