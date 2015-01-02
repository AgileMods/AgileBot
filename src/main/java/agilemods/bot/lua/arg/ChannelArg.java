package agilemods.bot.lua.arg;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.pircbotx.Channel;

public class ChannelArg extends JavaArg {

    private Channel channel;

    public ChannelArg(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String getType() {
        return "channel";
    }

    @Override
    public void fillTable(LuaTable table) {
        table.set("action", new action());
        table.set("send", new send());

        table.set("name", channel.getName());
        table.set("topic", channel.getTopic());
        table.set("topic_setter", channel.getTopicSetter());
        table.set("mode", channel.getMode());
    }

    public class action extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            channel.send().action(arg.toString());
            return LuaValue.NIL;
        }
    }

    public class send extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            channel.send().message(arg.toString());
            return LuaValue.NIL;
        }
    }
}
