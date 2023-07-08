package core.engine.loader.impl;

import core.engine.loader.EngineLoader;
import core.engine.loader.LoaderType;
import core.factory.EngineFactory;
import utils.ReflectionUtils;

import javax.script.ScriptEngine;

public class CompositeEngineLoader implements EngineLoader {

    public CompositeEngineLoader() {
    }

    @Override
    public void populateEngine(ScriptEngine engine) throws Exception {
        EngineLoader utilEngine = ReflectionUtils.callPrivateStaticFunction(EngineFactory.class, "getEngineLoader", EngineLoader.class, LoaderType.UTIL);
        EngineLoader dataEngine = ReflectionUtils.callPrivateStaticFunction(EngineFactory.class, "getEngineLoader", EngineLoader.class, LoaderType.DATA);
        EngineLoader functionEngine = ReflectionUtils.callPrivateStaticFunction(EngineFactory.class, "getEngineLoader", EngineLoader.class, LoaderType.FUNCTION);
        utilEngine.populateEngine(engine);
        dataEngine.populateEngine(engine);
        functionEngine.populateEngine(engine);
    }
}
