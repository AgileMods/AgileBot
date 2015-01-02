package agilemods.bot.wrapper;

import agilemods.bot.management.Ownership;

public class User {

    private org.pircbotx.Channel channel;
    private org.pircbotx.User user;

    public User(org.pircbotx.Channel channel, org.pircbotx.User user) {
        this.channel = channel;
        this.user = user;
    }

    public String get_nick() {
        return user.getNick();
    }

    public String get_name() {
        return user.getRealName();
    }

    public boolean is_op() {
        return user.getChannelsOpIn().contains(channel);
    }

    public boolean has_voice() {
        return user.getChannelsVoiceIn().contains(channel);
    }

    public boolean has_ownership() {
        return Ownership.hasOwnership(user);
    }

    public boolean is_away() {
        return user.isAway();
    }
}
