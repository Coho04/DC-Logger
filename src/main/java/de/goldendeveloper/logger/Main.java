package de.goldendeveloper.logger;

import de.goldendeveloper.dcbcore.DCBotBuilder;
import de.goldendeveloper.dcbcore.interfaces.CommandInterface;
import de.goldendeveloper.logger.discord.CustomEvents;
import de.goldendeveloper.logger.discord.commands.Settings;

import java.util.LinkedList;

public class Main {

    private static MysqlConnection mysqlConnection;

    public static void main(String[] args) {
        CustomConfig config = new CustomConfig();
        DCBotBuilder dcBotBuilder = new DCBotBuilder(args, true);
        dcBotBuilder.registerEvents(new CustomEvents());
        dcBotBuilder.registerCommands(registerCommands());
        dcBotBuilder.build();
        mysqlConnection = new MysqlConnection(config.getMysqlHostname(), config.getMysqlUsername(), config.getMysqlPassword(), config.getMysqlPort());
    }

    public static LinkedList<CommandInterface> registerCommands() {
        LinkedList<CommandInterface> commands = new LinkedList<>();
        commands.add(new Settings());
        return commands;
    }

    public static MysqlConnection getMysqlConnection() {
        return mysqlConnection;
    }
}
