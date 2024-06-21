package de.goldendeveloper.logger.discord.commands;

import de.goldendeveloper.logger.Main;
import de.goldendeveloper.logger.MysqlConnection;
import io.github.coho04.dcbcore.DCBot;
import io.github.coho04.dcbcore.interfaces.CommandInterface;
import io.github.coho04.mysql.entities.RowBuilder;
import io.github.coho04.mysql.entities.Table;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class Settings implements CommandInterface {

    public static String cmdSettings = "settings";
    public static String cmdSubSettingsChannel = "channel";
    public static String cmdSubSettingsChannelOptionChannel = "channel-id";

    @Override
    public CommandData commandData() {
        return Commands.slash(cmdSettings, "Stellt den Bot für diesen Discord Server ein!").addSubcommands(
                new SubcommandData(cmdSubSettingsChannel, "Legt den Channel für die Nachrichten fest!")
                        .addOption(OptionType.CHANNEL, cmdSubSettingsChannelOptionChannel, "Legt den Channel für die Nachrichten fest!", true)
        );
    }

    @Override
    public void runSlashCommand(SlashCommandInteractionEvent e, DCBot dcBot) {
        if (e.getSubcommandName() != null && e.getSubcommandName().equalsIgnoreCase(cmdSubSettingsChannel)) {
            OptionMapping option = e.getOption(cmdSubSettingsChannelOptionChannel);
            if (option != null && Main.getMysqlConnection().getMysql().existsDatabase(MysqlConnection.dbName)) {
                if (Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.dbName).existsTable(MysqlConnection.tableName)) {
                    Table table = Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.dbName).getTable(MysqlConnection.tableName);
                    if (table.hasColumn(MysqlConnection.clmServerID) && e.getGuild() != null) {
                        TextChannel channel = option.getAsChannel().asTextChannel();
                        if (table.getColumn(MysqlConnection.clmServerID).getAll().getAsString().contains(e.getGuild().getId())) {
                            String id = table.getRow(table.getColumn(MysqlConnection.clmServerID), e.getGuild().getId()).getData().get("id").getAsString();
                            table.getRow(table.getColumn(MysqlConnection.clmServerID), e.getGuild().getId()).set(table.getColumn(MysqlConnection.clmChannelID), id);
                            e.getInteraction().reply("Der neue Log Channel ist nun " + channel.getAsMention() + "!").queue();
                        } else {
                            table.insert(new RowBuilder().with(table.getColumn(MysqlConnection.clmChannelID), channel.getId()).with(table.getColumn(MysqlConnection.clmServerID), e.getGuild().getId()).build());
                            e.getInteraction().reply("Der Log Channel ist nun " + channel.getAsMention() + "!").queue();
                        }
                    }
                }
            }
        }
    }
}
