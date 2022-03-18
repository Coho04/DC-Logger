package de.goldendeveloper.dclogger.discord;

import de.goldendeveloper.dclogger.Main;
import de.goldendeveloper.mysql.entities.Table;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
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
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nullable;
import java.awt.*;
import java.time.LocalTime;
import java.util.HashMap;

public class Events extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent e) {
        if (e.isFromGuild()) {
            onEvent(e.getGuild(), "Reaction", "Der User " + e.getUser().getName() + " hat mit " + e.getReactionEmote().getAsReactionCode() + " auf die Nachricht " + e.getTextChannel().getHistory().getMessageById(e.getMessageId()) + " reagiert!",e.getTextChannel());
        }
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent e) {
        if (e.isFromGuild()) {
            onEvent(e.getGuild(), "Reaction", "Der User " + e.getUser().getName() + " hat seine Reaction " + e.getReactionEmote().getAsReactionCode() + " auf die Nachricht " + e.getTextChannel().getHistory().getMessageById(e.getMessageId()) + " entfernt!",e.getTextChannel());
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        if (e.isFromGuild()) {
            onEvent(e.getGuild(), "Command", "", e.getTextChannel());
        }
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent e) {
        onEvent(e.getGuild(), "", "", null);
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        onEvent(e.getGuild(), "", "", null);
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent e) {
        onEvent(e.getGuild(), "", "", null);
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent e) {
        onEvent(e.getGuild(), "", "", null);
    }

    @Override
    public void onGuildUpdateAfkChannel(GuildUpdateAfkChannelEvent e) {
        onEvent(e.getGuild(), "", "", null);
    }

    @Override
    public void onGuildUpdateBanner(GuildUpdateBannerEvent e) {
        onEvent(e.getGuild(), "", "", null);
    }

    @Override
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent e) {
        onEvent(e.getGuild(), "", "", null);
    }

    @Override
    public void onGuildMemberUpdateBoostTime(GuildMemberUpdateBoostTimeEvent e) {
        onEvent(e.getGuild(), "", "", null);
    }

    @Override
    public void onGuildUpdateMaxMembers(GuildUpdateMaxMembersEvent e) {
        onEvent(e.getGuild(), "", "", null);
    }

    @Override
    public void onGuildUnban(GuildUnbanEvent e) {

    }

    private MessageEmbed onEmbed(Guild guild, String Event, String Value) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("**Logger**", "https://golden-developer.de");
        builder.setThumbnail(guild.getBannerUrl());
        builder.setAuthor("@Golden-Developer", "https://golden-developer.de");
        builder.setTimestamp(LocalTime.now());
        builder.setColor(Color.gray);
        builder.addField("**" + Event + "**", Value, true);
        return builder.build();
    }

    private void onEvent(Guild guild, String Event, String Value,@Nullable TextChannel ch) {
        if (Main.getMysql().existsDatabase(Main.dbName)) {
            if (Main.getMysql().getDatabase(Main.dbName).existsTable(Main.tableName)) {
                Table table = Main.getMysql().getDatabase(Main.dbName).getTable(Main.tableName);
                if (table.hasColumn(Main.clmServerID)) {
                    if (table.getColumn(Main.clmServerID).getAll().contains(guild.getId())) {
                        HashMap<String, Object> row = table.getRow(table.getColumn(Main.clmServerID), guild.getId());
                        if (!row.get(Main.clmChannelID).toString().isEmpty()) {
                            TextChannel channel = guild.getTextChannelById(row.get(Main.clmChannelID).toString());
                            if (channel != null) {
                                channel.sendMessageEmbeds(onEmbed(guild, Event,Value)).queue();
                            }
                        } else {
                            if (ch != null) {
                                ch.sendMessage("Es wurde kein Log Channel eingerichtet").queue();
                            }
                        }
                    }
                } else {
                    Main.sendErrorMessage("Can not find Column ServerID");
                }
            } else {
                Main.sendErrorMessage("Table don´t exists");
            }
        } else {
            Main.sendErrorMessage("Database don´t exists");
        }
    }
}
