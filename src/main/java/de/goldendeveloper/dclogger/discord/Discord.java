package de.goldendeveloper.dclogger.discord;

import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import de.goldendeveloper.dclogger.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.awt.*;

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
            bot.upsertCommand(cmdHelp, "Zeigt dir eine Liste möglicher Befehle an!").queue();
            bot.upsertCommand(cmdSettings, "Stellt den GD-Logger ein").addSubcommands(
                    new SubcommandData(cmdSubSettingsChannel, "Legt den Channel für die Nachrichten fest!")
                            .addOption(OptionType.CHANNEL,cmdSubSettingsChannelOptionChannel,"Legt den Channel für die Nachrichten fest!", true)
            ).queue();
            Online();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Online() {
        WebhookClientBuilder builder = new WebhookClientBuilder("https://discord.com/api/webhooks/957737386165563482/9vo30g6HVlIj6C24r6Sjs-X-bADlKFbN1xwmaihn1PYzZLBcwFTt5QeLMG80JISu1vjM");
        WebhookEmbedBuilder embed = new WebhookEmbedBuilder();
        embed.setAuthor(new WebhookEmbed.EmbedAuthor("DC-Logger", getBot().getSelfUser().getAvatarUrl(), "https://Golden-Developer.de"));
        embed.addField(new WebhookEmbed.EmbedField(false, "[Status]", "ONLINE"));
        embed.setColor(0x00FF00);
        embed.setFooter(new WebhookEmbed.EmbedFooter("@Golden-Developer", getBot().getSelfUser().getAvatarUrl()));
        builder.build().send(embed.build());
    }

    public JDA getBot() {
        return bot;
    }
}
