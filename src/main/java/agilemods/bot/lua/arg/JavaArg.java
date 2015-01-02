package agilemods.bot.lua.arg;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public abstract class JavaArg {

    public abstract String getType();

    public abstract void fillTable(LuaTable table);

    public LuaTable generateTable() {
        LuaTable table = LuaTable.tableOf();
        table.set("_arg_type", getType());
        fillTable(table);
        return table;
    }

    protected final boolean checkType(LuaTable table, String type) {
        LuaValue value = table.get("_arg_type");
        return value != null && value.toString().equals(type);
    }
}
