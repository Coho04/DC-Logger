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
import java.io.IOException;

public class Config {

    private String DiscordToken = "";
    private String MysqlHostname = "127.0.0.1";
    private String MysqlUsername = "";
    private String MysqlPassword = "";
    private int MysqlPort = 3306;

    public Config() {
        File file = new File("src/main/resources/login.xml");
        if (file.exists() && !file.isDirectory()) {
            readXML(file);
        }
        file = new File("src/main/resources/Login.xml");
        if (file.exists() && !file.isDirectory()) {
            readXML(file);
        }
    }

    private void readXML(File file) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList list = doc.getElementsByTagName("MYSQL");
            for (int i = 0; i < list.getLength(); i++) {
                if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) list.item(i);
                    String hostname = element.getElementsByTagName("Hostname").item(0).getTextContent();
                    String username = element.getElementsByTagName("Username").item(0).getTextContent();
                    String password = element.getElementsByTagName("Password").item(0).getTextContent();
                    String port = element.getElementsByTagName("Port").item(0).getTextContent();
                    String token = doc.getElementsByTagName("DiscordToken").item(0).getTextContent();
                    if (!hostname.isEmpty() || !hostname.isBlank()) {
                        MysqlHostname = hostname;
                    }
                    if (!username.isEmpty() || !username.isBlank()) {
                        MysqlUsername = username;
                    }
                    if (!password.isEmpty() || !password.isBlank()) {
                        MysqlPassword = password;
                    }
                    if (!port.isEmpty() || !port.isBlank()) {
                        MysqlPort = Integer.parseInt(port);
                    }
                    if (!token.isEmpty() || !token.isBlank()) {
                        DiscordToken = token;
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public void setDiscordToken(String discordToken) {
        DiscordToken = discordToken;
    }

    public void setMysqlHostname(String mysqlHostname) {
        MysqlHostname = mysqlHostname;
    }

    public void setMysqlPassword(String mysqlPassword) {
        MysqlPassword = mysqlPassword;
    }

    public void setMysqlPort(int mysqlPort) {
        MysqlPort = mysqlPort;
    }

    public void setMysqlUsername(String mysqlUsername) {
        MysqlUsername = mysqlUsername;
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
}
