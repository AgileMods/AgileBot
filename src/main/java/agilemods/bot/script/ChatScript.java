package agilemods.bot.script;

import agilemods.bot.wrapper.HttpWrapper;
import agilemods.bot.wrapper.Message;
import org.pircbotx.hooks.events.MessageEvent;

import javax.script.*;

public class ChatScript {

    private ScriptEngine scriptEngine;

    public String function;

    private CompiledScript script;

    public ChatScript(String function) {
        this.scriptEngine = ScriptHelper.getLuaEngine();
        this.scriptEngine.setBindings(this.scriptEngine.createBindings(), ScriptContext.GLOBAL_SCOPE);
        this.function = function;
    }

    public void reload() throws ScriptException {
        script = ((Compilable)scriptEngine).compile(function);
    }

    public void onMessage(MessageEvent<?> messageEvent) throws ScriptException {
        Bindings bindings = scriptEngine.createBindings();
        bindings.put("http", new HttpWrapper());
        bindings.put("message", new Message(messageEvent));
        script.eval(bindings);
    }
}
