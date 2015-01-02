package dmillerw.agile.script;

import dmillerw.agile.wrapper.ResponseWrapper;
import org.pircbotx.Channel;

import javax.script.*;

public class TemporaryScript {

    private Channel channel;

    private ScriptEngine scriptEngine;

    private String function;

    private CompiledScript script;

    public TemporaryScript(Channel channel, String function) throws ScriptException {
        this.channel = channel;
        this.scriptEngine = ScriptHelper.getLuaEngine();
        this.scriptEngine.setBindings(this.scriptEngine.createBindings(), ScriptContext.GLOBAL_SCOPE);
        this.function = function;
        this.script = ((Compilable)scriptEngine).compile(function);
    }

    public TemporaryScript evaluate() throws ScriptException {
        Bindings bindings = ScriptHelper.getBindings(scriptEngine);
        bindings.put("response", new ResponseWrapper(channel));
        script.eval(bindings);
        return this;
    }
}
