package de.goldendeveloper.dclogger;

import de.goldendeveloper.dclogger.discord.Discord;
import de.goldendeveloper.mysql.MYSQL;
import de.goldendeveloper.mysql.entities.Database;
import de.goldendeveloper.mysql.entities.MysqlTypes;
import de.goldendeveloper.mysql.entities.Table;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

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
       TextChannel channel = getDiscord().getBot().getTextChannelById(ID.DiscordErrorChannel);
       if (channel != null) {
           MessageEmbed embed = new EmbedBuilder()
                   .addField("[ERROR]",Error , true)
                   .setColor(new Color(250, 0, 0))
                   .build();
           channel.sendMessageEmbeds(embed).queue();
       }
    }
}
