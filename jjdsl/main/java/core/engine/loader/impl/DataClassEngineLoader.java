package core.engine.loader.impl;

import core.engine.loader.EngineLoader;
import monkey.data.Token;
import monkey.data.constants.TokenType;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.stream.Stream;

public class DataClassEngineLoader implements EngineLoader {

    public DataClassEngineLoader() {
    }

    private static final String LOAD_TYPE = "var %s = Java.type('%s');";

    @Override
    public void populateEngine(ScriptEngine engine) {
        Stream.of(Token.class, TokenType.class)
                .map(clazz ->
                        String.format(LOAD_TYPE, clazz.getSimpleName(), clazz.getCanonicalName())
                ).forEach(script -> {
                    try {
                        engine.eval(script);
                    } catch (ScriptException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
