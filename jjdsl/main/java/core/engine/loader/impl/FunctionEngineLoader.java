package core.engine.loader.impl;

import core.engine.loader.EngineLoader;

import javax.script.ScriptEngine;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FunctionEngineLoader implements EngineLoader {
    public FunctionEngineLoader() {
    }

    @Override
    public void populateEngine(ScriptEngine engine) throws Exception {
        try (Stream<Path> fileStream = Files.list(Paths.get("./scripts"))) {
            fileStream.map(Path::getFileName)
                    .map(Path::toString)
                    .sorted()
                    .map(FunctionEngineLoader::joinScript).forEach(script -> {
                        try {
                            engine.eval(script);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    private static String joinScript(String file) {
        try {
            Path path = Paths.get("./scripts/" + file);
            return String.join("\n", Files.readAllLines(path)) + "\n";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
