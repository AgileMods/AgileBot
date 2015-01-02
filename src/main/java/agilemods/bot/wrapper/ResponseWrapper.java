package agilemods.bot.wrapper;

import org.pircbotx.Channel;

public class ResponseWrapper {

    private Channel channel;

    public ResponseWrapper(Channel channel) {
        this.channel = channel;
    }

    public void send(String message) {
        channel.send().message(message);
    }
}
