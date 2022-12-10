package de.goldendeveloper.dclogger.discord;

import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import de.goldendeveloper.dclogger.Main;
import de.goldendeveloper.dclogger.MysqlConnection;
import de.goldendeveloper.mysql.entities.RowBuilder;
import de.goldendeveloper.mysql.entities.SearchResult;
import de.goldendeveloper.mysql.entities.Table;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateAfkChannelEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBannerEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateMaxMembersEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;

public class Events extends ListenerAdapter {

    @Override
    public void onShutdown(@NotNull ShutdownEvent e) {
        if (Main.getDeployment()) {
            WebhookEmbedBuilder embed = new WebhookEmbedBuilder();
            embed.setAuthor(new WebhookEmbed.EmbedAuthor(Main.getDiscord().getBot().getSelfUser().getName(), Main.getDiscord().getBot().getSelfUser().getAvatarUrl(), "https://Golden-Developer.de"));
            embed.addField(new WebhookEmbed.EmbedField(false, "[Status]", "Offline"));
            embed.addField(new WebhookEmbed.EmbedField(false, "Gestoppt als", Main.getDiscord().getBot().getSelfUser().getName()));
            embed.addField(new WebhookEmbed.EmbedField(false, "Server", Integer.toString(Main.getDiscord().getBot().getGuilds().size())));
            embed.addField(new WebhookEmbed.EmbedField(false, "Status", "\uD83D\uDD34 Offline"));
            embed.addField(new WebhookEmbed.EmbedField(false, "Version", Main.getDiscord().getProjektVersion()));
            embed.setFooter(new WebhookEmbed.EmbedFooter("@Golden-Developer", Main.getDiscord().getBot().getSelfUser().getAvatarUrl()));
            embed.setTimestamp(new Date().toInstant());
            embed.setColor(0xFF0000);
            new WebhookClientBuilder(Main.getConfig().getDiscordWebhook()).build().send(embed.build()).thenRun(() -> System.exit(0));
        }
    }

