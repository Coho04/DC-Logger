package io.github.coho04.logger.discord;

import io.github.coho04.logger.Main;
import io.github.coho04.logger.MysqlConnection;
import io.github.coho04.mysql.entities.SearchResult;
import io.github.coho04.mysql.entities.Table;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
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
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.time.LocalTime;
import java.util.HashMap;

public class CustomEvents extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.isFromGuild() && e.getMember() != null && e.getMember().getUser() != e.getJDA().getSelfUser()) {
            onEvent(e.getGuild(), "Message", "[Channel]: " + e.getChannel().asTextChannel().getAsMention() + " \n Message: " + e.getMember().getUser().getAsMention() + " >> " + e.getMessage().getContentRaw(), e.getChannel().asTextChannel());
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent e) {
        if (e.isFromGuild() && e.getUser() != null) {
            onEvent(e.getGuild(), "Reaction", "Der User " + e.getUser().getName() + " hat mit " + e.getReaction().getEmoji().getAsReactionCode() + " auf die Nachricht " + e.getChannel().asTextChannel().getHistory().getMessageById(e.getMessageId()) + " reagiert!", e.getChannel().asTextChannel());
        }
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent e) {
        if (e.isFromGuild() && e.getUser() != null) {
            onEvent(e.getGuild(), "Reaction", "Der User " + e.getUser().getName() + " hat seine Reaction " + e.getReaction().getEmoji().getAsReactionCode() + " auf die Nachricht " + e.getChannel().asTextChannel().getHistory().getMessageById(e.getMessageId()) + " entfernt!", e.getChannel().asTextChannel());
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        if (e.isFromGuild()) {
            onEvent(e.getGuild(), "Command", "Der User " + e.getUser().getName() + " hat den Command " + e.getName() + e.getSubcommandName(), e.getChannel().asTextChannel());
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
        onEvent(e.getGuild(), "Time Boosted Update", e.getOldTimeBoosted() + " Neu " + e.getNewTimeBoosted(), null);
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
        if (e.getChannelLeft() != null && e.getChannelJoined() != null) {
            onEvent(e.getGuild(), "User Channel Move", "Der User " + e.getMember().getUser().getName() + " hat den Channel " + e.getChannelLeft().getAsMention() + " verlassen und ist dem Channel " + e.getChannelJoined() + " beigetreten!", null);
        }

        if (e.getChannelLeft() != null) {
            onEvent(e.getGuild(), "User Channel Leave", "Der User " + e.getMember().getUser().getName() + " hat den Channel " + e.getChannelLeft().getAsMention() + " verlassen!", null);
        }

        if (e.getChannelJoined() != null) {
            onEvent(e.getGuild(), "User Channel Join", "Der User " + e.getMember().getUser().getName() + " ist dem Channel " + e.getChannelJoined().getAsMention() + " beigetreten!", null);
        }
    }

    private MessageEmbed onEmbed(Guild guild, String event, String value) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("**Logger**");
        builder.setThumbnail(guild.getBannerUrl());
        builder.setAuthor("@Logger");
        builder.setTimestamp(LocalTime.now());
        builder.setColor(Color.gray);
        builder.addField("**" + event + "**", value, true);
        return builder.build();
    }

    private void onEvent(Guild guild, String event, String value, @Nullable TextChannel textChannel) {
        if (Main.getMysqlConnection().getMysql().existsDatabase(MysqlConnection.dbName)) {
            if (Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.dbName).existsTable(MysqlConnection.tableName)) {
                Table table = Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.dbName).getTable(MysqlConnection.tableName);
                if (table.hasColumn(MysqlConnection.clmServerID)) {
                    if (table.getColumn(MysqlConnection.clmServerID).getAll().getAsString().contains(guild.getId())) {
                        HashMap<String, SearchResult> row = table.getRow(table.getColumn(MysqlConnection.clmServerID), guild.getId()).getData();
                        if (!row.get(MysqlConnection.clmChannelID).toString().isEmpty()) {
                            TextChannel channel = guild.getTextChannelById(row.get(MysqlConnection.clmChannelID).getAsString());
                            if (channel != null) {
                                channel.sendMessageEmbeds(onEmbed(guild, event, value)).queue();
                            }
                        } else {
                            if (textChannel != null) {
                                textChannel.sendMessage("Es wurde kein Log Channel eingerichtet").queue();
                            }
                        }
                    }
                }
            }
        }
    }
}
