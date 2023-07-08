package core.engine.loader;

import javax.script.ScriptEngine;

public interface EngineLoader {

    void populateEngine(ScriptEngine engine) throws Exception;
}
