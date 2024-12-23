package io.github.coho04.logger;


import io.github.coho04.mysql.MYSQL;
import io.github.coho04.mysql.entities.Database;
import io.github.coho04.mysql.entities.Table;

public class MysqlConnection {

    private final MYSQL mysql;

    public static String tableName = "Discord";
    public static String clmServerID = "ServerID";
    public static String clmChannelID = "ChannelID";

    public MysqlConnection(String hostname, String username, String password, int port) {
        mysql = new MYSQL(hostname, username, password, port);
        if (!mysql.existsDatabase(Main.getCustomConfig().getMysqlDatabase())) {
            mysql.createDatabase(Main.getCustomConfig().getMysqlDatabase());
        }
        Database db = mysql.getDatabase(Main.getCustomConfig().getMysqlDatabase());
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
