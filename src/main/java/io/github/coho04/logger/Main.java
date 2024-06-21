package io.github.coho04.logger;

import io.github.coho04.logger.discord.CustomEvents;
import io.github.coho04.logger.discord.commands.Settings;
import io.github.coho04.dcbcore.DCBotBuilder;


public class Main {

    private static MysqlConnection mysqlConnection;

    public static void main(String[] args) {
        CustomConfig config = new CustomConfig();
        DCBotBuilder dcBotBuilder = new DCBotBuilder(args, true);
        dcBotBuilder.registerEvents(new CustomEvents());
        dcBotBuilder.registerCommands(new Settings());
        dcBotBuilder.build();
        mysqlConnection = new MysqlConnection(config.getMysqlHostname(), config.getMysqlUsername(), config.getMysqlPassword(), config.getMysqlPort());
    }

    public static MysqlConnection getMysqlConnection() {
        return mysqlConnection;
    }
}
