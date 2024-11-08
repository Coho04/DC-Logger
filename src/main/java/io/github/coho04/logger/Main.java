package io.github.coho04.logger;

import io.github.coho04.logger.discord.CustomEvents;
import io.github.coho04.logger.discord.commands.Settings;
import io.github.coho04.dcbcore.DCBotBuilder;


public class Main {

    private static MysqlConnection mysqlConnection;
    private static CustomConfig customConfig;

    public static void main(String[] args) {
        customConfig = new CustomConfig();
        DCBotBuilder dcBotBuilder = new DCBotBuilder(args, true);
        dcBotBuilder.registerEvents(new CustomEvents());
        dcBotBuilder.registerCommands(new Settings());
        dcBotBuilder.build();
        mysqlConnection = new MysqlConnection(customConfig.getMysqlHostname(), customConfig.getMysqlUsername(), customConfig.getMysqlPassword(), customConfig.getMysqlPort());
        System.out.println("Java application started successfully");
    }

    public static MysqlConnection getMysqlConnection() {
        return mysqlConnection;
    }

    public static CustomConfig getCustomConfig() {
        return customConfig;
    }
}
