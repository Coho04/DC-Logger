package de.goldendeveloper.dclogger.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Discord {

    private JDA bot;
    public static String cmdHelp = "help";
    public static String cmdSettings = "settings";
    public static String cmdSubSettingsChannel = "channel";
    public static String cmdSubSettingsChannelOptionChannel = "channelid";

    public Discord(String Token) {
        try {
            bot = JDABuilder.createDefault(Token)
                    .setChunkingFilter(ChunkingFilter.ALL)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .enableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.ROLE_TAGS, CacheFlag.EMOTE, CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS)
                    .enableIntents(GatewayIntent.GUILD_MESSAGE_REACTIONS,
                            GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_EMOJIS,
                            GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_PRESENCES,
                            GatewayIntent.GUILD_BANS, GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                            GatewayIntent.GUILD_INVITES, GatewayIntent.DIRECT_MESSAGE_TYPING,
                            GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.GUILD_VOICE_STATES,
                            GatewayIntent.GUILD_WEBHOOKS, GatewayIntent.GUILD_MEMBERS,
                            GatewayIntent.GUILD_MESSAGE_TYPING)
                    .addEventListeners(new Events())
                    .setAutoReconnect(true)
                    .build().awaitReady();
            getBot().upsertCommand(cmdHelp, "Zeigt dir eine Liste möglicher Befehle an!").queue();
            getBot().upsertCommand(cmdSettings, "Stellt den GD-Logger ein").addSubcommands(
                    new SubcommandData(cmdSubSettingsChannel, "Legt den Channel für die Nachrichten fest!")
                            .addOption(OptionType.CHANNEL,cmdSubSettingsChannelOptionChannel,"Legt den Channel für die Nachrichten fest!", true)
            ).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JDA getBot() {
        return bot;
    }
}
