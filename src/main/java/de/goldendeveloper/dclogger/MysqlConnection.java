package de.goldendeveloper.dclogger;

import de.goldendeveloper.mysql.MYSQL;
import de.goldendeveloper.mysql.entities.Database;
import de.goldendeveloper.mysql.entities.Table;

public class MysqlConnection {

    private final MYSQL mysql;

    public static String dbName = "GD-Logger";
    public static String tableName = "Discord";
    public static String clmServerID = "ServerID";
    public static String clmChannelID = "ChannelID";

    public MysqlConnection(String hostname, String username, String password, int port) {
        mysql = new MYSQL(hostname, username, password, port);
        if (!mysql.existsDatabase(dbName)) {
            mysql.createDatabase(dbName);
        }
        Database db = mysql.getDatabase(dbName);
        if (!db.existsTable(tableName)) {
            db.createTable(tableName);
        }
        Table table = db.getTable(tableName);
        if (!table.hasColumn(clmServerID)) {
            table.addColumn(clmServerID);
        }
        if (!table.hasColumn(clmChannelID)) {
            table.addColumn(clmChannelID);
        }
        System.out.println("MYSQL Finished");
    }

    public MYSQL getMysql() {
        return mysql;
    }
}
