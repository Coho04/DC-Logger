package de.goldendeveloper.dclogger;

import de.goldendeveloper.dclogger.discord.Discord;
import de.goldendeveloper.mysql.MYSQL;
import de.goldendeveloper.mysql.entities.Database;
import de.goldendeveloper.mysql.entities.MysqlTypes;
import de.goldendeveloper.mysql.entities.Table;

public class Main {

    private static Discord discord;
    private static MYSQL mysql;

    public static String dbName = "GDLogger";
    public static String tableName = "Discord";
    public static String clmServerID = "ServerID";
    public static String clmChannelID = "ChannelID";

    public static void main(String[] args) {
        discord = new Discord(ID.DiscordToken);
        connectMysql();
    }

    private static void connectMysql() {
        mysql = new MYSQL(ID.MysqlHostname, ID.MysqlUsername, ID.MysqlPassword, ID.MysqlPort);
        if (!getMysql().existsDatabase(dbName)) {
            getMysql().createDatabase(dbName);
        }
        Database db = getMysql().getDatabase(dbName);
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

    public static MYSQL getMysql() {
        return mysql;
    }

    public static Discord getDiscord() {
        return discord;
    }

    public static void sendErrorMessage(String Error) {

    }
}