    @Override
    public void onGuildJoin(GuildJoinEvent e) {
        e.getJDA().getPresence().setActivity(Activity.playing("/help | " + e.getJDA().getGuilds().size() + " Servern"));
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent e) {
        e.getJDA().getPresence().setActivity(Activity.playing("/help | " + e.getJDA().getGuilds().size() + " Servern"));
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.isFromGuild()) {
            if (e.getMember() != null) {
                if (e.getMember().getUser() != e.getJDA().getSelfUser()) {
                    onEvent(e.getGuild(), "Message", "[Channel]: " + e.getChannel().asTextChannel().getAsMention() + " \n Message: " + e.getMember().getUser().getAsMention() + " >> " + e.getMessage().getContentRaw(), e.getChannel().asTextChannel());
                }
            }
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent e) {
        if (e.isFromGuild()) {
            if (e.getUser() != null) {
                onEvent(e.getGuild(), "Reaction", "Der User " + e.getUser().getName() + " hat mit " + e.getReaction().getEmoji().getAsReactionCode() + " auf die Nachricht " + e.getChannel().asTextChannel().getHistory().getMessageById(e.getMessageId()) + " reagiert!", e.getChannel().asTextChannel());
            }
        }
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent e) {
        if (e.isFromGuild()) {
            if (e.getUser() != null) {
                onEvent(e.getGuild(), "Reaction", "Der User " + e.getUser().getName() + " hat seine Reaction " + e.getReaction().getEmoji().getAsReactionCode() + " auf die Nachricht " + e.getChannel().asTextChannel().getHistory().getMessageById(e.getMessageId()) + " entfernt!", e.getChannel().asTextChannel());
            }
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        User _Coho04_ = e.getJDA().getUserById("513306244371447828");
        User zRazzer = e.getJDA().getUserById("428811057700536331");
        if (e.isFromGuild()) {
            if (e.getName().equalsIgnoreCase(Discord.cmdSettings)) {
                if (e.getSubcommandName() != null) {
                    if (e.getSubcommandName().equalsIgnoreCase(Discord.cmdSubSettingsChannel)) {
                        OptionMapping option = e.getOption(Discord.cmdSubSettingsChannelOptionChannel);
                        if (option != null) {
                            TextChannel channel = option.getAsChannel().asTextChannel();
                            if (channel != null) {
                                if (Main.getMysqlConnection().getMysql().existsDatabase(MysqlConnection.dbName)) {
                                    if (Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.dbName).existsTable(MysqlConnection.tableName)) {
                                        Table table = Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.dbName).getTable(MysqlConnection.tableName);
                                        if (table.hasColumn(MysqlConnection.clmServerID)) {
                                            if (e.getGuild() != null) {
                                                if (table.getColumn(MysqlConnection.clmServerID).getAll().getAsString().contains(e.getGuild().getId())) {
                                                    HashMap<String, SearchResult> row = table.getRow(table.getColumn(MysqlConnection.clmServerID), e.getGuild().getId()).get();
                                                    table.getRow(table.getColumn(MysqlConnection.clmServerID), e.getGuild().getId()).set(table.getColumn(MysqlConnection.clmChannelID), row.get("id").getAsString());
                                                    e.getInteraction().reply("Der neue Log Channel ist nun " + channel.getAsMention() + "!").queue();
                                                } else {
                                                    table.insert(new RowBuilder().with(table.getColumn(MysqlConnection.clmChannelID), channel.getId()).with(table.getColumn(MysqlConnection.clmServerID), e.getGuild().getId()).build());
                                                    e.getInteraction().reply("Der Log Channel ist nun " + channel.getAsMention() + "!").queue();
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                e.getInteraction().reply("Der TextChannel konnte nicht gefunden werden!").queue();
                            }
                        }
                    }
                }
            } else if (e.getName().equalsIgnoreCase(Discord.cmdHelp)) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("**Help Commands**");
                embed.setColor(Color.MAGENTA);
                embed.setFooter("@Golden-Developer", e.getJDA().getSelfUser().getAvatarUrl());
                for (Command cm : Main.getDiscord().getBot().retrieveCommands().complete()) {
                    embed.addField("/" + cm.getName(), cm.getDescription(), true);
                }
                e.getInteraction().replyEmbeds(embed.build()).addActionRow(
                        Button.link("https://wiki.golden-developer.de", "Online Übersicht"),
                        Button.link("https://support.coho04.de", "Support Anfragen")
                ).queue();
            } else if (e.getName().equalsIgnoreCase(Discord.cmdShutdown)) {
                if (e.getUser() == zRazzer || e.getUser() == _Coho04_) {
                    e.getInteraction().reply("Der Bot wird nun heruntergefahren").queue();
                    e.getJDA().shutdown();
                } else {
                    e.getInteraction().reply("Dazu hast du keine Rechte du musst für diesen Befehl der Bot inhaber sein!").queue();
                }
            } else if (e.getName().equalsIgnoreCase(Discord.cmdRestart)) {
                if (e.getUser() == zRazzer || e.getUser() == _Coho04_) {
                    try {
                        e.getInteraction().reply("Der Discord Bot [" + Main.getDiscord().getBot().getSelfUser().getName() + "] wird nun neugestartet!").queue();
                        Process p = Runtime.getRuntime().exec("screen -AmdS " + Main.getDiscord().getProjektName() + " java -Xms1096M -Xmx1096M -jar " + Main.getDiscord().getProjektName() + "-" + Main.getDiscord().getProjektVersion() + ".jar restart");
                        p.waitFor();
                        e.getJDA().shutdown();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    e.getInteraction().reply("Dazu hast du keine Rechte du musst für diesen Befehl der Bot inhaber sein!").queue();
                }
            } else {
                onEvent(e.getGuild(), "Command", "Der User " + e.getUser().getName() + " hat den Command " + e.getName() + e.getSubcommandName(), e.getChannel().asTextChannel());
            }
        }
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent e) {
        onEvent(e.getGuild(), "Server verlassen", "Der User " + e.getUser().getName() + " hat den Server verlassen!", null);
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        onEvent(e.getGuild(), "Server betreten", "Der User " + e.getUser().getName() + " ist dem Server beigetreten!", null);
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent e) {
        onEvent(e.getGuild(), "Role hinzugefügt", "Dem User " + e.getUser().getName() + " wurde die Rolle <role> gegeben!", null);
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent e) {
        onEvent(e.getGuild(), "Rolle entfernt", "Dem User " + e.getUser().getName() + " wurde die Rolle <role> entfernt!", null);
    }

    @Override
    public void onGuildUpdateAfkChannel(GuildUpdateAfkChannelEvent e) {
        onEvent(e.getGuild(), "AFK Channel Update", "Der Neue AFK Channel ist nun " + e.getNewAfkChannel() + " Alte: " + e.getOldAfkChannel(), null);
    }

    @Override
    public void onGuildUpdateBanner(GuildUpdateBannerEvent e) {
        onEvent(e.getGuild(), "Banner Update", "Der Alte Banner wurde auf einen Neuen  geändert!", null);
    }

    @Override
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent e) {
        onEvent(e.getGuild(), "Nickname Update", "Der User " + e.getOldNickname() + " heißt nun " + e.getNewNickname() + "!", null);
    }

    @Override
    public void onGuildMemberUpdateBoostTime(GuildMemberUpdateBoostTimeEvent e) {
        onEvent(e.getGuild(), "Time Boosted Update", "" + e.getOldTimeBoosted() + " Neu " + e.getNewTimeBoosted(), null);
    }

    @Override
    public void onGuildUpdateMaxMembers(GuildUpdateMaxMembersEvent e) {
        onEvent(e.getGuild(), "Update Maximale User", "Die Anzahl von Maximalen Usern hat sich geändert! Alt: " + e.getOldMaxMembers() + " Neu: " + e.getNewMaxMembers() + "!", null);
    }

    @Override
    public void onGuildUnban(GuildUnbanEvent e) {
        onEvent(e.getGuild(), "Server Unban User", "Der User " + e.getUser().getName() + " hat oder wurde entbannt!", null);
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent e) {
        if (e.getOldValue() != null) {

        }
        onEvent(e.getGuild(), "User Channel Join", "Der User " + e.getMember().getUser().getName() + " ist dem Channel " + e.getChannelJoined().getAsMention() + " beigetreten!", null);
        onEvent(e.getGuild(), "User Channel Leave", "Der User " + e.getMember().getUser().getName() + " hat den Channel " + e.getChannelLeft().getAsMention() + " verlassen!", null);
        onEvent(e.getGuild(), "User Channel Move", "Der User " + e.getMember().getUser().getName() + " hat den Channel " + e.getChannelLeft().getAsMention() + " verlassen und ist dem Channel " + e.getChannelJoined() + " beigetreten!", null);
    }

    private MessageEmbed onEmbed(Guild guild, String Event, String Value) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("**Logger**", "https://Golden-Developer.de");
        builder.setThumbnail(guild.getBannerUrl());
        builder.setAuthor("@Golden-Developer", "https://Golden-Developer.de");
        builder.setTimestamp(LocalTime.now());
        builder.setColor(Color.gray);
        builder.addField("**" + Event + "**", Value, true);
        return builder.build();
    }

    private void onEvent(Guild guild, String Event, String Value, @Nullable TextChannel ch) {
        if (Main.getMysqlConnection().getMysql().existsDatabase(MysqlConnection.dbName)) {
            if (Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.dbName).existsTable(MysqlConnection.tableName)) {
                Table table = Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.dbName).getTable(MysqlConnection.tableName);
                if (table.hasColumn(MysqlConnection.clmServerID)) {
                    if (table.getColumn(MysqlConnection.clmServerID).getAll().getAsString().contains(guild.getId())) {
                        HashMap<String, SearchResult> row = table.getRow(table.getColumn(MysqlConnection.clmServerID), guild.getId()).get();
                        if (!row.get(MysqlConnection.clmChannelID).toString().isEmpty()) {
                            TextChannel channel = guild.getTextChannelById(row.get(MysqlConnection.clmChannelID).getAsString());
                            if (channel != null) {
                                channel.sendMessageEmbeds(onEmbed(guild, Event, Value)).queue();
                            }
                        } else {
                            if (ch != null) {
                                ch.sendMessage("Es wurde kein Log Channel eingerichtet").queue();
                            }
                        }
                    }
                } else {
                    Main.getDiscord().sendErrorMessage("Can`t find Column ServerID");
                }
            } else {
                Main.getDiscord().sendErrorMessage("Table don´t exists");
            }
        } else {
            Main.getDiscord().sendErrorMessage("Database don´t exists");
        }
    }
}
