package de.goldendeveloper.dclogger.discord;

import de.goldendeveloper.dclogger.Main;
import de.goldendeveloper.mysql.entities.Table;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
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

import java.awt.*;
import java.time.LocalTime;

public class Events extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent e) {
        if (e.isFromGuild()) {
            if (Main.getMysql().existsDatabase(Main.dbName)) {
                if (Main.getMysql().getDatabase(Main.dbName).existsTable(Main.tableName)) {
                    Table table = Main.getMysql().getDatabase(Main.dbName).getTable(Main.tableName);
                    if (table.hasColumn(Main.clmServerID)) {
                        if (table.getColumn(Main.clmServerID).getAll().contains(e.getGuild().getId())) {

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

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent e) {
        if (e.isFromGuild()) {
            if (Main.getMysql().existsDatabase(Main.dbName)) {
                if (Main.getMysql().getDatabase(Main.dbName).existsTable(Main.tableName)) {
                    Table table = Main.getMysql().getDatabase(Main.dbName).getTable(Main.tableName);
                    if (table.hasColumn(Main.clmServerID)) {
                        if (table.getColumn(Main.clmServerID).getAll().contains(e.getGuild().getId())) {

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

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        if (e.isFromGuild()) {
            if (Main.getMysql().existsDatabase(Main.dbName)) {
                if (Main.getMysql().getDatabase(Main.dbName).existsTable(Main.tableName)) {
                    Table table = Main.getMysql().getDatabase(Main.dbName).getTable(Main.tableName);
                    if (table.hasColumn(Main.clmServerID)) {
                        if (table.getColumn(Main.clmServerID).getAll().contains(e.getGuild().getId())) {

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

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent e) {
        if (Main.getMysql().existsDatabase(Main.dbName)) {
            if (Main.getMysql().getDatabase(Main.dbName).existsTable(Main.tableName)) {
                Table table = Main.getMysql().getDatabase(Main.dbName).getTable(Main.tableName);
                if (table.hasColumn(Main.clmServerID)) {
                    if (table.getColumn(Main.clmServerID).getAll().contains(e.getGuild().getId())) {

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

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        if (Main.getMysql().existsDatabase(Main.dbName)) {
            if (Main.getMysql().getDatabase(Main.dbName).existsTable(Main.tableName)) {
                Table table = Main.getMysql().getDatabase(Main.dbName).getTable(Main.tableName);
                if (table.hasColumn(Main.clmServerID)) {
                    if (table.getColumn(Main.clmServerID).getAll().contains(e.getGuild().getId())) {

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

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent e) {
        if (Main.getMysql().existsDatabase(Main.dbName)) {
            if (Main.getMysql().getDatabase(Main.dbName).existsTable(Main.tableName)) {
                Table table = Main.getMysql().getDatabase(Main.dbName).getTable(Main.tableName);
                if (table.hasColumn(Main.clmServerID)) {
                    if (table.getColumn(Main.clmServerID).getAll().contains(e.getGuild().getId())) {

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

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent e) {
        if (Main.getMysql().existsDatabase(Main.dbName)) {
            if (Main.getMysql().getDatabase(Main.dbName).existsTable(Main.tableName)) {
                Table table = Main.getMysql().getDatabase(Main.dbName).getTable(Main.tableName);
                if (table.hasColumn(Main.clmServerID)) {
                    if (table.getColumn(Main.clmServerID).getAll().contains(e.getGuild().getId())) {

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

    @Override
    public void onGuildUpdateAfkChannel(GuildUpdateAfkChannelEvent e) {
        if (Main.getMysql().existsDatabase(Main.dbName)) {
            if (Main.getMysql().getDatabase(Main.dbName).existsTable(Main.tableName)) {
                Table table = Main.getMysql().getDatabase(Main.dbName).getTable(Main.tableName);
                if (table.hasColumn(Main.clmServerID)) {
                    if (table.getColumn(Main.clmServerID).getAll().contains(e.getGuild().getId())) {

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

    @Override
    public void onGuildUpdateBanner(GuildUpdateBannerEvent e) {
        if (Main.getMysql().existsDatabase(Main.dbName)) {
            if (Main.getMysql().getDatabase(Main.dbName).existsTable(Main.tableName)) {
                Table table = Main.getMysql().getDatabase(Main.dbName).getTable(Main.tableName);
                if (table.hasColumn(Main.clmServerID)) {
                    if (table.getColumn(Main.clmServerID).getAll().contains(e.getGuild().getId())) {

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

    @Override
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent e) {
        if (Main.getMysql().existsDatabase(Main.dbName)) {
            if (Main.getMysql().getDatabase(Main.dbName).existsTable(Main.tableName)) {
                Table table = Main.getMysql().getDatabase(Main.dbName).getTable(Main.tableName);
                if (table.hasColumn(Main.clmServerID)) {
                    if (table.getColumn(Main.clmServerID).getAll().contains(e.getGuild().getId())) {

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

    @Override
    public void onGuildMemberUpdateBoostTime(GuildMemberUpdateBoostTimeEvent e) {
        if (Main.getMysql().existsDatabase(Main.dbName)) {
            if (Main.getMysql().getDatabase(Main.dbName).existsTable(Main.tableName)) {
                Table table = Main.getMysql().getDatabase(Main.dbName).getTable(Main.tableName);
                if (table.hasColumn(Main.clmServerID)) {
                    if (table.getColumn(Main.clmServerID).getAll().contains(e.getGuild().getId())) {

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

    @Override
    public void onGuildUpdateMaxMembers(GuildUpdateMaxMembersEvent e) {
        if (Main.getMysql().existsDatabase(Main.dbName)) {
            if (Main.getMysql().getDatabase(Main.dbName).existsTable(Main.tableName)) {
                Table table = Main.getMysql().getDatabase(Main.dbName).getTable(Main.tableName);
                if (table.hasColumn(Main.clmServerID)) {
                    if (table.getColumn(Main.clmServerID).getAll().contains(e.getGuild().getId())) {

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

    @Override
    public void onGuildUnban(GuildUnbanEvent e) {
        if (Main.getMysql().existsDatabase(Main.dbName)) {
            if (Main.getMysql().getDatabase(Main.dbName).existsTable(Main.tableName)) {
                Table table = Main.getMysql().getDatabase(Main.dbName).getTable(Main.tableName);
                if (table.hasColumn(Main.clmServerID)) {
                    if (table.getColumn(Main.clmServerID).getAll().contains(e.getGuild().getId())) {

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
}
