package de.goldendeveloper.dclogger.discord;

import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import de.goldendeveloper.dclogger.Main;
import de.goldendeveloper.mysql.entities.Row;
import de.goldendeveloper.mysql.entities.Table;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ShutdownEvent;
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
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import javax.annotation.Nullable;
import java.awt.Color;
import java.time.LocalTime;
import java.util.HashMap;

public class Events extends ListenerAdapter {

    @Override
    public void onShutdown(ShutdownEvent e) {
        WebhookClientBuilder builder = new WebhookClientBuilder("https://discord.com/api/webhooks/957737386165563482/9vo30g6HVlIj6C24r6Sjs-X-bADlKFbN1xwmaihn1PYzZLBcwFTt5QeLMG80JISu1vjM");
        WebhookEmbedBuilder embed = new WebhookEmbedBuilder();
        embed.setAuthor(new WebhookEmbed.EmbedAuthor("DC-Logger", Main.getDiscord().getBot().getSelfUser().getAvatarUrl(), "https://Golden-Developer.de"));
        embed.addField(new WebhookEmbed.EmbedField(false, "[Status]", "OFFLINE"));
        embed.setColor(0xFF0000);
        embed.setFooter(new WebhookEmbed.EmbedFooter("@Golden-Developer",  Main.getDiscord().getBot().getSelfUser().getAvatarUrl()));
        builder.build().send(embed.build());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.isFromGuild()) {
            if (e.getMember() != null) {
                if (e.getMember().getUser() != e.getJDA().getSelfUser()) {
                    onEvent(e.getGuild(), "Message", "[Channel]: " + e.getTextChannel().getAsMention() + " \n Message: " + e.getMember().getUser().getAsMention() + " >> " + e.getMessage().getContentRaw(), e.getTextChannel());
                }
            }
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent e) {
        if (e.isFromGuild()) {
            if (e.getUser() != null) {
                onEvent(e.getGuild(), "Reaction", "Der User " + e.getUser().getName() + " hat mit " + e.getReactionEmote().getAsReactionCode() + " auf die Nachricht " + e.getTextChannel().getHistory().getMessageById(e.getMessageId()) + " reagiert!", e.getTextChannel());
            }
        }
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent e) {
        if (e.isFromGuild()) {
            if (e.getUser() != null) {
                onEvent(e.getGuild(), "Reaction", "Der User " + e.getUser().getName() + " hat seine Reaction " + e.getReactionEmote().getAsReactionCode() + " auf die Nachricht " + e.getTextChannel().getHistory().getMessageById(e.getMessageId()) + " entfernt!", e.getTextChannel());
            }
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        if (e.isFromGuild()) {
            if (e.getName().equalsIgnoreCase(Discord.cmdSettings)) {
                if (e.getSubcommandName() != null) {
                    if (e.getSubcommandName().equalsIgnoreCase(Discord.cmdSubSettingsChannel)) {
                        OptionMapping option = e.getOption(Discord.cmdSubSettingsChannelOptionChannel);
                        if (option != null) {
                            TextChannel channel = option.getAsTextChannel();
                            if (channel != null) {
                                if (Main.getMysql().existsDatabase(Main.dbName)) {
                                    if (Main.getMysql().getDatabase(Main.dbName).existsTable(Main.tableName)) {
                                        Table table = Main.getMysql().getDatabase(Main.dbName).getTable(Main.tableName);
                                        if (table.hasColumn(Main.clmServerID)) {
                                            if (e.getGuild() != null) {
                                                if (table.getColumn(Main.clmServerID).getAll().contains(e.getGuild().getId())) {
                                                    HashMap<String, Object> row = table.getRow(table.getColumn(Main.clmServerID), e.getGuild().getId());
                                                    table.getColumn(Main.clmChannelID).set(channel.getId(), Integer.parseInt(row.get("id").toString()));
                                                    e.getInteraction().reply("Der neue Log Channel ist nun " + channel.getAsMention() + "!").queue();
                                                } else {
                                                    table.insert(new Row(table, table.getDatabase()).with(Main.clmChannelID, channel.getId()).with(Main.clmServerID, e.getGuild().getId()));
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
            } else {
                onEvent(e.getGuild(), "Command", "Der User " + e.getUser().getName() + " hat den Command " + e.getName() + e.getSubcommandName(), e.getTextChannel());
            }
        }
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent e) {
        onEvent(e.getGuild(), "Server Verlassen", "Der User " + e.getUser().getName() + " hat den Server verlassen!", null);
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        onEvent(e.getGuild(), "Server join", "Der User " + e.getUser().getName() + " ist dem Server beigetreten!", null);
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
    public void onGuildVoiceJoin(GuildVoiceJoinEvent e) {
        onEvent(e.getGuild(), "User Channel Join", "Der User " + e.getMember().getUser().getName() + " ist dem Channel " + e.getChannelJoined().getAsMention() + " gejoined!", null);
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent e) {
        onEvent(e.getGuild(), "User Channel Leave", "Der User " + e.getMember().getUser().getName() + " hat den Channel " + e.getChannelLeft().getAsMention() + " verlassen!", null);
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent e) {
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
        if (Main.getMysql().existsDatabase(Main.dbName)) {
            if (Main.getMysql().getDatabase(Main.dbName).existsTable(Main.tableName)) {
                Table table = Main.getMysql().getDatabase(Main.dbName).getTable(Main.tableName);
                if (table.hasColumn(Main.clmServerID)) {
                    if (table.getColumn(Main.clmServerID).getAll().contains(guild.getId())) {
                        HashMap<String, Object> row = table.getRow(table.getColumn(Main.clmServerID), guild.getId());
                        if (!row.get(Main.clmChannelID).toString().isEmpty()) {
                            TextChannel channel = guild.getTextChannelById(row.get(Main.clmChannelID).toString());
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
                    Main.sendErrorMessage("Can`t find Column ServerID");
                }
            } else {
                Main.sendErrorMessage("Table don´t exists");
            }
        } else {
            Main.sendErrorMessage("Database don´t exists");
        }
    }
}
