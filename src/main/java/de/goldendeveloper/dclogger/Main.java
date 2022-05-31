package de.goldendeveloper.dclogger;

import de.goldendeveloper.dclogger.discord.Discord;

public class Main {

    private static Discord discord;
    private static Config config;
    private static MysqlConnection mysqlConnection;

    public static void main(String[] args) {
        config = new Config();
        discord = new Discord(config.getDiscordToken());
        mysqlConnection = new MysqlConnection(config.getMysqlHostname(), config.getMysqlUsername(), config.getMysqlPassword(), config.getMysqlPort());
    }

    public static MysqlConnection getMysqlConnection() {
        return mysqlConnection;
    }

    public static Discord getDiscord() {
        return discord;
    }

    public static Config getConfig() {
        return config;
    }
}
