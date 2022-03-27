package de.goldendeveloper.dclogger;

import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import de.goldendeveloper.dclogger.discord.Discord;
import de.goldendeveloper.mysql.MYSQL;
import de.goldendeveloper.mysql.entities.Database;
import de.goldendeveloper.mysql.entities.MysqlTypes;
import de.goldendeveloper.mysql.entities.Table;

public class Main {

    private static Discord discord;
    private static MYSQL mysql;
    private static Config config;

    public static String dbName = "GDLogger";
    public static String tableName = "Discord";
    public static String clmServerID = "ServerID";
    public static String clmChannelID = "ChannelID";

    public static void main(String[] args) {
        config = new Config();
        discord = new Discord(config.getDiscordToken());
        connectMysql();
    }

    private static void connectMysql() {
        mysql = new MYSQL(config.getMysqlHostname(), config.getMysqlUsername(), config.getMysqlPassword(), config.getMysqlPort());
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
        WebhookClientBuilder builder = new WebhookClientBuilder("https://discord.com/api/webhooks/957737386165563482/9vo30g6HVlIj6C24r6Sjs-X-bADlKFbN1xwmaihn1PYzZLBcwFTt5QeLMG80JISu1vjM");
        WebhookEmbedBuilder embed = new WebhookEmbedBuilder();
        embed.setAuthor(new WebhookEmbed.EmbedAuthor("DC-Logger", Main.getDiscord().getBot().getSelfUser().getAvatarUrl(), "https://Golden-Developer.de"));
        embed.addField(new WebhookEmbed.EmbedField(false, "[ERROR]", Error));
        embed.setColor(0xFF0000);
        embed.setFooter(new WebhookEmbed.EmbedFooter("@Golden-Developer", Main.getDiscord().getBot().getSelfUser().getAvatarUrl()));
        builder.build().send(embed.build());
    }

    public static Config getConfig() {
        return config;
    }
}
