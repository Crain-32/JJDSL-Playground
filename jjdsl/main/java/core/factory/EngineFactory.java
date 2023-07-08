package core.factory;

import core.engine.loader.EngineLoader;
import core.engine.loader.LoaderType;
import core.engine.loader.impl.CompositeEngineLoader;
import core.engine.loader.impl.DataClassEngineLoader;
import core.engine.loader.impl.FunctionEngineLoader;
import core.engine.loader.impl.UtilClassEngineLoader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class EngineFactory {

    private EngineFactory() {
    }

    private static EngineLoader getEngineLoader(LoaderType loaderType) {
        EngineLoader engineLoader;
        switch (loaderType) {
            case DATA:
                engineLoader = new DataClassEngineLoader();
                break;
            case FUNCTION:
                engineLoader = new FunctionEngineLoader();
                break;
            case COMPOSITE:
                engineLoader = new CompositeEngineLoader();
                break;
            case UTIL:
                engineLoader = new UtilClassEngineLoader();
                break;
            default:
                throw new IllegalArgumentException(String.format("Failed to Map LoaderType %s", loaderType));
        }
        return engineLoader;
    }


    public static Invocable createEngine() throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        EngineLoader loader = EngineFactory.getEngineLoader(LoaderType.COMPOSITE);
        loader.populateEngine(engine);
        return (Invocable) engine;
    }
}
