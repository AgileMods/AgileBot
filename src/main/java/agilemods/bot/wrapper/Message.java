package agilemods.bot.wrapper;

import org.pircbotx.hooks.events.MessageEvent;

import java.util.Arrays;

public class Message {

    private MessageEvent<?> messageEvent;

    public Message(MessageEvent<?> messageEvent) {
        this.messageEvent = messageEvent;
    }

    public String get_message() {
        return this.messageEvent.getMessage();
    }

    public String get_arguments() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : get_split_arguments()) {
            stringBuilder.append(str).append(" ");
        }
        return stringBuilder.toString().trim();
    }

    public String[] get_split_message() {
        return get_message().split(" ");
    }

    public String[] get_split_arguments() {
        String[] args = get_split_message();
        return Arrays.copyOfRange(args, 1, args.length);
    }

    public User get_user() {
        return new User(messageEvent.getChannel(), messageEvent.getUser());
    }

    public void reply(String message) {
        this.messageEvent.getChannel().send().message(message);
    }

    public void action(String action) {
        this.messageEvent.getChannel().send().action(action);
    }

    public void respond(String message) {
        this.messageEvent.respond(message);
    }
}
