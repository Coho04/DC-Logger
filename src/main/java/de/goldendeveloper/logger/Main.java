package de.goldendeveloper.logger;

import de.goldendeveloper.logger.discord.CustomEvents;
import de.goldendeveloper.logger.discord.commands.Settings;
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
