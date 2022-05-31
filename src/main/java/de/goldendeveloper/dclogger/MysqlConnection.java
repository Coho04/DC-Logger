package de.goldendeveloper.dclogger;

import de.goldendeveloper.mysql.MYSQL;
import de.goldendeveloper.mysql.entities.Database;
import de.goldendeveloper.mysql.entities.MysqlTypes;
import de.goldendeveloper.mysql.entities.Table;

public class MysqlConnection {

    private final MYSQL mysql;

    public static String dbName = "GDLogger";
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
            table.addColumn(clmServerID, MysqlTypes.VARCHAR, 50);
        }
        if (!table.hasColumn(clmChannelID)) {
            table.addColumn(clmChannelID, MysqlTypes.VARCHAR, 50);
        }
        System.out.println("MYSQL Finished");
    }

    public MYSQL getMysql() {
        return mysql;
    }
}
