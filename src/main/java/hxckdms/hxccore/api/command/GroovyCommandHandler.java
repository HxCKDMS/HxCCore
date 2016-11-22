package hxckdms.hxccore.api.command;

import hxckdms.hxccore.libraries.GlobalVariables;
import net.minecraft.command.ICommandSender;
import org.codehaus.groovy.jsr223.GroovyScriptEngineImpl;

import javax.script.Bindings;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.HashSet;

import static javax.script.ScriptContext.GLOBAL_SCOPE;

public class GroovyCommandHandler {
    private static GroovyCommandHandler instance;
    private GroovyScriptEngineImpl scriptEngine;
    private File scriptDir;
    private HashSet<File> commandScripts = new HashSet<>();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private GroovyCommandHandler() {
        scriptEngine = new GroovyScriptEngineImpl();
        scriptDir = new File(GlobalVariables.modConfigDir, "scripts");
        if (scriptDir.exists()) scriptDir.mkdirs();
        loadScripts();
        instance = this;
    }

    public static GroovyCommandHandler getInstance() {
        if (instance == null) instance = new GroovyCommandHandler();
        return instance;
    }

    private void loadScripts() {
        File[] scripts = scriptDir.listFiles(file -> file.getName().endsWith(".groovy"));
        if (scripts != null) Collections.addAll(commandScripts, scripts);
    }

    boolean doesCommandScriptExist(String command) {
        return commandScripts.parallelStream().map(File::getName).map(name -> name.replace(".groovy", "")).anyMatch(name -> name.equalsIgnoreCase(command));
    }

    void executeCommandScript(String command, ICommandSender sender) {
        if (doesCommandScriptExist(command)) {
            File script = commandScripts.parallelStream().filter(file -> file.getName().replace(".groovy", "").equalsIgnoreCase(command)).findAny().orElseThrow(() -> new NullPointerException("this is not supposed to happen."));

            try {
                Bindings bindings = scriptEngine.createBindings();
                bindings.put("player", sender);
                scriptEngine.setBindings(bindings, GLOBAL_SCOPE);

                scriptEngine.eval(new FileReader(script), bindings);
            } catch (ScriptException | FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
