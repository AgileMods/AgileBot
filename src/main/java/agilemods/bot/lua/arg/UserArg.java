package agilemods.bot.lua.arg;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.pircbotx.Channel;
import org.pircbotx.User;

public class UserArg extends JavaArg {

    private User user;

    public UserArg(User user) {
        this.user = user;
    }

    @Override
    public String getType() {
        return "user";
    }

    @Override
    public void fillTable(LuaTable table) {
        table.set("nickname", user.getNick());
        table.set("is_op", new is_op());
        table.set("has_voice", new has_voice());
    }

    public class is_op extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            if (!arg.istable()) {
                argerror(0, "Expected table, got " + arg.typename());
            }

            LuaTable table = (LuaTable) arg;

            if (!checkType(table, "channel")) {
                error("Invalid table. Expected channel data");
            }

            LuaValue channelName = table.get("name");

            for (Channel channel : user.getChannelsOpIn()) {
                String cname = channel.getName().replace("#", "");
                String sname = channelName.toString().replace("#", "");
                if (sname.equalsIgnoreCase(cname)) {
                    return LuaValue.TRUE;
                }
            }

            return LuaValue.FALSE;
        }
    }

    public class has_voice extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            if (!arg.istable()) {
                argerror(0, "Expected table, got " + arg.typename());
            }

            LuaTable table = (LuaTable) arg;

            if (!checkType(table, "channel")) {
                error("Invalid table. Expected channel data");
            }

            LuaValue channelName = table.get("name");

            for (Channel channel : user.getChannelsVoiceIn()) {
                String cname = channel.getName().replace("#", "");
                String sname = channelName.toString().replace("#", "");
                if (sname.equalsIgnoreCase(cname)) {
                    return LuaValue.TRUE;
                }
            }

            return LuaValue.FALSE;
        }
    }
}
